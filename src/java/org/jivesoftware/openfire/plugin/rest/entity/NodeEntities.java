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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class NodeEntities.
 */
@XmlRootElement(name = "nodes")
public class NodeEntities {

    /** The nodes. */
    List<NodeEntity> nodes;

    /**
     * Instantiates a new node entities.
     */
    public NodeEntities() {

    }

    /**
     * Instantiates a new node entities.
     *
     * @param nodes
     *            the nodes
     */
    public NodeEntities(List<NodeEntity> nodes) {
        this.nodes = nodes;
    }

    /**
     * Gets the nodes.
     *
     * @return the nodes
     */
    @XmlElement(name = "node")
    @JsonProperty(value = "nodes")
    public List<NodeEntity> getNodes() {
        return nodes;
    }

    /**
     * Sets the nodes.
     *
     * @param nodes
     *            the new nodes
     */
    public void setNodes(List<NodeEntity> nodes) {
        this.nodes = nodes;
    }

}
