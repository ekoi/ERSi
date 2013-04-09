/**
 * 
 */
package nl.knaw.dans.ersy.webui.secure.view;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.OaiPmhReposConfig;
import nl.knaw.dans.ersi.dataselector.SimpleOaiPmhExtractor;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * @author akmi
 * 
 */
public class TabDataExtractionPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1973574682018245001L;

	public TabDataExtractionPanel(String id, final String filePath) {
		super(id);
		
		ConfigurationReader configurationReader = new ConfigurationReader(filePath);
		OaiPmhReposConfig opc = configurationReader.getDataExtractionConfig().getOaiPmhReposConfig();
		
		SimpleOaiPmhExtractor seme = new SimpleOaiPmhExtractor(configurationReader.getDataExtractionConfig());
		
		final Label status = new Label("status", new Model<String>("Not Running"));
        status.setOutputMarkupId(true);
        add(status);
        
		
		Form<Void> form = new Form<Void>("form");
        add(form);
        
        final Label baseUrl = new Label("baseUrl", new Model<String>(opc.getBaseUrl()));
        baseUrl.setOutputMarkupId(true);
        form.add(baseUrl);
        
        final Label metadataPrefix = new Label("metadataPrefix", new Model<String>(opc.getMetadataPrefix()));
        metadataPrefix.setOutputMarkupId(true);
        form.add(metadataPrefix);
        
        final Label sets = new Label("sets", new Model<String>(opc.getSet()));
        sets.setOutputMarkupId(true);
        form.add(sets);
        
        final Label verb = new Label("verb", new Model<String>("http://easy.dans.knaw.nl/oai/?verb=ListRecords&metadataPrefix=oai_dc&set=D30000:D37000"));
        verb.setOutputMarkupId(true);
        form.add(verb);
        
     // add a button that can be used to submit the form via ajax
        form.add(new AjaxButton("ajax-button", form)
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
            	
            	target.add(baseUrl);
            	
            }

        });
        
	}

}
