/**
 * 
 */
package nl.knaw.dans.ersi.dataselector;

import java.io.IOException;

import se.kb.oai.OAIException;

import com.cybozu.labs.langdetect.LangDetectException;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.DataExtractionConfig;
import nl.knaw.dans.ersi.config.ReportConfig;

/**
 * @author akmi
 *
 */
public abstract class SimpleExtractor {

	/**
	 * 
	 */
	private ConfigurationReader confReader;
	
	public SimpleExtractor(ConfigurationReader confReader) {
		this.confReader = confReader;
	}
	
	protected ConfigurationReader getConfReader() {
		return confReader;
	}
	
	/**
	 * @return the dataExtractionConfig
	 */
	protected DataExtractionConfig getDataExtractionConfig() {
		return confReader.getDataExtractionConfig();
	}
	
	protected ReportConfig getReportConfig() {
		return getDataExtractionConfig().getReport();
	}

	public void extract() throws OAIException, IOException, LangDetectException {}
}
