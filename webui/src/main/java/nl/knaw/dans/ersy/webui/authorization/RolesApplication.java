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
package nl.knaw.dans.ersy.webui.authorization;

import java.util.Arrays;
import java.util.List;

import nl.knaw.dans.ersy.webui.authorization.pages.AdminPanelPage;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;


/**
 * Application object for this example.
 * 
 * @author Eelco Hillenius
 */
public class RolesApplication extends WebApplication
{
	/**
	 * User DB.
	 */
	public static List<User> USERS = Arrays.asList(new User("Admin", "ADMIN"),
		new User("User", "USER"), new User("Guest", ""));

	/**
	 * Construct.
	 */
	public RolesApplication()
	{
		super();
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage()
	{
		return AdminPanelPage.class;
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request,
	 *      org.apache.wicket.request.Response)
	 */
	@Override
	public Session newSession(Request request, Response response)
	{
		return new RolesSession(request);
	}

	@Override
	protected void init()
	{
		super.init();
		mountPage("/admin", AdminPanelPage.class);
		getDebugSettings().setDevelopmentUtilitiesEnabled(true);

		getSecuritySettings().setAuthorizationStrategy(
			new RoleAuthorizationStrategy(new UserRolesAuthorizer()));
		
		
	}

}
