/**
 * 
 */
package nl.knaw.dans.ersy.webui.secure.view;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.DataExtractionConfig;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * @author akmi
 * 
 */
public class TabConfigurationPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1973574682018245001L;

	public TabConfigurationPanel(String id) {
		super(id);
		
		ConfigurationReader cr = new ConfigurationReader("src/main/resources/configuration.xml");
		
		DataExtractionConfig dec = cr.getDataExtractionConfig();		
		Form<Void> form = new Form<Void>("form");
        add(form);
        final TextField<String> baseUrlField = new TextField<String>("baseUrlField", new Model<String>(dec.getOaiPmhReposConfig().getBaseUrl()));
        form.add(baseUrlField);
        final TextField<String> metadataPrefixField = new TextField<String>("metadataPrefixField", new Model<String>(dec.getOaiPmhReposConfig().getBaseUrl()));
        form.add(metadataPrefixField);
        final TextField<String> baseUrlField = new TextField<String>("baseUrlField", new Model<String>(dec.getOaiPmhReposConfig().getBaseUrl()));
        form.add(baseUrlField);
        final TextField<String> baseUrlField = new TextField<String>("baseUrlField", new Model<String>(dec.getOaiPmhReposConfig().getBaseUrl()));
        form.add(baseUrlField);
        final TextField<String> baseUrlField = new TextField<String>("baseUrlField", new Model<String>(dec.getOaiPmhReposConfig().getBaseUrl()));
        form.add(baseUrlField);
        
        
        //for check, just temporary: remove this line
        final Label selectedPid = new Label("selectedPid", new Model<String>(""));
        selectedPid.setOutputMarkupId(true);
        form.add(selectedPid);
        
     // add a button that can be used to submit the form via ajax
        form.add(new AjaxButton("ajax-button", form)
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
            	String baserUrl= baseUrlField.getDefaultModelObjectAsString();
            	String metadataPrefix= metadataPrefixField.getDefaultModelObjectAsString();
            	
            	selectedPid.setDefaultModelObject(baserUrl);
            	target.add(selectedPid);
            }

        });
        
	}

}
