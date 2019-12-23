package com.weebinatidi.model;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by martial on 01/03/2016.
 * Represente une facture
 * c'est forcement achat
 */
public class Invoice {
    public Date date;
    private String dates;//ajouter pour la base sqlite
    public ArrayList<Item> items;
    public ClientRepository client;
    private String numidcli;//ajouter pour la base sqlite
    public int total;
    private Client clientm;
    private int id;
    private String type;
    private String nomcli;
    private String numcli;
//    Gson gson;

    public Invoice() {

    }

    public Invoice(InvoiceRepository invoice) {

//        gson=new Gson();

        date = invoice.getDate();
        items = new ArrayList<>();

        if (invoice.getItems().size() > 0) {
            for (ItemRepository item : invoice.getItems()) {
                Item o = new Item();
                o.setName(item.getName());
                o.setQuantity(item.getQuantity());
                o.setTotalPrice(item.getTotalPrice());
                o.setUnitPrice(item.getUnitPrice());
                items.add(o);
            }

        }

        client = invoice.getClient();
        total = invoice.getTotal();
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
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

    @Override
    public String toString() {
        return new Gson().toJson(this, Invoice.class);
//        return new gson.toJson(this);
    }


    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            //insertion infos facture...
            jsonObject.put("date", getDate());
            jsonObject.put("total", getTotal());


            //if our client do exist it means it was a vente client
            //instead its a vente cash so we don't refer our client to this ..
            if (getClient() != null) {
                //insertion des donnees client dans un jsonobject...
                JSONObject ClientObjectJson = new JSONObject();
                ClientObjectJson.put("name", getClient().getNom());
                ClientObjectJson.put("mail", getClient().getMail());
                ClientObjectJson.put("numero", getClient().getNumero());
                ClientObjectJson.put("solde", getClient().getSolde());

                //insertion du client json object dans notre facture...
                jsonObject.put("client", ClientObjectJson);
            }


            JSONArray jsonArray = new JSONArray();
            JSONObject itemjsonobject = new JSONObject();


            if ((items != null) && (items.size() > 0)) {
                for (Item item : items) {
                    itemjsonobject.put("nomproduit", item.getName());
                    itemjsonobject.put("quantite", item.getQuantity());
                    itemjsonobject.put("prixunitaire", item.getUnitPrice());
                    itemjsonobject.put("prixtotal", item.getTotalPrice());
                    jsonArray.put(itemjsonobject);
                }
            }

            jsonObject.put("items", jsonArray);

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
            //insertion infos facture...
            jsonObject.put("date", getDate());
            jsonObject.put("total", getTotal());


            //if our client do exist it means it was a vente client
            //instead its a vente cash so we don't refer our client to this ..
            if (getClient() != null) {
                //insertion des donnees client dans un jsonobject...
                JSONObject ClientObjectJson = new JSONObject();
                ClientObjectJson.put("name", getClient().getNom());
                ClientObjectJson.put("mail", getClient().getMail());
                ClientObjectJson.put("numero", getClient().getNumero());
                ClientObjectJson.put("solde", getClient().getSolde());

                //insertion du client json object dans notre facture...
                jsonObject.put("client", ClientObjectJson);
            }

//            Item[] itemsArray = new Item[items.size()];
//
//            for (int i = 0; i < itemsArray.length; i++) {
//                itemsArray[i] = new Item(items.get(i));
//            }

            JSONArray jsonArray = new JSONArray();
            JSONObject itemjsonobject = new JSONObject();

            if ((items != null) && (items.size() > 0)) {
//                for (Item item: items)
//                {
//                    itemjsonobject.put("nomproduit" ,item.getName());
//                    itemjsonobject.put("quantite" ,item.getQuantity());
//                    itemjsonobject.put("prixunitaire" ,item.getUnitPrice());
//                    itemjsonobject.put("prixtotal" ,item.getTotalPrice());
//                    jsonArray.put(itemjsonobject);
//                }
//
//                for(int i=0;i<items.size();i++)
//                {
//                    itemjsonobject.put("nomproduit" ,items.get(i).getName());
//                    itemjsonobject.put("quantite" ,items.get(i).getQuantity());
//                    itemjsonobject.put("prixunitaire" ,items.get(i).getUnitPrice());
//                    itemjsonobject.put("prixtotal" ,items.get(i).getTotalPrice());
//                    jsonArray.put(itemjsonobject);
//                }

                for (Item item : items) {
                    jsonArray.put(item.toJSONObject());
                }
            }

            jsonObject.put("items", jsonArray);

            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClientm() {
        return clientm;
    }

    public void setClientm(Client clientm) {
        this.clientm = clientm;
    }

    public String getNumidcli() {
        return numidcli;
    }

    public void setNumidcli(String numidcli) {
        this.numidcli = numidcli;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNomcli() {
        return nomcli;
    }

    public void setNomcli(String nomcli) {
        this.nomcli = nomcli;
    }

    public String getNumcli() {
        return numcli;
    }

    public void setNumcli(String numcli) {
        this.numcli = numcli;
    }
}
