package nl.knaw.dans.ersy.textmining.clustering;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.mahout.clustering.cdbw.CDbwEvaluator;
import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.clustering.evaluation.ClusterEvaluator;
import org.apache.mahout.clustering.evaluation.RepresentativePointsDriver;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.ClassUtils;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirIterable;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirValueIterable;
import org.apache.mahout.utils.clustering.CSVClusterWriter;
import org.apache.mahout.utils.clustering.ClusterDumperWriter;
import org.apache.mahout.utils.clustering.ClusterWriter;
import org.apache.mahout.utils.clustering.GraphMLClusterWriter;
import org.apache.mahout.utils.vectors.VectorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

public final class ClusterPrinter extends AbstractJob {

  public static final String SAMPLE_POINTS = "samplePoints";
  protected DistanceMeasure measure;

  public enum OUTPUT_FORMAT {
    TEXT,
    CSV,
    GRAPH_ML,
  }

  private static final Logger log = LoggerFactory.getLogger(ClusterPrinter.class);
  private Path seqFileDir;
  private Path pointsDir;
  private long maxPointsPerCluster = Long.MAX_VALUE;
  private String termDictionary;
  private String dictionaryFormat;
  private int subString = Integer.MAX_VALUE;
  private int numTopFeatures = 10;
  private Map<Integer, List<WeightedVectorWritable>> clusterIdToPoints;
  private OUTPUT_FORMAT outputFormat = OUTPUT_FORMAT.TEXT;
  private boolean runEvaluation;

  public ClusterPrinter(Path seqFileDir, Path pointsDir) {
    this.seqFileDir = seqFileDir;
    this.pointsDir = pointsDir;
    init();
  }

  public ClusterPrinter() {
    setConf(new Configuration());
  }

  public static void main(String[] args) throws Exception {
    new ClusterPrinter().run(args);
  }

  @Override
  public int run(String[] args) throws Exception {
    measure = ClassUtils.instantiateAs("org.apache.mahout.common.distance.CosineDistanceMeasure", DistanceMeasure.class);

    init();
    printClusters(null);
    return 0;
  }
 
  

  public void printClusters(String[] dictionary) throws Exception {
    Configuration conf = new Configuration();

    if (this.termDictionary != null) {
      if ("text".equals(dictionaryFormat)) {
        dictionary = VectorHelper.loadTermDictionary(new File(this.termDictionary));
      } else if ("sequencefile".equals(dictionaryFormat)) {
        dictionary = VectorHelper.loadTermDictionary(conf, this.termDictionary);
      } else {
        throw new IllegalArgumentException("Invalid dictionary format");
      }
    }

    Writer writer;
    boolean shouldClose;
    if (this.outputFile == null) {
      shouldClose = false;
      writer = new OutputStreamWriter(System.out);
    } else {
      shouldClose = true;
      if (outputFile.getName().startsWith("s3n://")) {
        Path p = outputPath;
        FileSystem fs = FileSystem.get(p.toUri(), conf);
        writer = new OutputStreamWriter(fs.create(p), Charsets.UTF_8);
      } else {
        writer = Files.newWriter(this.outputFile, Charsets.UTF_8);
      }
    }
    ClusterWriter clusterWriter = createClusterWriter(writer, dictionary);
    try {
      long numWritten = clusterWriter.write(new SequenceFileDirValueIterable<ClusterWritable>(new Path(seqFileDir, "part-*"), PathType.GLOB, conf));

      writer.flush();
      if (runEvaluation) {
        HadoopUtil.delete(conf, new Path("tmp/representative"));
        int numIters = 5;
        RepresentativePointsDriver.main(new String[]{
                "--input", seqFileDir.toString(),
                "--output", "tmp/representative",
                "--clusteredPoints", pointsDir.toString(),
                "--distanceMeasure", measure.getClass().getName(),
                "--maxIter", String.valueOf(numIters)//
        });
        conf.set(RepresentativePointsDriver.DISTANCE_MEASURE_KEY, measure.getClass().getName());
        conf.set(RepresentativePointsDriver.STATE_IN_KEY, "tmp/representative/representativePoints-" + numIters);
        ClusterEvaluator ce = new ClusterEvaluator(conf, seqFileDir);
        writer.append("\n");
        writer.append("Inter-Cluster Density: ").append(String.valueOf(ce.interClusterDensity())).append("\n");
        writer.append("Intra-Cluster Density: ").append(String.valueOf(ce.intraClusterDensity())).append("\n");
        CDbwEvaluator cdbw = new CDbwEvaluator(conf, seqFileDir);
        writer.append("CDbw Inter-Cluster Density: ").append(String.valueOf(cdbw.interClusterDensity())).append("\n");
        writer.append("CDbw Intra-Cluster Density: ").append(String.valueOf(cdbw.intraClusterDensity())).append("\n");
        writer.append("CDbw Separation: ").append(String.valueOf(cdbw.separation())).append("\n");
        writer.flush();
      }
      log.info("Wrote {} clusters", numWritten);
    } finally {
      if (shouldClose) {
        Closeables.closeQuietly(clusterWriter);
      } else {
        if (clusterWriter instanceof GraphMLClusterWriter) {
          clusterWriter.close();
        }
      }
    }
  }

