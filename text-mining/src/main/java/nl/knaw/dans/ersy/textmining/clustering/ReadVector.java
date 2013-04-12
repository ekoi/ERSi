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


public class ReadVector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("BEGIN");
		Configuration conf = new Configuration();
		 FileSystem fs;
		 SequenceFile.Reader read;
		 try {
		  fs = FileSystem.get(conf);
		  read = new SequenceFile.Reader(fs, new Path("/Users/akmi/tmp/1resuptiontoken/outputdata-from-easy/dictionary.file-0"), conf);
		  IntWritable dicKey = new IntWritable();
		  Text text = new Text();
		  HashMap dictionaryMap = new HashMap();
		  try {
		      while (read.next(text, dicKey)) {
		         dictionaryMap.put(Integer.parseInt(dicKey.toString()), text.toString());
		      }
		   } catch (NumberFormatException e) {
		       e.printStackTrace();
		   } catch (IOException e) {
		       e.printStackTrace();
		   }
		   read.close();
		         
		   read = new SequenceFile.Reader(fs, new Path("/Users/akmi/tmp/1resuptiontoken/outputdata-from-easy/tf-vectors/part-r-00000"), conf);
		   Text key = new Text();
		   VectorWritable value = new VectorWritable();
		   SequentialAccessSparseVector vect;
		   while (read.next(key, value)) {
			   SequentialAccessSparseVector namedVector = (SequentialAccessSparseVector)value.get();
		        //vect= (SequentialAccessSparseVector)namedVector.
		        for( Element  e : namedVector ){
		           System.out.println("Token: "+dictionaryMap.get(e.index())+", TF-IDF weight: "+e.get()) ;
		          }
		         }
		         read.close();        
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		  e.printStackTrace();
		 }
		System.out.println("===END===");

	}

}
