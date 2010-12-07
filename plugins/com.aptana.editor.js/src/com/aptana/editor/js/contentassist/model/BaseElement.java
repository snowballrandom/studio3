/**
 * This file Copyright (c) 2005-2010 Aptana, Inc. This program is
 * dual-licensed under both the Aptana Public License and the GNU General
 * Public license. You may elect to use one or the other of these licenses.
 * 
 * This program is distributed in the hope that it will be useful, but
 * AS-IS and WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, TITLE, or
 * NONINFRINGEMENT. Redistribution, except as permitted by whichever of
 * the GPL or APL you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or modify this
 * program under the terms of the GNU General Public License,
 * Version 3, as published by the Free Software Foundation.  You should
 * have received a copy of the GNU General Public License, Version 3 along
 * with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Aptana provides a special exception to allow redistribution of this file
 * with certain other free and open source software ("FOSS") code and certain additional terms
 * pursuant to Section 7 of the GPL. You may view the exception and these
 * terms on the web at http://www.aptana.com/legal/gpl/.
 * 
 * 2. For the Aptana Public License (APL), this program and the
 * accompanying materials are made available under the terms of the APL
 * v1.0 which accompanies this distribution, and is available at
 * http://www.aptana.com/legal/apl/.
 * 
 * You may view the GPL, Aptana's exception and additional terms, and the
 * APL in the file titled license.html at the root of the corresponding
 * plugin containing this source file.
 * 
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.js.contentassist.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.mortbay.util.ajax.JSON.Convertible;
import org.mortbay.util.ajax.JSON.Output;

import com.aptana.core.util.StringUtil;

public class BaseElement implements Convertible
{
	private static final String USER_AGENTS_PROPERTY = "userAgents"; //$NON-NLS-1$
	private static final String SINCE_PROPERTY = "since"; //$NON-NLS-1$
	private static final String DESCRIPTION_PROPERTY = "description"; //$NON-NLS-1$
	private static final String NAME_PROPERTY = "name"; //$NON-NLS-1$

	private String _name;
	private String _description;
	private List<UserAgentElement> _userAgents;
	private List<SinceElement> _sinceList;
	private List<String> _documents;

	/**
	 * BaseElement
	 */
	public BaseElement()
	{
	}

	/**
	 * addDocument
	 * 
	 * @param document
	 */
	public void addDocument(String document)
	{
		if (document != null && document.length() > 0)
		{
			if (this._documents == null)
			{
				this._documents = new ArrayList<String>();
			}

			this._documents.add(document);
		}
	}

	/**
	 * addSince
	 * 
	 * @param since
	 */
	public void addSince(SinceElement since)
	{
		if (since != null)
		{
			if (this._sinceList == null)
			{
				this._sinceList = new ArrayList<SinceElement>();
			}

			this._sinceList.add(since);
		}
	}

	/**
	 * addUserAgent
	 * 
	 * @param userAgent
	 */
	public void addUserAgent(UserAgentElement userAgent)
	{
		if (userAgent != null)
		{
			if (this._userAgents == null)
			{
				this._userAgents = new ArrayList<UserAgentElement>();
			}

			this._userAgents.add(userAgent);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mortbay.util.ajax.JSON.Convertible#fromJSON(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	public void fromJSON(Map object)
	{
		this.setName(object.get(NAME_PROPERTY).toString());
		this.setDescription(object.get(DESCRIPTION_PROPERTY).toString());

		// since list
		Object sinceList = object.get(SINCE_PROPERTY);

		if (sinceList != null && sinceList.getClass().isArray())
		{
			for (Object since : (Object[]) sinceList)
			{
				if (since instanceof Map)
				{
					SinceElement s = new SinceElement();

					s.fromJSON((Map) since);

					this.addSince(s);
				}
			}
		}

		// user agents
		Object userAgents = object.get(USER_AGENTS_PROPERTY);

		if (userAgents != null && userAgents.getClass().isArray())
		{
			for (Object userAgent : (Object[]) userAgents)
			{
				if (userAgent instanceof Map)
				{
					UserAgentElement ua = new UserAgentElement();

					ua.fromJSON((Map) userAgent);

					this.addUserAgent(ua);
				}
			}
		}
	}

	/**
	 * getDescription
	 * 
	 * @return
	 */
	public String getDescription()
	{
		return StringUtil.getValue(this._description);
	}

	/**
	 * getDocuments
	 * 
	 * @return
	 */
	public List<String> getDocuments()
	{
		List<String> result = this._documents;

		if (result == null)
		{
			result = Collections.emptyList();
		}

		return result;
	}

	/**
	 * getName
	 * 
	 * @return
	 */
	public String getName()
	{
		return StringUtil.getValue(this._name);
	}

	/**
	 * getSinceList
	 * 
	 * @return
	 */
	public List<SinceElement> getSinceList()
	{
		List<SinceElement> result = this._sinceList;

		if (result == null)
		{
			result = Collections.emptyList();
		}

		return result;
	}

	/**
	 * getUserAgentNames
	 * 
	 * @return
	 */
	public List<String> getUserAgentNames()
	{
		List<String> result;

		if (this._userAgents != null)
		{
			result = new ArrayList<String>(this._userAgents.size());

			for (UserAgentElement userAgent : this._userAgents)
			{
				result.add(userAgent.getPlatform());
			}
		}
		else
		{
			result = Collections.emptyList();
		}

		return result;
	}

	/**
	 * getUserAgents
	 * 
	 * @return
	 */
	public List<UserAgentElement> getUserAgents()
	{
		List<UserAgentElement> result = this._userAgents;

		if (result == null)
		{
			result = Collections.emptyList();
		}

		return result;
	}

	/**
	 * setDescription
	 * 
	 * @param description
	 */
	public void setDescription(String description)
	{
		if (description != null)
		{
			this._description = description;
		}
	}

	/**
	 * setName
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		if (name != null)
		{
			this._name = name;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mortbay.util.ajax.JSON.Convertible#toJSON(org.mortbay.util.ajax.JSON.Output)
	 */
	public void toJSON(Output out)
	{
		out.add(NAME_PROPERTY, this.getName());
		out.add(DESCRIPTION_PROPERTY, this.getDescription());
		out.add(SINCE_PROPERTY, this.getSinceList());
		out.add(USER_AGENTS_PROPERTY, this.getUserAgents());
	}
}
