/**
 * $RCSfile$
 * $Revision$
 * $Date$
 *
 * Copyright 2003-2007 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidpn.packet;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.StringUtils;

/**
 * Represents registration packets. An empty GET query will cause the server to
 * return information about it's registration support. SET queries can be used
 * to create accounts or update existing account information. XMPP servers may
 * require a number of attributes to be set when creating a new account. The
 * standard account attributes are as follows:
 * <ul>
 * <li>name -- the user's name.
 * <li>first -- the user's first name.
 * <li>last -- the user's last name.
 * <li>email -- the user's email address.
 * <li>city -- the user's city.
 * <li>state -- the user's state.
 * <li>zip -- the user's ZIP code.
 * <li>phone -- the user's phone number.
 * <li>url -- the user's website.
 * <li>date -- the date the registration took place.
 * <li>misc -- other miscellaneous information to associate with the account.
 * <li>text -- textual information to associate with the account.
 * <li>remove -- empty flag to remove account.
 * </ul>
 *
 * @author Matt Tucker
 */
public class Chat extends IQ {

	private Type type = Type.SEND;

	private String id;

	private Map<String, String> attributes = new HashMap<String, String>();

	public void addAttribute(String key, String value) {
		attributes.put(key, value);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<message xmlns=\"jabber:iq:chat\">");
		if (attributes != null && attributes.size() > 0) {
			for (String name : attributes.keySet()) {
				String value = attributes.get(name);
				buf.append("<").append(name).append(">");
				buf.append(value);
				buf.append("</").append(name).append(">");
			}
		}
		// Add packet extensions, if any are defined.
		buf.append(getExtensionsXML());
		buf.append("</message>");
		return buf.toString();
	}

	/**
	 * Returns the type of the IQ packet.
	 *
	 * @return the type of the IQ packet.
	 */
	public Type getType() {
		return type;
	}

	@Override
	public String toXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<chat ");
		if (getPacketID() != null) {
			buf.append("id=\"" + getPacketID() + "\" ");
		}
		if (getTo() != null) {
			buf.append("to=\"").append(StringUtils.escapeForXML(getTo())).append("\" ");
		}
		if (getFrom() != null) {
			buf.append("from=\"").append(StringUtils.escapeForXML(getFrom())).append("\" ");
		}
		if (type == null) {
			buf.append("type=\"get\">");
		} else {
			buf.append("type=\"").append(getType()).append("\">");
		}
		// Add the query section if there is one.
		String queryXML = getChildElementXML();
		if (queryXML != null) {
			buf.append(queryXML);
		}
		// Add the error sub-packet, if there is one.
		XMPPError error = getError();
		if (error != null) {
			buf.append(error.toXML());
		}
		buf.append("</chat>");
		return buf.toString();
	}

}