package nl.knaw.dans.ersy.textmining.utils;

import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersy.textmining.clustering.ReadVector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DurationCalculation {
	private static Logger LOG = LoggerFactory.getLogger(DurationCalculation.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long diff = 3593585;
		String s= String.format("%d min, %d sec", 
	        	    TimeUnit.MILLISECONDS.toMinutes(diff),
	        	    TimeUnit.MILLISECONDS.toSeconds(diff) - 
	        	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
	        	);
		 LOG.debug("Difference is : " + s);

	}
}
