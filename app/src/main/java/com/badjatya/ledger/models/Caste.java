package com.badjatya.ledger.models;

import java.util.UUID;

/**
 * Created by Tanmay on 11/1/2016.
 */
public class Caste {
    String id;
    String casteName;

    public Caste(String id, String casteName) {
        this.id = id;
        this.casteName = casteName;
    }

    public Caste(String casteName) {
        this.id= UUID.randomUUID().toString();
        this.casteName = casteName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCasteName() {
        return casteName;
    }

    public void setCasteName(String casteName) {
        this.casteName = casteName;
    }
}
