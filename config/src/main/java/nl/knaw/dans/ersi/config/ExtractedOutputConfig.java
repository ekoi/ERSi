package nl.knaw.dans.ersi.config;

import java.util.Map;

import org.simpleframework.xml.ElementMap;

public class ExtractedOutputConfig {
	
	@ElementMap(entry="file", key="language", value="description", attribute=true, inline=true)
	private Map<String, OutputFileConfig> outputFileConfig;

	public Map<String, OutputFileConfig> getOutputFileConfig() {
		return outputFileConfig;
	}

	public void setOutputFileConfig(Map<String, OutputFileConfig> outputFileConfig) {
		this.outputFileConfig = outputFileConfig;
	}
	

}