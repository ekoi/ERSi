package nl.knaw.dans.ersi.datapreprocessor.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.DataCleansingConfig;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleansingResultReader {
	
	private static Logger LOG = LoggerFactory.getLogger(CleansingResultReader.class);
	private final DataCleansingConfig dcc;
	private Configuration conf = new Configuration();
	private FileSystem fs;
	private SequenceFile.Reader read;
	private List<Entry<String, Integer>> wordAndItsNumberOccurences;
	private int numberOfDifferentWords;
	private int totalNumberOfWords;
	
	public CleansingResultReader(DataCleansingConfig dcc) {
		this.dcc = dcc;
		init();
	}

	private void init(){
		int count = 0;
		Map<String, Integer> wordOccurences = new HashMap<String, Integer>(); 
		try {
			fs = FileSystem.get(conf);
			read = new SequenceFile.Reader(fs, new Path(dcc.getOutputDirectory() + "/wordcount/part-r-00000"), conf);
			 Writable key = (Writable) ReflectionUtils.newInstance(read.getKeyClass(), conf);
			 Writable value = (Writable) ReflectionUtils.newInstance(read.getValueClass(), conf);
			 while (read.next(key, value)) {
//                 LOG.debug("key: " + key + " value: " + value);
                 count = Integer.parseInt(value.toString());
                 totalNumberOfWords = totalNumberOfWords + count;
                 wordOccurences.put(key.toString(), new Integer(count));
         }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (read != null)
				try {
					read.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		wordAndItsNumberOccurences = entriesSortedByValues(wordOccurences);
		numberOfDifferentWords = wordAndItsNumberOccurences.size();
	}
	/**
	 * @param args
	 */
	public int getNumberOfDifferentWords() {
		return numberOfDifferentWords;
	}
	
	
	public void tfIdfWeight(){
		HashMap dictionaryMap = new HashMap();
		
		try {
			read = new SequenceFile.Reader(fs, new Path(dcc.getOutputDirectory() + "/tf-vectors/part-r-00000"), conf);
			Text key = new Text();
			VectorWritable value = new VectorWritable();
			SequentialAccessSparseVector vect;
			while (read.next(key, value)) {
				SequentialAccessSparseVector namedVector = (SequentialAccessSparseVector) value
						.get();
				// vect= (SequentialAccessSparseVector)namedVector.
				for (Element e : namedVector) {
					LOG.debug("Token: " + dictionaryMap.get(e.index())
							+ ", TF-IDF weight: " + e.get());
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (read != null)
				try {
					read.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	static <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(
			Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(
				map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});

		return sortedEntries;
	}

	public int getTotalNumberOfWords() {
		return totalNumberOfWords;
	}

	/**
	 * @return the wordAndItsNumberOccurences
	 */
	public List<Entry<String, Integer>> getWordAndItsNumberOccurences() {
		return wordAndItsNumberOccurences;
	}
	
	public static void main(String[] args) {
		LOG.debug( "Hello World! " + System.getenv("ERSY_HOME"));
		 ConfigurationReader cr = new ConfigurationReader();
		 CleansingResultReader crr = new
		 CleansingResultReader(cr.getDataCleansingConfig());
		 
		 LOG.debug("Total numbers of words: " + crr.getTotalNumberOfWords());
		 LOG.debug("Total numbers of different words: " + crr.getNumberOfDifferentWords());
			List<Entry<String, Integer>> l2 = crr.getWordAndItsNumberOccurences();
			for (int i=0; i<10; i++) {
				Entry<String, Integer> e = l2.get(i);
				String key = e.getKey();
				Integer val = e.getValue() ;
				LOG.debug("key: " + key + "\t value: " + val );
			}
	}

}
