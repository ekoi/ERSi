/**
 * 
 */
package nl.knaw.dans.ersy.webui.secure.view;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * @author akmi
 * 
 */
public class TabDataCleaningPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1973574682018245001L;

	public TabDataCleaningPanel(String id, final String filePath) {
		super(id);
		
		ConfigurationReader configurationReader = new ConfigurationReader(filePath);
		
		String cleansingProcessStatus = ProcessStatus.giveStatus();
		
		final Label status = new Label("status", new Model<String>(cleansingProcessStatus));
        status.setOutputMarkupId(true);
        add(status);
        
        
		
		Form<Void> form = new Form<Void>("form");
        add(form);
        
//        final Label baseUrl = new Label("baseUrl", new Model<String>(opc.getBaseUrl()));
//        baseUrl.setOutputMarkupId(true);
//        form.add(baseUrl);
//        
//        final Label metadataPrefix = new Label("metadataPrefix", new Model<String>(opc.getMetadataPrefix()));
//        metadataPrefix.setOutputMarkupId(true);
//        form.add(metadataPrefix);
//        
//        final Label sets = new Label("sets", new Model<String>(opc.getSet()));
//        sets.setOutputMarkupId(true);
//        form.add(sets);
//        
//        final Label verb = new Label("verb", new Model<String>("http://easy.dans.knaw.nl/oai/?verb=ListRecords&metadataPrefix=oai_dc&set=D30000:D37000"));
//        verb.setOutputMarkupId(true);
//        form.add(verb);
//        
//     // add a button that can be used to submit the form via ajax
//        form.add(new AjaxButton("ajax-button", form)
//        {
//            @Override
//            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
//            {
//            	if (!ExtractionProcessStatus.isRunning()) {
//            		try {
//            			DataExtractionExecutor.main();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//            	}
//            	//target.add(baseUrl);
//            	PageParameters pageParameters = new PageParameters();
//    			pageParameters.add("selectedTab", 2);
//            	setResponsePage(new AdminPage(pageParameters));
//            }
//            
//            @Override
//            public boolean isEnabled() {
//            	return !ExtractionProcessStatus.isRunning();
//            }
//            
//          
//        });
        
	}

}
