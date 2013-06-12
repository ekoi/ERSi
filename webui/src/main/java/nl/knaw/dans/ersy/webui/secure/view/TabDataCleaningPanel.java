package nl.knaw.dans.ersy.webui.secure.view;

import java.util.List;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.DataCleansingConfig;
import nl.knaw.dans.ersi.datapreprocessor.utils.StandardAbrDataCleansingExecutor;
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
public class TabDataCleaningPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1973574682018245001L;

	public TabDataCleaningPanel(String id, String ersyHome) {
		super(id);
		
		final ProcessStatus ps = new ProcessStatus(ProcessName.DATA_CLEANING, ersyHome);
		boolean executeIsAllowed = !ps.isRunning();
		if (executeIsAllowed) {
			final ProcessStatus psde = new ProcessStatus(ProcessName.DATA_EXTRACTION, ersyHome);
			executeIsAllowed = !psde.isRunning();
		}
		final boolean dataCleaningIsAllowed = executeIsAllowed;
		
		add (new Label("currentStatus", new Model<String>(ps.giveCurrentStatus())));
		add (new Label("lastStatus", new Model<String>(ps.giveTimeLastProcess())));
		
		ConfigurationReader confReader = new ConfigurationReader(ersyHome);
		DataCleansingConfig dcc = confReader.getDataCleansingConfig();
		
		add (new Label("minWordLength", new Model<Integer>(dcc.getSimpleDimensionReduction().getMinWordLength())));
		List<String> wordsToSkip = dcc.getSimpleDimensionReduction().getSkipWord();
		add (new Label("skipWord", new Model<String>(wordsToSkip.toString())));
		add (new Label("minDf", new Model<Integer>(dcc.getMinDf())));
		add (new Label("minSupport", new Model<Integer>(dcc.getMinSupport())));
		add (new Label("maxDfPrecent", new Model<Integer>(dcc.getMaxDFPercent())));
		add (new Label("maxNGramSize", new Model<Integer>(dcc.getMaxNGramSize())));
		add (new Label("minLLRValue", new Model<Float>(dcc.getMinLLRValue())));
		add (new Label("reduceTasks", new Model<Integer>(dcc.getReduceTasks())));
		add (new Label("chuckSize", new Model<Integer>(dcc.getChunkSize())));
		add (new Label("norm", new Model<Integer>(dcc.getNorm())));
//        
		
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
            			StandardAbrDataCleansingExecutor.main();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	//target.add(baseUrl);
            	PageParameters pageParameters = new PageParameters();
    			pageParameters.add("selectedTab", 3);
            	setResponsePage(new AdminPage(pageParameters));
            }
            
            @Override
            public boolean isEnabled() {
            	return dataCleaningIsAllowed;
            }
        });
        
        Label executeNotAllowedLable = new Label("executeNotAllowed", new Model<String>("Please wait until data extraction process finish."));
		executeNotAllowedLable.setVisible(!dataCleaningIsAllowed);
		form.add (executeNotAllowedLable);
	}

}
