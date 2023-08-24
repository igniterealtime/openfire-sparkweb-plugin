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

@XmlRootElement(name = "action")
@XmlType(propOrder = { "action", "type", "title", "icon", "placeholder", "data", "value"})
public class NotificationActionEntity {
    String action;
    String data;	
    String value;	
    String icon;	
    String type;
    String placeholder;
    String title;

    public NotificationActionEntity() {

	}
	
    public NotificationActionEntity(String action, String type, String title) {
		this.action = action;
		this.type = type;
		this.title = title;
    }	

    @XmlElement
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    @XmlElement
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
	
    @XmlElement
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @XmlElement
    public String getTitle() {
		return title; 
	}
	
    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement
    public String getPlaceholder() {
        return placeholder;
    }
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @XmlElement
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
	
    @XmlElement
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
