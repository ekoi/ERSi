package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class DataExtractionConfig {
	
	
	@Element(name="OAI-PMH") 
	private OaiPmhReposConfig oaiPmhReposConfig;
	
	@Element(name="local-source")
	private LocalSourceDataConfig localSource;
	
	@Element(name="extracted-output")
	private ExtractedOutputConfig extractedOutput;
	
	@Element
	private ReportConfig report;

	public ReportConfig getReport() {
		return report;
	}

	public void setReport(ReportConfig report) {
		this.report = report;
	}

	public OaiPmhReposConfig getOaiPmhReposConfig() {
		return oaiPmhReposConfig;
	}

	public void setOaiPmhReposConfig(OaiPmhReposConfig oaiPmhReposConfig) {
		this.oaiPmhReposConfig = oaiPmhReposConfig;
	}
	
	public LocalSourceDataConfig getLocalSource() {
		return localSource;
	}

	public void setLocalSource(LocalSourceDataConfig localSource) {
		this.localSource = localSource;
	}

	public ExtractedOutputConfig getExtractedOutput() {
		return extractedOutput;
	}

	public void setExtractedOutput(ExtractedOutputConfig extractedOutput) {
		this.extractedOutput = extractedOutput;
	}
}