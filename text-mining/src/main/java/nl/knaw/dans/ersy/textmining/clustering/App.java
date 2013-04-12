package nl.knaw.dans.ersy.textmining.clustering;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.math.NamedVector;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        String outputDir = "/Users/akmi/zzz-test/clusters-output/";
    	  Path vectorsFolder = new Path(outputDir, "tfidf-vectors");
    	 Path clusterOutput = new Path(outputDir , "clusters");
        System.out.println( "Hello World!" );
        SequenceFile.Reader reader = new SequenceFile.Reader(fs,
                new Path(clusterOutput + "/" + Cluster.CLUSTERED_POINTS_DIR + "/part-m-00000"), conf);
            
            IntWritable key = new IntWritable();
            WeightedVectorWritable value = new WeightedVectorWritable();
            while (reader.next(key, value)) {
            	NamedVector vector = (NamedVector) value.getVector();
                String vectorName = vector.getName();
                System.out.println(vectorName + ", " + key.toString());
               System.out.println(key.toString() + " belongs to cluster "
               + value.toString());
            }
            reader.close();
            
            
    }
}
