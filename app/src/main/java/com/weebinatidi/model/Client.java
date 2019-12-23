package com.weebinatidi.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DadjaBASSOU on 5/5/16.
 */
public class Client {

    public boolean isChecked;
    private String numero;
    private String nom;
    private int solde;
    private String id;
    private String mail;
    private ArrayList<OperationClient> operationClients;
    private ArrayList<OperationClientFb> operationClientsfb;



    public Client() {
    }

    public Client(String nom, String numero, String id, int solde) {
        this.numero = numero;
        this.nom = nom;
        this.id = id;
        this.solde = solde;
    }


    public Client(ClientRepository client) {


        if (client != null) {


            numero = client.getNumero();
            nom = client.getNom();
            solde = client.getSolde();
            mail = client.getMail();

//        operationClients =new ArrayList<OperationClient>();


//        if(client.getOperationClients().size() > 0)
//        {
//            for(OperationClientRepository item : client.getOperationClients())
//            {
//                OperationClient o=new OperationClient();
//                o.setDate(item.getDate());
//                o.setMontant(item.getMontant());
//
//                o.getClient().setMail(item.getInvoiceRepository().getClient().getMail());
//                o.getClient().setNom(item.getInvoiceRepository().getClient().getNom());
//                o.getClient().setNumero(item.getInvoiceRepository().getClient().getNumero());
//                o.getClient().setSolde(item.getInvoiceRepository().getClient().getSolde());
//
//
//
//
//                if(item.getInvoiceRepository().getClient() != null)
//                    {
//                        o.getInvoice().setClient(item.getInvoiceRepository().getClient());
//
//                    }
//
//
//                operationClients.add(o);
//            }
//
//        }

        }

    }

    public Client(String numero, String nom, int solde, String mail, ArrayList<OperationClient> operationClients, boolean isChecked) {
        this.numero = numero;
        this.nom = nom;
        this.solde = solde;
        this.mail = mail;
        this.operationClients = operationClients;
        this.isChecked = isChecked;
    }

    public ArrayList<OperationClient> getOperationClients() {
        return operationClients;
    }

    public void setOperationClients(ArrayList<OperationClient> operationClients) {
        this.operationClients = operationClients;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getSolde() {
        return solde;
    }

    public void setSolde(int solde) {
        this.solde = solde;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public String toJSON() {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("numero", getNumero());
            jsonObject.put("nom", getNom());
            jsonObject.put("solde", getSolde());
            jsonObject.put("mail", getMail());

//            JSONArray jsonArray= new JSONArray();
//            JSONObject itemjsonobject = new JSONObject();
//
//            for (OperationClient operation: operationClients)
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

    public ArrayList<OperationClientFb> getOperationClientsfb() {
        return operationClientsfb;
    }

    public void setOperationClientsfb(ArrayList<OperationClientFb> operationClientsfb) {
        this.operationClientsfb = operationClientsfb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
