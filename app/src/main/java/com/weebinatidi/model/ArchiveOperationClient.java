package com.weebinatidi.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by DadjaBASSOU on 4/20/16.
 */
public class ArchiveOperationClient extends RealmObject {

    private Date date;
    private int montant;
    private ClientRepository client;
    private String operationclientarchive;
    //private Invoice invoice;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public ClientRepository getClient() {
        return client;
    }

    public void setClient(ClientRepository client) {
        this.client = client;
    }


    public String getOperationclientarchive() {
        return operationclientarchive;
    }

    public void setOperationclientarchive(String operationclientarchive) {
        this.operationclientarchive = operationclientarchive;
    }
}
