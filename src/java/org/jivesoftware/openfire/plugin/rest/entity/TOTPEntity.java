package org.jivesoftware.openfire.plugin.rest.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "totp")
public class TOTPEntity {

    private String url;

    public TOTPEntity() {
    }

    public TOTPEntity(String url)
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