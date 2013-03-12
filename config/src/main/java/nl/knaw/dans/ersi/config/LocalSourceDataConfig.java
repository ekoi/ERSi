package nl.knaw.dans.ersi.config;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class LocalSourceDataConfig {
	@Element(name="file-name")
	private String fileName;

	@Element(name="file-path")
	private String filePath;

	@Element(name="file-type")
	private String fileType;

	@ElementList(name = "selected-fields")
	private List<Field> selectedFields;

	public void setSelectedFields(List<Field> selectedFields) {
		this.selectedFields = selectedFields;
	}

	public List<Field> getSelectedFields() {
		return selectedFields;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}