package nl.knaw.dans.ersi.datapreprocessor;

import nl.knaw.dans.ersi.datapreprocessor.utils.DataCleansingExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Hello world!
 *
 */
public class DataCleansingTryOut 
{
	private static Logger LOG = LoggerFactory.getLogger(DataCleansingTryOut.class);
    public static void main( String[] args )
    {
        try {
        	DataCleansingExecutor.main();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

    }
}
