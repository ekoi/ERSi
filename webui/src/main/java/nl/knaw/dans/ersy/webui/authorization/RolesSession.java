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

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

/**
 * Web Session for this example.
 * 
 * @author Eelco Hillenius
 */
public class RolesSession extends AuthenticatedWebSession
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1420594630137606120L;
	/** the current user. */
	private User user = RolesApplication.USERS.get(2);

	/**
	 * Construct.
	 * 
	 * @param request
	 */
	public RolesSession(Request request)
	{
		super(request);
	}

	/**
	 * Gets user.
	 * 
	 * @return user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * Sets user.
	 * 
	 * @param user
	 *            user
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	@Override
	public boolean authenticate(String username, String password) {
		final String WICKET = "wicket";
		if (WICKET.equals(username) && WICKET.equals(password)) {
			RolesSession session = (RolesSession)Session.get();
			user = RolesApplication.USERS.get(0);
			session.setUser(user);
			return true;
		}
		// Check username and password
		return false;
	}

	@Override
	public Roles getRoles() {
		if (isSignedIn())
		{
			// If the user is signed in, they have these roles
			return new Roles(Roles.ADMIN);
		}
		return null;
	}

}
