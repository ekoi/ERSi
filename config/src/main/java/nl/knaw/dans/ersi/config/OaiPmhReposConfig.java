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
	
	@Element(name="filter-class-name", required = false)
	private String filterClassName;
	
	@Element(name="words-list-name", required = false)
	private String wordsListName;

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

	public String getFilterClassName() {
		return filterClassName;
	}

	public void setFilterClassName(String filterClassName) {
		this.filterClassName = filterClassName;
	}

	public String getWordsListName() {
		return wordsListName;
	}

	public void setWordsListName(String wordsListName) {
		this.wordsListName = wordsListName;
	}

}