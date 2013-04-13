package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;

public class ReportConfig {

	@Element
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Element
	private String path;
	
}
