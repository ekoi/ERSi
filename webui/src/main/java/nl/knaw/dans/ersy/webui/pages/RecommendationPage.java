/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.ersy.webui.pages;

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

/**
 * Navigation panel for the examples project.
 * 
 * @author Eelco Hillenius
 */

public final class RecommendationPage extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 970185289293886277L;

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
	public RecommendationPage(String id, String exampleTitle, WebPage page)
	{
		super(id);
		 
		final Label rec1 = new Label("rec1", new Model<String>(""));
	    rec1.setOutputMarkupId(true);
	    add(rec1);
		
	    final Label rec2 = new Label("rec2", new Model<String>(""));
	    rec2.setOutputMarkupId(true);
	    add(rec2);
	    
	    final Label rec3 = new Label("rec3", new Model<String>(""));
	    rec3.setOutputMarkupId(true);
	    add(rec3);
	    
	    
	    
		Form<Void> form = new Form<Void>("form");
        add(form);

        final TextField<String> field = new TextField<String>("pid", new Model<String>("urn:nbn:nl:ui:13-"));
        form.add(field);

        final Label selectedPid = new Label("selectedPid", new Model<String>(""));
        selectedPid.setOutputMarkupId(true);
        form.add(selectedPid);

      
        
     // add a button that can be used to submit the form via ajax
        form.add(new AjaxButton("ajax-button", form)
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
            	selectedPid.setDefaultModelObject("TITLE AND DESC ---" + field.getDefaultModelObjectAsString());
                target.add(selectedPid);
                rec1.setDefaultModelObject("REC1 ---" + field.getDefaultModelObjectAsString());
                target.add(rec1);
                rec2.setDefaultModelObject("REC2 ---" + field.getDefaultModelObjectAsString());
                target.add(rec2);
                rec3.setDefaultModelObject("REC3 ---" + field.getDefaultModelObjectAsString());
                target.add(rec3);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
                // repaint the feedback panel so errors are shown
                target.add(rec1);
            }
        });

	}
	
	
	
}
