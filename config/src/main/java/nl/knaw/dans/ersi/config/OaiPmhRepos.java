package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;

public class OaiPmhRepos {
	@Element
	private String host = "localhost";
	
	@Element	
	private int port = 80;
	
	@Element	
	private String metadataPrefix = "oai_dc";
	
	@Element(required=false)	
	private String set;


	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}
}