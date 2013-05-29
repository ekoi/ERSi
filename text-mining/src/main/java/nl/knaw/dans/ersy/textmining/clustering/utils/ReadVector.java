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
import org.apache.mahout.math.NamedVector;
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
		ConfigurationReader cr = new ConfigurationReader();
		int i = 0;
		Configuration conf = new Configuration();
		FileSystem fs;
		SequenceFile.Reader read;
		try {
			fs = FileSystem.get(conf);
			read = new SequenceFile.Reader(fs,new Path(
							"/Users/akmi/SkyDrive/experiments/ersy_home_abr/data-cleansing/oai-pmh/vectors/dictionary.file-0"),
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
				LOG.error(e.getMessage());
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
			read.close();
			System.out.println(dictionaryMap.get(12));
			System.out.println(dictionaryMap.get(48));
			System.out.println(dictionaryMap.get(143));
			System.out.println(dictionaryMap.get(149));
			System.out.println(dictionaryMap.get(228));
			System.out.println(dictionaryMap.get(369));
			LOG.debug("i: " + i);
			read = new SequenceFile.Reader(
					fs,
					new Path(
							"/Users/akmi/SkyDrive/experiments/ersy_home_abr/data-cleansing/oai-pmh/vectors/tfidf-vectors/part-r-00000"),
					conf);
			Text key = new Text();
			VectorWritable value = new VectorWritable();
			SequentialAccessSparseVector vect;
			while (read.next(key, value)) {
				NamedVector namedVector = (NamedVector) value
						.get();
				
				System.out.println(namedVector.getName() + " \t" + namedVector.toString());
				// vect= (SequentialAccessSparseVector)namedVector.
				for (Element e : namedVector) {
					if (e.get() > 0.0)
					LOG.debug("Token: " + dictionaryMap.get(e.index())
							+ ", TF-IDF weight: " + e.get());
				}
			}
			read.close();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		LOG.debug("===END ReadVector===");

	}

}
