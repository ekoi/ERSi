package nl.knaw.dans.ersi.dataselector;

import nl.knaw.dans.ersi.dataselector.util.DataABRExtractionExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Hello world!
 *
 */
public class DataExtractionAbrTryOut 
{
	private static Logger LOG = LoggerFactory.getLogger(DataExtractionAbrTryOut.class);
	public static void main(String[] args) {
        try {
        	DataABRExtractionExecutor.main();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
}
