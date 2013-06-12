package nl.knaw.dans.ersy.textmining.clustering;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.config.CanopyConfig;
import nl.knaw.dans.ersi.config.ClusterAlgorithmConfig;
import nl.knaw.dans.ersi.config.ClusteringConfig;
import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.Constants;
import nl.knaw.dans.ersi.config.KMeansConfig;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus.ProcessName;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KMeansWithCanopyClustering {
	private static Logger LOG = LoggerFactory.getLogger(KMeansWithCanopyClustering.class);

	private static ClusteringConfig clusteringConfig;
	public KMeansWithCanopyClustering() {
		ConfigurationReader confReader = new ConfigurationReader(Constants.ERSY_HOME);;
		clusteringConfig = confReader.getClusteringConfig();
	}
	
	  public void run() throws IOException, ClassNotFoundException, InterruptedException, InstantiationException, IllegalAccessException  {
		
		long begin = System.currentTimeMillis();
		ProcessStatus processStatus = new ProcessStatus(ProcessName.DATA_MINING, Constants.ERSY_HOME);
		boolean b = processStatus.writeCurrentStatus();
		LOG.info("WRITE current status: " + b);
		ClusterAlgorithmConfig cac = clusteringConfig.getClusterAlgorithmConfig();
		CanopyConfig cc = cac.getCanopyConfig();
		KMeansConfig kc = cac.getkMeansConfig();
		
		
		String inputDir = clusteringConfig.getInputVectorsPath();
		

		Configuration conf = new Configuration();
		String vectorsFolder = inputDir + "/tfidf-vectors";
		Path samples = new Path(vectorsFolder + "/part-r-00000");

		Path output = new Path(clusteringConfig.getOutputPath() + "/clusters-output");
		HadoopUtil.delete(conf, output);

	    Path canopyCentroids = new Path(output, "canopy-centroids");
	    Path clusterOutput = new Path(output, "clusters");
	    Class<DistanceMeasure> c =  (Class<DistanceMeasure>) Class.forName(cc.getDistanceMeasureClassName());
		CanopyDriver.run(conf, samples, canopyCentroids, c.newInstance(),
				cc.getDistanceMetricT1(), cc.getDistanceMetricT2(), false, cc.getClusterClassificationThreshold(), false);

		Class<DistanceMeasure> kdm =  (Class<DistanceMeasure>) Class.forName(kc.getDistanceMeasureClassName());
	    KMeansDriver.run(conf, new Path(vectorsFolder), new Path(canopyCentroids, "clusters-0-final"), clusterOutput,
	    		kdm.newInstance(), kc.getConvergenceDelta(), kc.getMaxIterations(), true, kc.getClusterClassificationThreshold(), false);
		
//	    List<List<Cluster>> Clusters = ClusterHelper.readClusters(conf, clusterOutput);
//		for (Cluster cluster : Clusters.get(Clusters.size() - 1)) {
//			LOG.debug("Cluster id: " + cluster.getId() + " center: "
//					+ cluster.getCenter().asFormatString());
//		}
//		
		  // run ClusterDumper
//	    ClusterDumper clusterDumper = new ClusterDumper(new Path(output,
//	        "clusters-0-final"), new Path(output, "clusteredPoints"));
//	    clusterDumper.printClusters(null);
//	    LOG.debug(clusterDumper.getTermDictionary());
	    long diff = System.currentTimeMillis() - begin;
	    String s= String.format("%d min, %d sec", 
        	    TimeUnit.MILLISECONDS.toMinutes(diff),
        	    TimeUnit.MILLISECONDS.toSeconds(diff) - 
        	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
        	);
	    boolean b2 = processStatus.writeLastStatus();
		boolean b3 = processStatus.writeDoneStatus();
	    LOG.info("Duration: " + s);
	    LOG.info(b2+ "=========END of Clustering========="+b3);
	}
}
