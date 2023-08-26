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
 * The Class NodeEntity.
 */
@XmlRootElement(name = "node")
@XmlType(propOrder = { "id", "name", "description", "owner"})
public class NodeEntity {

    private String id;
    private String name;	
    private String description;	
    private List<String> owners;
	
    public NodeEntity() {
		
    }
	
    public NodeEntity(String id, String name, String description, List<String> owners) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.owners = owners;
    }

    @XmlElement
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }	
	
    @XmlElement
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }	

    @XmlElement
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }	

    @XmlElement
    public List<String> getOwners() { return owners; }
    public void setOwners(List<String> owners) { this.owners = owners; }
}
