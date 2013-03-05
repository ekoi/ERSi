package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OutputFileConfig {
	
	private static String USER_HOME = "user.home"; 
	private static Logger LOG = LoggerFactory.getLogger(OutputFileConfig.class);
	
	@Element(name="file-name")
	private String fileName;
	
	@Element(name="txt-file-path")
	private String txtFilePath;
	
	@Element(required=false, name="hdfs-file-path")
	private String hdfsFilePath;
	
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
		String path = System.getProperty(USER_HOME);
		if (hdfsFilePath.equals(path)) {
			LOG.error(path + " is not permitted. Please create a folder underneath of " + path + ".");
			this.hdfsFilePath = null;
		} else 
			this.hdfsFilePath = hdfsFilePath;
	}

}
