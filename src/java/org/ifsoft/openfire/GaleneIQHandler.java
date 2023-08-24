/*
 * Copyright (C) 2017 Ignite Realtime Foundation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 
package org.ifsoft.openfire;

import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.disco.ServerFeaturesProvider;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.cache.Cache;
import org.jivesoftware.util.cache.CacheFactory;
import org.jivesoftware.openfire.event.SessionEventListener;
import org.jivesoftware.openfire.event.SessionEventDispatcher;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.muc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Presence;
import org.xmpp.packet.Message;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;

import java.nio.charset.StandardCharsets;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import net.sf.json.*;

/**
 * custom IQ handler for HTTP Online Meeting requests and responses
 */
public class GaleneIQHandler extends IQHandler implements ServerFeaturesProvider
{
    private static final Logger Log = LoggerFactory.getLogger( GaleneIQHandler.class );	
	private static final String domain = XMPPServer.getInstance().getServerInfo().getXMPPDomain();
	
    public static final String ELEMENT_NAME = "request";
    public static final String NAMESPACE = "urn:xmpp:http:online-meetings:galene:0";
	
	public void startHandler() {

	}

	public void stopHandler() {
	
	}
	
    public GaleneIQHandler() {
        super("Galene IQ Handler");
    }

    @Override
    public IQ handleIQ(IQ iq)
    {
		if (iq.getType() == IQ.Type.set || iq.getType() == IQ.Type.get) {
			IQ reply = IQ.createResultIQ(iq);

			try {
				Log.debug("Galene handleIQ \n" + iq.toString());
				final Element element = iq.getChildElement();
				final String from = iq.getFrom().toBareJID();
					
				if (element != null) {
					final String meetingId = element.attribute("id").getValue();	
					String url =  JiveGlobals.getProperty("galene.url");			
					
					if ( url == null || url.trim().isEmpty() )	{
						final String protocol = "https"; // No point in providing the non-SSL protocol, as webRTC won't work there.
						final String host = XMPPServer.getInstance().getServerInfo().getHostname();
						final String port = JiveGlobals.getProperty("httpbind.port.secure", "7443");
						final String path = JiveGlobals.getProperty("ofmeet.webapp.contextpath");
						
						url = protocol + "//" + host + ":" + port;
					}
					
					url = url + "/galene/?room=public/" + meetingId;
					
					final Element childElement = reply.setChildElement("x", "jabber:x:oob");
					childElement.addElement("url").setText(URLEncoder.encode(url, StandardCharsets.UTF_8.toString()));																																	
				}
				else {
					reply.setError(new PacketError(PacketError.Condition.not_allowed, PacketError.Type.modify, "request element is missing"));
				}
				return reply;

			} catch(Exception e) {
				Log.error("Galene handleIQ", e);
				reply.setError(new PacketError(PacketError.Condition.internal_server_error, PacketError.Type.modify, e.toString()));
				return reply;
			}
		}
		return null;
    }	
	

    @Override
    public IQHandlerInfo getInfo() {
        return new IQHandlerInfo(ELEMENT_NAME, NAMESPACE);
    }
	
    @Override
    public Iterator<String> getFeatures()
    {
        final ArrayList<String> features = new ArrayList<>();
        features.add( NAMESPACE );
        return features.iterator();
    }		
}
