package com.weebinatidi.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mbp on 22/03/2017.
 */

public class ReferenceRepository extends RealmObject {

    @PrimaryKey
    private String nomref;
    private RealmList<Referencelist> refs;

    public ReferenceRepository() {
    }

    public ReferenceRepository(String nomref) {
        this.nomref = nomref;
    }


    public String getNomref() {
        return nomref;
    }

    public void setNomref(String nomref) {
        this.nomref = nomref;
    }

    public RealmList<Referencelist> getRefs() {
        return refs;
    }

    public void setRefs(RealmList<Referencelist> refs) {
        this.refs = refs;
    }
}
