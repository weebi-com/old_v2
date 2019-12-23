package com.weebinatidi.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by martial on 11/11/2015.
 * represente une operation client
 * qui peut etre une achat ou un depot
 * quand invoice est definit on a affaire a un achat..
 */


public class OperationClientRepository extends RealmObject {
    private Date date;
    private int montant;
    private ClientRepository client;
    private String numero;
    //TODO Shall we reactivate this one...
    private InvoiceRepository invoiceRepository;
    private boolean isInvoice;

    public InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
    }

    public void setInvoiceRepository(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public boolean isInvoice() {
        return isInvoice;
    }

    public void setInvoice(boolean invoice) {
        isInvoice = invoice;
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

    public ClientRepository getClient() {
        return client;
    }

    public void setClient(ClientRepository client) {
        this.client = client;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
