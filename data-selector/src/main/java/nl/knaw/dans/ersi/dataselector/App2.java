package nl.knaw.dans.ersi.dataselector;

import nl.knaw.dans.ersi.dataselector.util.DataABRExtractionExecutor;


/**
 * Hello world!
 *
 */
public class App2 
{
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
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (LangDetectException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println( "Hello World! " + System.getenv("ERSY_HOME"));
        try {
        	DataABRExtractionExecutor.main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
