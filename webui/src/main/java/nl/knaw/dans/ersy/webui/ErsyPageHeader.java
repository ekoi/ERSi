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
package nl.knaw.dans.ersy.webui;

import javax.servlet.ServletContext;

import nl.knaw.dans.ersy.webui.pages.ApiPage;
import nl.knaw.dans.ersy.webui.pages.ContactPage;
import nl.knaw.dans.ersy.webui.pages.HomePage;
import nl.knaw.dans.ersy.webui.pages.HowItWorkPage;
import nl.knaw.dans.ersy.webui.pages.publications.PublicationPage;
import nl.knaw.dans.ersy.webui.secure.view.AdminPage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Navigation panel for the examples project.
 * 
 * @author Eelco Hillenius
 */
public final class ErsyPageHeader extends Panel
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
	public ErsyPageHeader(String id, WebPage page)
	{
		super(id);
		add(new BookmarkablePageLink<HomePage>("home", HomePage.class));
		add(new BookmarkablePageLink<AdminPage>("admin", AdminPage.class));
		add(new BookmarkablePageLink<HowItWorkPage>("how", HowItWorkPage.class));
		add(new BookmarkablePageLink<ApiPage>("api", ApiPage.class));
		add(new BookmarkablePageLink<PublicationPage>("pub", PublicationPage.class));
		add(new BookmarkablePageLink<ContactPage>("contact", ContactPage.class));
	}
	
	public static String getRootContext(){
		 
		String rootContext = "";
 
		WebApplication webApplication = WebApplication.get();
		if(webApplication!=null){
			ServletContext servletContext = webApplication.getServletContext();
			if(servletContext!=null){
				rootContext = servletContext.getServletContextName();
			}else{
				//do nothing
			}
		}else{
			//do nothing
		}
 
		return rootContext;
 
}
}
