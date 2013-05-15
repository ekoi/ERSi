package nl.knaw.dans.ersi.datapreprocessor;

import nl.knaw.dans.ersi.datapreprocessor.utils.DataAbrCleansingExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Hello world!
 *
 */
public class ABRCleansingTryOut 
{
	private static Logger LOG = LoggerFactory.getLogger(ABRCleansingTryOut.class);
    public static void main( String[] args )
    {
        try {
        	DataAbrCleansingExecutor.main();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

    }
}
