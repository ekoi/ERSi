package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

public class Output {
	@Attribute
	private String language;

	@Element
	private String txtFileLocation;
	
	@Element(required=false)
	private String hdfsFileLocation;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTxtFileLocation() {
		return txtFileLocation;
	}

	public void setTxtFileLocation(String txtFileLocation) {
		this.txtFileLocation = txtFileLocation;
	}

	public String getHdfsFileLocation() {
		return hdfsFileLocation;
	}

	public void setHdfsFileLocation(String hdfsFileLocation) {
		this.hdfsFileLocation = hdfsFileLocation;
	}

	

}
