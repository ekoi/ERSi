/**
 * 
 */
package nl.knaw.dans.ersy.webui.secure.view;

import nl.knaw.dans.ersi.config.ConfigurationCreator;
import nl.knaw.dans.ersi.config.ConfigurationReader;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
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
		final String filePath = "src/main/resources/configuration.xml";
		final ConfigurationReader cr = new ConfigurationReader(filePath);
			
		Form<Void> form = new Form<Void>("form");
        add(form);
        final TextArea<String> confTextArea = new TextArea<String>("confTextArea", new Model<String>(cr.toString()));
        confTextArea.setEscapeModelStrings(false);
        form.add(confTextArea);
      
        
        final Label xmlLastMod = new Label("xmlLastMod", new Model<String>(cr.getLastModificationTimeAsString()));
        xmlLastMod.setOutputMarkupId(true);
        form.add(xmlLastMod);
        
        final Label errorMessage = new Label("errorMessage", new Model<String>(""));
        errorMessage.setOutputMarkupId(true);
        form.add(errorMessage);
        
     // add a button that can be used to submit the form via ajax
        form.add(new AjaxButton("ajax-button", form)
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
            	
            	String xmlConfTextArea= confTextArea.getDefaultModelObjectAsString();
            	
            	ConfigurationCreator cc2 = new ConfigurationCreator();
            	boolean b = cc2.saveStringAsXml(xmlConfTextArea,filePath);
            	errorMessage.setDefaultModelObject("Saved is successfull!");
            	if (!b)
            		errorMessage.setDefaultModelObject(cc2.getErrorMessage());
            	target.add(errorMessage);
            	
            	ConfigurationReader cr2 =  new ConfigurationReader(filePath);
            	xmlLastMod.setDefaultModelObject(cr2.getLastModificationTimeAsString());
            	target.add(xmlLastMod);
            	
            }

        });
        
	}

}
