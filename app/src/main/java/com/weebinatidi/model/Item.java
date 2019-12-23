package com.weebinatidi.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by martial on 01/03/2016.
 * <p>
 * Represente le produit
 */
public class Item {
    public int quantity;
    public int totalPrice;
    public int unitPrice;
    public String name;
    private int id;
    private String dateinv;

    public Item() {
    }

    public Item(int quantity, int totalPrice, int unitPrice, String name) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.unitPrice = unitPrice;
        this.name = name;

    }

    public Item(int quantity, int totalPrice, int unitPrice, String name, int id) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.unitPrice = unitPrice;
        this.name = name;
        this.id = id;
    }

//    public Item(ItemRepository item)

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toJSON() {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", getName());
            jsonObject.put("quantity", getQuantity());
            jsonObject.put("unitPrice", getUnitPrice());
            jsonObject.put("totalPrice", getTotalPrice());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }


    public JSONObject toJSONObject() {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", getName());
            jsonObject.put("quantity", getQuantity());
            jsonObject.put("unitPrice", getUnitPrice());
            jsonObject.put("totalPrice", getTotalPrice());

            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public String getDateinv() {
        return dateinv;
    }

    public void setDateinv(String dateinv) {
        this.dateinv = dateinv;
    }
}
