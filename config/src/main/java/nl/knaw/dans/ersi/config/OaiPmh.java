package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class OaiPmh {

	@Element
	private String host = "localhost";
	
	@Element	
	private int port = 80;

//	@Element
//	private Security security;

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

//	public Security getSecurity() {
//		return security;
//	}
}