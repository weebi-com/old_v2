package com.weebinatidi.model;

/**
 * Created by macbookpro on 11/12/2017.
 */

public class ForgetSale {

    private int id;
    private int prix;
    private String dateAjout;

    public ForgetSale() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(String dateAjout) {
        this.dateAjout = dateAjout;
    }

    @Override
    public String toString() {
        return "ForgetSale{" +
                "id=" + id +
                ", prix='" + prix + '\'' +
                ", dateAjout='" + dateAjout + '\'' +
                '}';
    }
}
