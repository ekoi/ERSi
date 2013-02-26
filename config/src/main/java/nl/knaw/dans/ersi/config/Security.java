package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Security {

	@Attribute
	private boolean ssl = true;

	@Element
	private String keyStore = "keystore";

	public boolean isSSL() {
		return ssl;
	}

	public String getKeyStore() {
		return keyStore;
	}
}