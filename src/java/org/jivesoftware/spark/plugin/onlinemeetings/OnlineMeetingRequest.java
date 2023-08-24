/**
 * Copyright (C) 2004-2011 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.spark.plugin.onlinemeetings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.XmlEnvironment;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.xml.XmlPullParser;
import org.jivesoftware.smack.xml.XmlPullParserException;

import java.io.IOException;

/**
 * An IQ packet that's a request for an Jitsi Meet url 
 */
public class OnlineMeetingRequest extends IQ
{
    public static final String NAMESPACE = "urn:xmpp:http:online-meetings:jitsi:0";
    public static final String ELEMENT_NAME = "request";
	
    public String url = null;
    public String id = null;

    public OnlineMeetingRequest() {
        super( ELEMENT_NAME, NAMESPACE );
    }

    public OnlineMeetingRequest(String id, String ns)
    {
        super( ELEMENT_NAME, ns );
        this.id = id;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder( IQChildElementXmlStringBuilder buf )
    {
        buf.append(" id=\"" + id + "\"");			
        buf.rightAngleBracket();
        return buf;
    }

    public static class Provider extends IQProvider<OnlineMeetingRequest>
    {
        public Provider() {
            super();
        }

        public OnlineMeetingRequest parse(XmlPullParser parser, int i, XmlEnvironment xmlEnvironment) throws XmlPullParserException, IOException
        {
            final OnlineMeetingRequest meetRequest = new OnlineMeetingRequest();

            boolean done = false;
            while ( !done )
            {
                XmlPullParser.Event eventType = parser.next();

                if ( eventType == XmlPullParser.Event.START_ELEMENT )
                {
                    if ( parser.getName().equals( "url" ) ) {
                        meetRequest.url = parser.nextText();
                    }
                }

                else if ( eventType == XmlPullParser.Event.END_ELEMENT )
                {
                    if ( parser.getName().equals( "x" ) ) {
                        done = true;
                    }
                }
            }

            return meetRequest;
        }
    }
}
