package nl.knaw.dans.ersy.textmining.clustering;

import java.io.IOException;

import nl.knaw.dans.ersy.textmining.clustering.utils.DataClusteringExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class DataClusteringExecutorTryOut 
{
	private static Logger LOG = LoggerFactory.getLogger(DataClusteringExecutorTryOut.class);
    public static void main( String[] args ) throws IOException
    {
//    	Configuration conf = new Configuration();
//        FileSystem fs = FileSystem.get(conf);
//        String outputDir = "/Users/akmi/zzz-test/clusters-output/";
//    	  Path vectorsFolder = new Path(outputDir, "tfidf-vectors");
//    	 Path clusterOutput = new Path(outputDir , "clusters");
//        LOG.debug( "Hello World!" );
//        SequenceFile.Reader reader = new SequenceFile.Reader(fs,
//                new Path(clusterOutput + "/" + Cluster.CLUSTERED_POINTS_DIR + "/part-m-00000"), conf);
//            
//            IntWritable key = new IntWritable();
//            WeightedVectorWritable value = new WeightedVectorWritable();
//            while (reader.next(key, value)) {
//            	NamedVector vector = (NamedVector) value.getVector();
//                String vectorName = vector.getName();
//                LOG.debug(vectorName + ", " + key.toString());
//               LOG.debug(key.toString() + " belongs to cluster "
//               + value.toString());
//            }
//            reader.close();
            
            
         try {
         	DataClusteringExecutor.main();
 		} catch (Exception e) {
 			LOG.error(e.getMessage());
 		}
    }
}
