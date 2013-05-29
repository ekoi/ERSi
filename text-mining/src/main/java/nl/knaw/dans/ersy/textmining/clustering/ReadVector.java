package nl.knaw.dans.ersy.textmining.clustering;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReadVector {
	private static Logger LOG = LoggerFactory.getLogger(ReadVector.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOG.debug("BEGIN ReadVector");
		Configuration conf = new Configuration();
		 FileSystem fs;
		 SequenceFile.Reader read;
		 try {
		  fs = FileSystem.get(conf);
		  read = new SequenceFile.Reader(fs, new Path(System.getenv("ERSY_HOME") + "/data-cleansing/oai-pmh/vectors/dictionary.file-0"), conf);
		  IntWritable dicKey = new IntWritable();
		  Text text = new Text();
		  HashMap dictionaryMap = new HashMap();
		  try {
		      while (read.next(text, dicKey)) {
		         dictionaryMap.put(Integer.parseInt(dicKey.toString()), text.toString());
		      }
		   } catch (NumberFormatException e) {
		       LOG.error(e.getMessage());
		   } catch (IOException e) {
		       LOG.error(e.getMessage());
		   }
		   read.close();
		         
		   read = new SequenceFile.Reader(fs, new Path(System.getenv("ERSY_HOME") + "/data-cleansing/oai-pmh/vectors/tf-vectors/part-r-00000"), conf);
		   Text key = new Text();
		   VectorWritable value = new VectorWritable();
		   SequentialAccessSparseVector vect;
		   while (read.next(key, value)) {
			   SequentialAccessSparseVector namedVector = (SequentialAccessSparseVector)value.get();
		        //vect= (SequentialAccessSparseVector)namedVector.
		        for( Element  e : namedVector ){
		           LOG.debug("Token: "+dictionaryMap.get(e.index())+", TF-IDF weight: "+e.get()) ;
		          }
		         }
		         read.close();        
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		  LOG.error(e.getMessage());
		 }
		LOG.debug("===END ReadVector===");

	}

}
