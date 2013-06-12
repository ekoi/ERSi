/**
 * 
 */
package nl.knaw.dans.ersi.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

/**
 * @author akmi
 *
 */
public class ConfigurationReader implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7900485746738833517L;
	private String ersyHome;
	private String confFileLocation;
	private DataExtractionConfig  dataExtractionConfig;
	private DataCleansingConfig dataCleansingConfig;
	private ClusteringConfig clusteringConfig;
	private String xmlAsString;
	private String lastModificationTimeAsString;
	
	/**
	 * 
	 */
	
	public ConfigurationReader(){
		this(Constants.ERSY_HOME + "/conf/configuration.xml");
	}

	public ConfigurationReader(String ersyHome) {
		this.ersyHome = ersyHome;
		confFileLocation = ersyHome + "/conf/configuration.xml";
		init();
	}
	private void init(){
		Serializer serializer = new Persister(new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>"));
		File source = new File(confFileLocation);
		try {
			Configuration configuration = serializer.read(Configuration.class, source);
			setLastModificationTimeAsString(configuration.getGenerated());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.write(configuration, baos);
			xmlAsString = baos.toString();
			dataExtractionConfig = configuration.getDataExtractionConfig();
			dataCleansingConfig = configuration.getDataCleansingConfig();
			clusteringConfig = configuration.getClusteringConfig();
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
	
	/**
	 * @return the clusteringConfig
	 */
	public ClusteringConfig getClusteringConfig() {
		return clusteringConfig;
	}
	public String toString() {
		return xmlAsString;
	}
	public String getLastModificationTimeAsString() {
		return lastModificationTimeAsString;
	}
	public void setLastModificationTimeAsString(String lastModificationTimeAsString) {
		this.lastModificationTimeAsString = lastModificationTimeAsString;
	}

	public String getErsyHome() {
		return ersyHome;
	}

	public void setErsyHome(String ersyHome) {
		this.ersyHome = ersyHome;
	}
}