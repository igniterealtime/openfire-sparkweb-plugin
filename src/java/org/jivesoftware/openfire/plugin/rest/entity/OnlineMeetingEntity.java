package org.jivesoftware.openfire.plugin.rest.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "online-meeting")
public class OnlineMeetingEntity {

    private String url;

    public OnlineMeetingEntity() {
    }

    public OnlineMeetingEntity(String url)
    {
        this.url = url;
    }


    @XmlElement
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}