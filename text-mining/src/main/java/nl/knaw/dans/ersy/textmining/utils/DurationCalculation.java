package nl.knaw.dans.ersy.textmining.utils;

import java.util.concurrent.TimeUnit;

public class DurationCalculation {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long diff = 3593585;
		String s= String.format("%d min, %d sec", 
	        	    TimeUnit.MILLISECONDS.toMinutes(diff),
	        	    TimeUnit.MILLISECONDS.toSeconds(diff) - 
	        	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
	        	);
		 System.out.println("Difference is : " + s);

	}
}
