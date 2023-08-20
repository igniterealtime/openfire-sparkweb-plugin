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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class PresenceEntity.
 */
@XmlRootElement(name = "presence")
@XmlType(propOrder = { "username", "show", "status" })
public class PresenceEntity {

    private String username;
	private String show;
	private String status;	

    /**
     * Instantiates a new presence entity.
     */
    public PresenceEntity() {

    }

    /**
     * Instantiates a new presence entity.
     *
     * @param username
     *            the username
     * @param show
     *            the show
     * @param status
     *            the status
     */
    public PresenceEntity(String username, String show, String status) {
        this.username = username;
        this.show = show;
        this.status = status;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    @XmlElement
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }
	
	/**
	 * Gets the presence show.
	 *
	 * @return the presence show
	 */
	@XmlElement
	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	/**
	 * Gets the presence status.
	 *
	 * @return the status
	 */
	@XmlElement
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
}
