package nl.knaw.dans.ersi.datapreprocessor.utils;

import java.util.List;
import java.util.Map.Entry;

import nl.knaw.dans.ersi.config.ConfigurationReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JustTryOut {

	private static Logger LOG = LoggerFactory.getLogger(JustTryOut.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
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
