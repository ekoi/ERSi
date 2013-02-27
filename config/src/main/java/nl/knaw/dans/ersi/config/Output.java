package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Output {
	@Attribute
	private String language;

	@Element(name="file-name")
	private String fileName;
	
	@Element(name="txt-file-path")
	private String txtFilePath;
	
	@Element(required=false, name="hdfs-file-path")
	private String hdfsFilePath;
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTxtFilePath() {
		return txtFilePath;
	}

	public void setTxtFilePath(String txtFilePath) {
		this.txtFilePath = txtFilePath;
	}

	public String getHdfsFilePath() {
		return hdfsFilePath;
	}

	public void setHdfsFilePath(String hdfsFilePath) {
		this.hdfsFilePath = hdfsFilePath;
	}

}
