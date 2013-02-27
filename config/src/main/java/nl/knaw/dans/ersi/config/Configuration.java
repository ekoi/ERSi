package nl.knaw.dans.ersi.config;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Configuration {
	
	private String language = "nl";
	private String country = "nl";

	@Element(name="OAI-PMH") 
	private OaiPmhRepos oaiPmh;

	@Attribute
	private String generated;

	public Configuration() {
		init();
	}
	
	public Configuration (String language, String country) {
		this.language = language;
		this.country = country;
		init();
	}

	private void init() {
		DateTime dt = new DateTime();
		Locale locale = new Locale(language, country);
		DateTimeFormatter fmt = DateTimeFormat.longDateTime();
		generated = fmt.withLocale(locale).print(dt);
		
	}

	public String getGenerated() {
		return generated;
	}

	public OaiPmhRepos getOaiPmh() {
		return oaiPmh;
	}

	public void setOaiPmh(OaiPmhRepos oaiPmh) {
		this.oaiPmh = oaiPmh;
	}
}