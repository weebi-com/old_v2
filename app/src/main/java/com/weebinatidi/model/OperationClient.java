package com.weebinatidi.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by DadjaBASSOU on 5/5/16.
 */
public class OperationClient {

    private Date date;
    private int montant;
    private Client client;
    //TODO Shall we reactivate this one...
    private Invoice invoice;
    private boolean isInvoice;


    public OperationClient() {
    }

    public OperationClient(Date date, int montant, Client client, Invoice invoice) {
        this.date = date;
        this.montant = montant;
        this.client = client;
        this.invoice = invoice;

    }

    public OperationClient(Client client) {
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

    public boolean isInvoice() {
        return isInvoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void setIsInvoice(boolean invoice) {
        isInvoice = invoice;
    }


    public String toJSON() {


        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("date", getDate());
            jsonObject.put("montant", getMontant());
            jsonObject.put("client", getClient().toJSON());
            jsonObject.put("invoice", getInvoice().toJSON());
//            jsonObject.put("isInvoice", get);
//
//            JSONArray jsonArray= new JSONArray();
//            JSONObject itemjsonobject = new JSONObject();
//
//            for (OperationClientRepository operation: operationClients)
//            {
//                itemjsonobject.put("operation" ,operation.toJSON());
//                jsonArray.put(itemjsonobject);
//            }
//
//            jsonObject.put("operations",jsonArray);

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }
}
