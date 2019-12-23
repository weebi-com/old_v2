package com.weebinatidi.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by martial on 11/11/2015.
 */
public class ClientRepository extends RealmObject {

    @PrimaryKey
    private String numero;
    private String nom;
    private int solde;
    private String mail;
    //    @Ignore
    private boolean isChecked;

    // One-to-many relation The list of deposit
    private RealmList<OperationClientRepository> operationClients;

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

    public RealmList<OperationClientRepository> getOperationClients() {
        return operationClients;
    }

    public void setOperationClients(RealmList<OperationClientRepository> operationClients) {
        this.operationClients = operationClients;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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

}
