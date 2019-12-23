package com.weebinatidi.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by martial on 11/11/2015.
 * quand un achat n'est pas lie a un client
 * ce qui veut dire que le client a payer cash avec de l'argent liquide
 */
public class Cash extends RealmObject {
    private RealmList<InvoiceRepository> invoices;
    private String saveDate;

    @PrimaryKey
    private int id;

    public RealmList<InvoiceRepository> getInvoices() {
        return invoices;
    }

    public void setInvoices(RealmList<InvoiceRepository> invoices) {
        this.invoices = invoices;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
