package com.weebinatidi.model;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * /**
 * Created by martial on 01/03/2016.
 * Represente une facture
 * c'est forcement achat
 * <p>
 * NB Correspond a Invoice mais cette classe est un RealmObject
 */
public class InvoiceRepository extends RealmObject {
    private Date date;
    private RealmList<ItemRepository> items;
    private boolean isInvoiceRepo;
    private ClientRepository client;
    private int total;


    @PrimaryKey
    private int id;

    public InvoiceRepository() {
        items = new RealmList<ItemRepository>();
    }

    public InvoiceRepository getByPrimaryKey(Realm realm, int id) {
        return realm.where(getClass()).equalTo("id", id).findFirst();
    }

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

    public boolean isInvoiceRepo() {
        return isInvoiceRepo;
    }

    public void setInvoiceRepo(boolean invoiceRepo) {
        isInvoiceRepo = invoiceRepo;
    }
}
