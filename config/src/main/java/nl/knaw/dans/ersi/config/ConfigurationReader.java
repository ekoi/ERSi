/**
 * 
 */
package nl.knaw.dans.ersi.config;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * @author akmi
 *
 */
public class ConfigurationReader {
	
	private String confFileLocation;
	private DataExtractionConfig  dataExtractionConfig;
	private DataCleansingConfig dataCleansingConfig;
	private String xmlAsString;
	/**
	 * 
	 */
	
	public ConfigurationReader(String confFileLocation) {
		this.confFileLocation = confFileLocation;
		init();
	}
	private void init(){
		Serializer serializer = new Persister();
		File source = new File(confFileLocation);
		try {
			Configuration configuration = serializer.read(Configuration.class, source);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.write(configuration, baos);
			xmlAsString = baos.toString();
			dataExtractionConfig = configuration.getDataExtractionConfig();
			dataCleansingConfig = configuration.getDataCleansingConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getConfFileLocation() {
		return confFileLocation;
	}
	public DataExtractionConfig getDataExtractionConfig() {
		return dataExtractionConfig;
	}
	public DataCleansingConfig getDataCleansingConfig() {
		return dataCleansingConfig;
	}
	
	public String toString() {
		return xmlAsString;
	}
}
