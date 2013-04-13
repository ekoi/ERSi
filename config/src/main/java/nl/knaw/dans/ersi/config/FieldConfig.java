package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;

public class FieldConfig {
	
	@Element
	private String name;
	
	@Element(name = "detect-lang")
	private boolean detectLang = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDetectLang() {
		return detectLang;
	}

	public void setDetectLang(boolean detectLang) {
		this.detectLang = detectLang;
	}
}
