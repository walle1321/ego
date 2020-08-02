package com.ego.commons.pojo;

import java.io.Serializable;

public class MailPojo implements Serializable {
    private String mail;
    private String id;

    public MailPojo() {
    }

    public MailPojo(String mail, String id) {
        this.mail = mail;
        this.id = id;
    }

    @Override
    public String toString() {
        return "MailPojo{" +
                "mail='" + mail + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
