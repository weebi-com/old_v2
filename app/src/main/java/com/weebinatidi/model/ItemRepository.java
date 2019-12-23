package com.weebinatidi.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by martial on 01/03/2016.
 * <p>
 * Represente le produit
 * est aussi Item
 * mais cette classe est un realmObject...
 */
public class ItemRepository extends RealmObject {
    private int quantity;
    private int totalPrice;
    private int unitPrice;
    private String name;
    private String laref;

    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLaref() {
        return laref;
    }

    public void setLaref(String laref) {
        this.laref = laref;
    }
}
