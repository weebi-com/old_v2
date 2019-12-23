package com.weebinatidi.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by DadjaBASSOU on 4/20/16.
 */
public class  ArchiveInvoiceRepository extends RealmObject {

    private Date date;
    private RealmList<ItemRepository> items;
    private ClientRepository client;
    private int total;
    private String Invoicerepositoryarchive;

    @PrimaryKey
    private int id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmList<ItemRepository> getItems() {
        return items;
    }

    public void setItems(RealmList<ItemRepository> items) {
        this.items = items;
    }

    public ClientRepository getClient() {
        return client;
    }

    public void setClient(ClientRepository client) {
        this.client = client;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoicerepositoryarchive() {
        return Invoicerepositoryarchive;
    }

    public void setInvoicerepositoryarchive(String invoicerepositoryarchive) {
        Invoicerepositoryarchive = invoicerepositoryarchive;
    }
}
