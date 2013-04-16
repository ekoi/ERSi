package nl.knaw.dans.ersy.textmining.clustering.utils;

import java.io.IOException;
import java.util.HashMap;

import nl.knaw.dans.ersi.config.ConfigurationReader;

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
		ConfigurationReader cr = new ConfigurationReader();
		int i = 0;
		Configuration conf = new Configuration();
		FileSystem fs;
		SequenceFile.Reader read;
		try {
			fs = FileSystem.get(conf);
			read = new SequenceFile.Reader(fs,new Path(
							"/tmp/ersy/data-cleansing/oai-pmh/vectors/dictionary.file-0"),
					conf);
			IntWritable dicKey = new IntWritable();
			Text text = new Text();
			HashMap dictionaryMap = new HashMap();
			try {
				while (read.next(text, dicKey)) {
					i++;
					dictionaryMap.put(Integer.parseInt(dicKey.toString()),
							text.toString());
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			read.close();
			System.out.println(i);
			read = new SequenceFile.Reader(
					fs,
					new Path(
							"/tmp/ersy/data-cleansing/oai-pmh/vectors/tf-vectors/part-r-00000"),
					conf);
			Text key = new Text();
			VectorWritable value = new VectorWritable();
			SequentialAccessSparseVector vect;
			while (read.next(key, value)) {
				SequentialAccessSparseVector namedVector = (SequentialAccessSparseVector) value
						.get();
				// vect= (SequentialAccessSparseVector)namedVector.
				for (Element e : namedVector) {
					System.out.println("Token: " + dictionaryMap.get(e.index())
							+ ", TF-IDF weight: " + e.get());
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