  private ClusterWriter createClusterWriter(Writer writer, String[] dictionary) throws IOException {
    ClusterWriter result = null;

    switch (outputFormat) {
      case TEXT:
        result = new ClusterDumperWriter(writer, clusterIdToPoints, measure, numTopFeatures, dictionary, subString);
        break;
      case CSV:
        result = new CSVClusterWriter(writer, clusterIdToPoints, measure);
        break;
      case GRAPH_ML:
        result = new GraphMLClusterWriter(writer, clusterIdToPoints, measure, numTopFeatures, dictionary, subString);
        break;
    }
    return result;
  }

  private void init() {
    if (this.pointsDir != null) {
      Configuration conf = new Configuration();
      // read in the points
      clusterIdToPoints = readPoints(this.pointsDir, maxPointsPerCluster, conf);
    } else {
      clusterIdToPoints = Collections.emptyMap();
    }
  }

  public String getTermDictionary() {
    return termDictionary;
  }

  public void setTermDictionary(String termDictionary, String dictionaryType) {
    this.termDictionary = termDictionary;
    this.dictionaryFormat = dictionaryType;
  }

  public void setNumTopFeatures(int num) {
    this.numTopFeatures = num;
  }

  public void setMaxPointsPerCluster(long maxPointsPerCluster) {
    this.maxPointsPerCluster = maxPointsPerCluster;
  }

  public void setOutputFile(File file, OUTPUT_FORMAT format) {
	  outputFile = file;
	  outputFormat = format; 
  }

  private static Map<Integer, List<WeightedVectorWritable>> readPoints(Path pointsPathDir, long maxPointsPerCluster, Configuration conf) {
    Map<Integer, List<WeightedVectorWritable>> result = new TreeMap<Integer, List<WeightedVectorWritable>>();
    for (Pair<IntWritable, WeightedVectorWritable> record :
            new SequenceFileDirIterable<IntWritable, WeightedVectorWritable>(
                    pointsPathDir, PathType.LIST, PathFilters.logsCRCFilter(), conf)) {
      // value is the cluster id as an int, key is the name/id of the
      // vector, but that doesn't matter because we only care about printing
      // it
      //String clusterId = value.toString();
      int keyValue = record.getFirst().get();
      List<WeightedVectorWritable> pointList = result.get(keyValue);
      if (pointList == null) {
        pointList = Lists.newArrayList();
        result.put(keyValue, pointList);
      }
      if (pointList.size() < maxPointsPerCluster) {
        pointList.add(record.getSecond());
      }
    }
    return result;
  }
}
