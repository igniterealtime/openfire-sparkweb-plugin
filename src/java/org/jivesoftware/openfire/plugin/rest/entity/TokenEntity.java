package org.jivesoftware.openfire.plugin.rest.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "token")
public class TokenEntity {

    private String token;

    public TokenEntity() {
    }

    public TokenEntity(String token) {
        this.token = token;
    }


    @XmlElement
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}