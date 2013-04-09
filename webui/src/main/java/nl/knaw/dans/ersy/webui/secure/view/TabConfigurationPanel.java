/**
 * 
 */
package nl.knaw.dans.ersy.webui.secure.view;

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
		
		ConfigurationReader cr = new ConfigurationReader("src/main/resources/configuration.xml");
			
		Form<Void> form = new Form<Void>("form");
        add(form);
        final TextArea<String> confTextArea = new TextArea<String>("confTextArea", new Model<String>(cr.toString()));
        form.add(confTextArea);
      
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
            	String xmlConfTextArea= confTextArea.getDefaultModelObjectAsString();

            	
            	selectedPid.setDefaultModelObject("Saved is successfull!");
            	target.add(selectedPid);
            }

        });
        
	}

}
