package nl.knaw.dans.ersi.config;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class OaiPmhRepos {
	@Element
	private String baseUrl = "localhost";

	@Element
	private String metadataPrefix = "oai_dc";

	@Element(required = false)
	private String set;

	@ElementList(name = "selection-fields", entry = "field-name")
	private List<String> selectionFields;

	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public List<String> getSelectionFields() {
		return selectionFields;
	}

	public void setSelectionFields(List<String> selectionFields) {
		this.selectionFields = selectionFields;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

}