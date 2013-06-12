package nl.knaw.dans.ersy.webui.secure.view;

import java.io.File;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.OaiPmhReposConfig;
import nl.knaw.dans.ersi.config.ReportConfig;
import nl.knaw.dans.ersy.webui.utis.ReadWriteTextFile;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * @author akmi
 * 
 */
public class TabStatisticsPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1973574682018245001L;

	public TabStatisticsPanel(String id, final String ersyHome) {
		super(id);
		ConfigurationReader cr = new ConfigurationReader(ersyHome);
		final ConfigurationReader configurationReader = new ConfigurationReader(ersyHome);
		ReportConfig rc = configurationReader.getDataExtractionConfig().getReport();
		File reportFile = new File(ersyHome + "/" + rc.getPath() + "/" + rc.getName());
		String report = "ERROR";
		if (reportFile.exists()) {
			report = ReadWriteTextFile.getContents(reportFile);
		}
		Label dataExtractionReport = new Label("dataExtractionReport", new Model<String>(report));
		dataExtractionReport.setEscapeModelStrings(false);
		add(dataExtractionReport);
	}
}
