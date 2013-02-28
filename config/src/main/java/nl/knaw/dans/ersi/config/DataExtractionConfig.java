package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class DataExtractionConfig {
	
	
	@Element(name="OAI-PMH") 
	private OaiPmhReposConfig oaiPmhReposConfig;
	
	@Element(name="extracted-output")
	private ExtractedOutput extractedOutput;
	
	@Element
	private Report report;

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public OaiPmhReposConfig getOaiPmhReposConfig() {
		return oaiPmhReposConfig;
	}

	public void setOaiPmhReposConfig(OaiPmhReposConfig oaiPmhReposConfig) {
		this.oaiPmhReposConfig = oaiPmhReposConfig;
	}

	public ExtractedOutput getExtractedOutput() {
		return extractedOutput;
	}

	public void setExtractedOutput(ExtractedOutput extractedOutput) {
		this.extractedOutput = extractedOutput;
	}
}