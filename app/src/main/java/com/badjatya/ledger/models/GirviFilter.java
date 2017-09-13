package com.badjatya.ledger.models;

import java.io.Serializable;

/**
 * Created by Tanmay on 11/14/2016.
 */
public class GirviFilter implements Serializable{
    String name="";
    String fatherName="";
    String village="";
    String caste="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }
}
