package com.weebinatidi.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by DadjaBASSOU on 4/20/16.
 */
public class ArchiveClient extends RealmObject {

    @Ignore
    public boolean isChecked;
    @PrimaryKey
    private String numero;
    private String nom;
    private int solde;
    private String mail;
    private String clientarchive;
    // One-to-many relation The list of deposit
    private RealmList<OperationClientRepository> operationClients;


    public String getClientarchive() {
        return clientarchive;
    }

    public void setClientarchive(String clientarchive) {
        this.clientarchive = clientarchive;
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

    public RealmList<OperationClientRepository> getOperationClients() {
        return operationClients;
    }

    public void setOperationClients(RealmList<OperationClientRepository> operationClients) {
        this.operationClients = operationClients;
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
