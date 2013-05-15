package nl.knaw.dans.ersi.dataselector;

import nl.knaw.dans.ersi.dataselector.util.DataExtractionExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Hello world!
 *
 */
public class DataExtractionTryOut 
{
	private static Logger LOG = LoggerFactory.getLogger(DataExtractionTryOut.class);

	public static void main(String[] args) {
//		ConfigurationReader configurationReader = new ConfigurationReader();
//		SimpleOaiPmhExtractor seme = new SimpleOaiPmhExtractor(configurationReader.getDataExtractionConfig());
//		
////		SimpleLocalSourceExtractor seme = new SimpleLocalSourceExtractor(configurationReader.getDataExtractionConfig());
//
//		try {
//			seme.extract();
//		} catch (OAIException e) {
//			// TODO Auto-generated catch block
//			LOG.error(e.getMessage());
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			LOG.error(e.getMessage());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			LOG.error(e.getMessage());
//		} catch (LangDetectException e) {
//			// TODO Auto-generated catch block
//			LOG.error(e.getMessage());
//		}
		
        try {
			DataExtractionExecutor.main();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
}
