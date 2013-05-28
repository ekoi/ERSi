package nl.knaw.dans.ersy.textmining.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.DistanceMeasure;

public class InterClusterDistances {

	public static void main(String args[]) throws Exception {
		String inputFile
		= "/Users/akmi/SkyDrive/experiments/ersy_home_abr/clustering-result/vectors/clusters-output/clusters/clusters-8-final/part-r-00000";
		    Configuration conf = new Configuration();
		    Path path = new Path(inputFile);
		    
		    System.out.println("Input Path: " + path);
		    FileSystem fs = FileSystem.get(path.toUri(), conf);
		    List<ClusterWritable> clusters = new ArrayList<ClusterWritable>();
		    SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf); 
		    Writable key = (Writable) reader.getKeyClass().newInstance(); 
		    Writable value = (Writable) reader.getValueClass().newInstance();
		    while (reader.next(key, value)) {
		    	
		    	ClusterWritable cluster = (ClusterWritable) value;
		    	clusters.add(cluster);
		    	value = (Writable) reader.getValueClass().newInstance();
		    	
		    }
		    	
		    	DistanceMeasure measure = new CosineDistanceMeasure();
		    	double max = 0;
		    	double min = Double.MAX_VALUE;
		    	double sum = 0;
		    	int count = 0;
		    	for (int i = 0; i < clusters.size(); i++) {
		    		for (int j = i + 1; j < clusters.size(); j++) {
		    			double d = measure.distance(clusters.get(i).getValue().getCenter(),
		    			      clusters.get(j).getValue().getCenter());
		    			  min = Math.min(d, min);
		    			  max = Math.max(d, max);
		    			  sum += d;
		    			  count++;
		    		} 
		    	}
		    	System.out.println(max);
		    	System.out.println(min);
		    
	
	
	
	
	}
		    	

	

}
