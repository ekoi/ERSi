package nl.knaw.dans.ersy.webui.pages;

import java.util.ArrayList;
import java.util.List;

import nl.knaw.dans.ersy.webui.pages.search.RecursivePanel;
import nl.knaw.dans.ersy.webui.pages.search.SearchPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * Navigation panel for the examples project.
 * 
 * @author Eko Indarto
 */

public final class RecommendationPage extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 970185289293886277L;
	private AjaxCheckBox stdRecCheckbox;
	private AjaxCheckBox abrRecCheckbox;
	private AjaxCheckBox locationRecCheckbox;
	private AjaxCheckBox graphRecCheckbox;

	/**
	 * Construct.
	 * 
	 * @param id
	 *            id of the component
	 * @param exampleTitle
	 *            title of the example
	 * @param page
	 *            The example page
	 */
	public RecommendationPage(String id, String exampleTitle, WebPage page) {
		super(id);

		add(new SearchPanel("searchPanel"));
		List<Object> l1 = new ArrayList<Object>();
       
        RecursivePanel rp = new RecursivePanel("recommendationPanels", l1);
        rp.setOutputMarkupId(true);
        add(rp);
        
        this.setOutputMarkupId(true);
        
        stdRecCheckbox = new AjaxCheckBox("stdRecCheckbox",
                new Model<Boolean>()) {
        	
			private static final long serialVersionUID = 4784932920732316086L;

			@Override
            protected void onUpdate(AjaxRequestTarget target)
            {
				locationRecCheckbox.setModelObject(!getModelObject());
                target.add(locationRecCheckbox);
                abrRecCheckbox.setModelObject(!getModelObject());
                target.add(abrRecCheckbox);
            }
        }; 
        stdRecCheckbox.setDefaultModelObject(true);
        stdRecCheckbox.setOutputMarkupId(true);
        add(stdRecCheckbox);
        
        abrRecCheckbox = new AjaxCheckBox("abrRecCheckbox",
                new Model<Boolean>()) {

			private static final long serialVersionUID = -385819254193372296L;

			@Override
            protected void onUpdate(AjaxRequestTarget target)
            {
            	stdRecCheckbox.setModelObject(!getModelObject());
                target.add(stdRecCheckbox);
                locationRecCheckbox.setModelObject(!getModelObject());
                target.add(locationRecCheckbox);
            }
        }; 
        abrRecCheckbox.setOutputMarkupId(true);
        add(abrRecCheckbox);
        
        locationRecCheckbox = new AjaxCheckBox("locationRecCheckbox",
                new Model<Boolean>()) {

			private static final long serialVersionUID = -385819254193372296L;

			@Override
            protected void onUpdate(AjaxRequestTarget target)
            {
            	stdRecCheckbox.setModelObject(!getModelObject());
                target.add(stdRecCheckbox);
                abrRecCheckbox.setModelObject(!getModelObject());
                target.add(abrRecCheckbox);                
            }
        }; 
        locationRecCheckbox.setOutputMarkupId(true);
        add(locationRecCheckbox);
        
        graphRecCheckbox = new AjaxCheckBox("graphRecCheckbox",
                new Model<Boolean>()) {

			private static final long serialVersionUID = -385819254193372296L;

			@Override
            protected void onUpdate(AjaxRequestTarget target)
            {
				graphRecCheckbox.setModelObject(false);
                target.add(graphRecCheckbox);            
            }
        }; 
        graphRecCheckbox.setOutputMarkupId(true);
        add(graphRecCheckbox);
	}

}