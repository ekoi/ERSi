package nl.knaw.dans.ersy.webui.secure.view;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.OaiPmhReposConfig;
import nl.knaw.dans.ersi.dataselector.util.DataExtractionExecutor;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus.ProcessName;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author akmi
 * 
 */
public class TabDataExtractionPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1973574682018245001L;

	public TabDataExtractionPanel(String id, final String ersyHome) {
		super(id);
		
		final ConfigurationReader configurationReader = new ConfigurationReader(ersyHome);
		OaiPmhReposConfig opc = configurationReader.getDataExtractionConfig().getOaiPmhReposConfig();
		final String extractionClassName = opc.getFilterClassName();
		final ProcessStatus ps = new ProcessStatus(ProcessName.DATA_EXTRACTION, ersyHome);
		boolean executeIsAllowed = !ps.isRunning();
		if (executeIsAllowed) {
			final ProcessStatus psde = new ProcessStatus(ProcessName.DATA_CLEANING, ersyHome);
			executeIsAllowed = !psde.isRunning();
		}
		final boolean dataExtractionIsAllowed = executeIsAllowed;
		
		add (new Label("currentStatus", new Model<String>(ps.giveCurrentStatus())));
		add (new Label("lastStatus", new Model<String>(ps.giveTimeLastProcess())));
		add(new Label("baseUrl", new Model<String>(opc.getBaseUrl())));
		add(new Label("metadataPrefix", new Model<String>(opc.getMetadataPrefix())));
		add(new Label("sets", new Model<String>(opc.getSet())));
		add(new Label("verb", new Model<String>("http://easy.dans.knaw.nl/oai/?verb=ListRecords&metadataPrefix=oai_dc&set=D30000:D37000")));
		
		
		Form<Void> form = new Form<Void>("form");
        add(form);
        
     // add a button that can be used to submit the form via ajax
        form.add(new AjaxButton("ajax-button", form)
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
            	if (!ps.isRunning()) {
            		try {
            			DataExtractionExecutor.go(ersyHome, extractionClassName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	//target.add(baseUrl);
            	PageParameters pageParameters = new PageParameters();
    			pageParameters.add("selectedTab", 2);
            	setResponsePage(new AdminPage(pageParameters));
            }
            
            @Override
            public boolean isEnabled() {
            	return dataExtractionIsAllowed;
            }
            
          
        });
        Label executeNotAllowedLable = new Label("executeNotAllowed", new Model<String>("Please wait until data cleaning process finish."));
		executeNotAllowedLable.setVisible(executeIsAllowed && !dataExtractionIsAllowed);
		form.add (executeNotAllowedLable);
	}

}
