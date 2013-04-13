package nl.knaw.dans.ersi.config;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class OaiPmhReposConfig {
	@Element(name="base-url")
	private String baseUrl = "https://easy.dans.knaw.nl/oai";

	@Element(name="metadata-prefix")
	private String metadataPrefix = "oai_dc";

	@Element(required = false)
	private String set;

	@ElementList(name = "selected-fields")
	private List<FieldConfig> selectedFields;

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


	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setSelectedFields(List<FieldConfig> selectedFields) {
		this.selectedFields = selectedFields;
	}

	public List<FieldConfig> getSelectedFields() {
		return selectedFields;
	}

}