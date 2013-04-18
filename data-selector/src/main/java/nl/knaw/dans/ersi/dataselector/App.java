package nl.knaw.dans.ersi.dataselector;

import nl.knaw.dans.ersi.dataselector.util.DataExtractionExecutor;


/**
 * Hello world!
 *
 */
public class App 
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
		
		System.out.println( "Hello World! ");
        try {
			DataExtractionExecutor.main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
