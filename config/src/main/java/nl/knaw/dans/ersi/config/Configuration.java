package nl.knaw.dans.ersi.config;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root
public class Configuration {
	
	private String language = "nl";
	private String country = "nl";
    
	@Attribute
	private String generated; 
	
	@Element(name="data-extraction") 
	private DataExtractionConfig dataExtractionConfig;
	
	@Element(name="data-cleansing")
	private DataCleansingConfig dataCleansingConfig;
	
	@Element(name="clustering")
	private ClusteringConfig clusteringConfig;

	
	public Configuration() {
	      this("nl", "nl");
	   }  
	
	public Configuration (String language, String country) {
		this.language = language;
		this.country = country;
		generateModificationTimeNow();
	}
	
	public void generateModificationTimeNow() {
		DateTime dt = new DateTime();
		Locale locale = new Locale(language, country);
		DateTimeFormatter fmt = DateTimeFormat.longDateTime();
		generated = fmt.withLocale(locale).print(dt);
		
	}

	public String getGenerated() {
		return generated;
	}

	public DataExtractionConfig getDataExtractionConfig() {
		return dataExtractionConfig;
	}

	public void setDataExtractionConfig(DataExtractionConfig dataExtractionConfig) {
		this.dataExtractionConfig = dataExtractionConfig;
	}

	public DataCleansingConfig getDataCleansingConfig() {
		return dataCleansingConfig;
	}

	public void setDataCleansingConfig(DataCleansingConfig dataCleansingConfig) {
		this.dataCleansingConfig = dataCleansingConfig;
	}

	
	public ClusteringConfig getClusteringConfig() {
		return clusteringConfig;
	}

	public void setClusteringConfig(ClusteringConfig clusteringConfig) {
		this.clusteringConfig = clusteringConfig;
	}

	public static Configuration Load(String xml) {
		Serializer serializer = new Persister();
		try {
			return serializer.read(Configuration.class, xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}