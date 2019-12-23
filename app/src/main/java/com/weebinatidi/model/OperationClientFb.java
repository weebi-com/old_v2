package com.weebinatidi.model;

import java.util.Date;

/**
 * Created by mbp on 23/04/2017.
 */

//cette modele est utilisé pour la base firebase
public class OperationClientFb {
    private Date date;
    private int montant;
    private Client client;
    //TODO Shall we reactivate this one...
    private Invoice invoice;

    public OperationClientFb() {

    }

    public OperationClientFb(Client client) {
        this.client = client;
    }

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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
