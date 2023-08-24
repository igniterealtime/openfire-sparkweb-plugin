/*
 * Copyright (c) 2022.
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

package org.jivesoftware.openfire.plugin.rest.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * The Class NotificationEntity.
 */
@XmlRootElement(name = "notification")
@XmlType(propOrder = { "subject", "body", "icon", "requireInteraction", "persistent", "sticky", "data", "actions", "token"})
public class NotificationEntity {

    private String subject;
    private String token;	
    private String body;
    private String icon;	
	private boolean requireInteraction;
	private boolean persistent;
	private boolean sticky;	
    private String data;	
    private List<NotificationActionEntity> actions;	

    /**
     * Instantiates a new message entity.
     */
    public NotificationEntity() {
    }

    /**
     * Gets the body.
     *
     * @return the body
     */
    @XmlElement
    public String getBody() {
        return body;
    }

    /**
     * Sets the body.
     *
     * @param body
     *            
     */
    public void setBody(String body) {
        this.body = body;
    }

    @XmlElement
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }	
	
    @XmlElement
    public boolean getRequireInteraction() { return requireInteraction; }
    public void setRequireInteraction(boolean requireInteraction) { this.requireInteraction = requireInteraction; }	

    @XmlElement
    public boolean getPersistent() { return persistent; }
    public void setPersistent(boolean persistent) { this.persistent = persistent; }	

    @XmlElement
    public boolean getSticky() { return sticky; }
    public void setSticky(boolean sticky) { this.sticky = sticky; }	
	
    @XmlElement
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }	

    @XmlElement
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    @XmlElement
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }	

    @XmlElement
    public List<NotificationActionEntity> getActions() { return actions; }
    public void setActions(List<NotificationActionEntity> actions) { this.actions = actions; }		
}
