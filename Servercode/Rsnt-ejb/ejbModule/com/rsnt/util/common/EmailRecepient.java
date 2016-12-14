package com.rsnt.util.common;

import java.io.Serializable;

import org.jboss.seam.annotations.Name;

@Name("emailRecepient")
public class EmailRecepient implements Serializable{

    private static final long serialVersionUID = -8410710135998941301L;

    String name;

    String emailId;

    public EmailRecepient(){
        
    }
    
    public EmailRecepient(String name, String emailId){
        this.name = name;
        this.emailId = emailId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
