package nl.knaw.dans.ersy.textmining.clustering;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KMeansClustering {
	private static Logger LOG = LoggerFactory.getLogger(KMeansClustering.class);

	public static void main(String args[]) throws Exception {
		LOG.debug("=========START KMeansClustering=========");
		String inputDir = System.getenv("ERSY_HOME") + "/output-vectors";
		long begin = System.currentTimeMillis();
		Configuration conf = new Configuration();
		String vectorsFolder = inputDir + "/tfidf-vectors";
		Path samples = new Path(vectorsFolder + "/part-r-00000");
		for (int i=1000; i<1500; i=i+10) {
			LOG.debug("i: " + i);
			long a = System.currentTimeMillis();
		Path output = new Path("/Volumes/Holdtank/clustering-results/k-means/"+i+"/");
		HadoopUtil.delete(conf, output);
		DistanceMeasure measure = new CosineDistanceMeasure();

		Path clustersIn = new Path(output, "random-seeds");
		RandomSeedGenerator.buildRandom(conf, samples, clustersIn, i, measure);
		KMeansDriver.run(samples, clustersIn, output, measure, 0.1, 10, true,
				0.0, true);
		
		long d = System.currentTimeMillis() - a;
	    String s= String.format("%d hours. %d min, %d sec", 
	    		TimeUnit.MILLISECONDS.toHours(d),
        	    TimeUnit.MILLISECONDS.toMinutes(d),
        	    TimeUnit.MILLISECONDS.toSeconds(d) - 
        	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(d))
        	);
	    
	    LOG.debug("Duration: " + s);
		}
		
		
//		List<List<Cluster>> Clusters = ClusterHelper.readClusters(conf, output);
//
//		for (Cluster cluster : Clusters.get(Clusters.size() - 1)) {
//			LOG.debug("Cluster id: " + cluster.getId() + " center: "
//					+ cluster.getCenter().asFormatString());
//		}
		
		
		long diff = System.currentTimeMillis() - begin;
	    String s= String.format("%d hours. %d min, %d sec", 
	    		TimeUnit.MILLISECONDS.toHours(diff),
        	    TimeUnit.MILLISECONDS.toMinutes(diff),
        	    TimeUnit.MILLISECONDS.toSeconds(diff) - 
        	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
        	);
	    
	    LOG.debug("Duration: " + s);
	    LOG.debug("=========END of KMeansClustering=========");
	}
}
