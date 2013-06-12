package nl.knaw.dans.ersi.datapreprocessor;

import nl.knaw.dans.ersi.datapreprocessor.utils.AbrDataCleansingExecutor;

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
        	AbrDataCleansingExecutor.main();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

    }
}
