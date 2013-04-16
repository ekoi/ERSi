package nl.knaw.dans.ersi.datapreprocessor.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.knaw.dans.ersi.config.ConfigurationReader;

public class Eko {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 ConfigurationReader cr = new ConfigurationReader();
		 CleansingResultReader crr = new
		 CleansingResultReader(cr.getDataCleansingConfig());
		 
		 System.out.println("Total numbers of words: " + crr.getTotalNumberOfWords());
		 System.out.println("Total numbers of different words: " + crr.getNumberOfDifferentWords());
			List<Entry<String, Integer>> l2 = crr.getWordAndItsNumberOccurences();
			for (int i=0; i<10; i++) {
				Entry<String, Integer> e = l2.get(i);
				String key = e.getKey();
				Integer val = e.getValue() ;
				System.out.println("key: " + key + "\t value: " + val );
			}
	}

	
}
