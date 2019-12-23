package com.weebinatidi.model;

import io.realm.RealmObject;

/**
 * Created by mbp on 23/03/2017.
 */

public class Referencelist extends RealmObject {

    private String nomduref;

    public Referencelist() {
    }

    public Referencelist(String nomduref) {
        this.nomduref = nomduref;
    }

    public String getNomduref() {
        return nomduref;
    }

    public void setNomduref(String nomduref) {
        this.nomduref = nomduref;
    }
}
