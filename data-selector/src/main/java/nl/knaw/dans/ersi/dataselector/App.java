package nl.knaw.dans.ersi.dataselector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.DataExtractionConfig;
import se.kb.oai.OAIException;

import com.cybozu.labs.langdetect.LangDetectException;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) {
		ConfigurationReader configurationReader = new ConfigurationReader(
				"/Users/akmi/Dropbox/THESIS/Sources/Eclipse/workspace/ERSi/config/src/resources/configuration.xml");
		SimpleOaiPmhExtractor seme = new SimpleOaiPmhExtractor(configurationReader.getDataExtractionConfig());

		try {
			seme.extract();
		} catch (OAIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LangDetectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
