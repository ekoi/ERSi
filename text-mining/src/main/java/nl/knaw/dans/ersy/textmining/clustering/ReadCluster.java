package nl.knaw.dans.ersy.textmining.clustering;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadCluster {
	
	private static Logger LOG = LoggerFactory.getLogger(ReadCluster.class);

	public static void main(String args[]) throws Exception {
		String inputDir = System.getenv("ERSY_HOME") + "/clustering-result/vectors";

		Configuration conf = new Configuration();
		String vectorsFolder = inputDir + "/tfidf-vectors";
		Path samples = new Path(vectorsFolder + "/part-r-00000");

		Path output = new Path(System.getenv("ERSY_HOME") + "/clustering-result/vectors");
//		HadoopUtil.delete(conf, output);

	    Path canopyCentroids = new Path(output, "canopy-centroids");
	    Path clusterOutput = new Path(output, "clusters");
//		
//		CanopyDriver.run(conf, samples, canopyCentroids, new CosineDistanceMeasure(),
//				0.7, 0.5, false, 0, false);
//
//	    KMeansDriver.run(conf, new Path(vectorsFolder), new Path(canopyCentroids, "clusters-0-final"), clusterOutput,
//	    		new CosineDistanceMeasure(), 0.01, 20, true, 0.0, false);
		
	    List<List<Cluster>> Clusters = ClusterHelper.readClusters(conf, clusterOutput);
	    Clusters.size();
		for (Cluster cluster : Clusters.get(Clusters.size() - 1)) {
			LOG.debug("Cluster id: " + cluster.getId() + " center: "
					+ cluster.getCenter().asFormatString());
		}
		
//		   run ClusterDumper
//	    ClusterDumper clusterDumper = new ClusterDumper(new Path(output,
//	        "clusters-0-final"), new Path(output, "clusteredPoints"));
//	    clusterDumper.printClusters(null);
//	    LOG.debug(clusterDumper.getTermDictionary());
	    LOG.debug("=========END ReadCluster=========");
	}
}
