package com.badjatya.ledger.models;

import java.util.UUID;

/**
 * Created by Tanmay on 11/1/2016.
 */
public class Village {

    String id;
    String villageName;

    public Village(String id, String villageName) {
        this.id = id;
        this.villageName = villageName;
    }

    public Village(String villageName) {
        this.id= UUID.randomUUID().toString();
        this.villageName = villageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}
