package nl.knaw.dans.ersy.textmining.clustering;

import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.distance.CosineDistanceMeasure;

public class KMeansWithCanopyClustering {

	/*
	 
	 <clustering>
	 	<cluster-algorithm>
	 	 	<canopy>
	 	 		
	 	 	</canopy>
	 	 	<kmeans>
	 	 	
	 	 	</kmeans>
	 	</cluster-algorithm>
	 	<input-vectors-path>
	 	</input-vectors-path>
	 	<output-clustering-path>
	 	</output-clustering-path>
      <output-directory>/tmp/ersy/data-cleansing/oai-pmh/vectors</output-directory>
   </data-cleansing>
	 
	 
	 */
	
	public static void main(String args[]) throws Exception {
		
		long begin = System.currentTimeMillis();
		
		String inputDir = "/Volumes/Holdtank/Experiments/ERSi/data-cleansing-result/oai-pmh/output-vector";

		Configuration conf = new Configuration();
		String vectorsFolder = inputDir + "/tfidf-vectors";
		Path samples = new Path(vectorsFolder + "/part-r-00000");

		Path output = new Path("/Volumes/Holdtank/Experiments/ERSi/clusters-output");
		HadoopUtil.delete(conf, output);

	    Path canopyCentroids = new Path(output, "canopy-centroids");
	    Path clusterOutput = new Path(output, "clusters");
		
		CanopyDriver.run(conf, samples, canopyCentroids, new CosineDistanceMeasure(),
				0.7, 0.5, false, 0, false);

	    KMeansDriver.run(conf, new Path(vectorsFolder), new Path(canopyCentroids, "clusters-0-final"), clusterOutput,
	    		new CosineDistanceMeasure(), 0.01, 10, true, 0.0, false);
		
//	    List<List<Cluster>> Clusters = ClusterHelper.readClusters(conf, clusterOutput);
//		for (Cluster cluster : Clusters.get(Clusters.size() - 1)) {
//			System.out.println("Cluster id: " + cluster.getId() + " center: "
//					+ cluster.getCenter().asFormatString());
//		}
//		
		  // run ClusterDumper
//	    ClusterDumper clusterDumper = new ClusterDumper(new Path(output,
//	        "clusters-0-final"), new Path(output, "clusteredPoints"));
//	    clusterDumper.printClusters(null);
//	    System.out.println(clusterDumper.getTermDictionary());
	    long diff = System.currentTimeMillis() - begin;
	    String s= String.format("%d min, %d sec", 
        	    TimeUnit.MILLISECONDS.toMinutes(diff),
        	    TimeUnit.MILLISECONDS.toSeconds(diff) - 
        	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
        	);
	    
	    System.out.println("Duration: " + s);
	    System.out.println("=========END=========");
	}
}
