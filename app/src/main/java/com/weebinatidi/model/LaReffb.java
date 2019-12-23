package com.weebinatidi.model;

/**
 * Created by mbp on 19/04/2017.
 */

public class LaReffb {
    private String laref;
    private String larefprice;
    private String larefqte;
    private String id;
    private String lienimg;
    private String dateen;
    private String idbdext;
    private String contexts;

    public LaReffb(){}

    public LaReffb(String laref, String larefqte) {
        this.laref=laref;
        this.larefqte=larefqte;
    }

    public LaReffb(String laref, String larefprice, String larefqte, String lid, String lienimg) {
        this.laref=laref;
        this.larefprice=larefprice;
        this.larefqte=larefqte;
        this.lienimg=lienimg;
        this.id=lid;
    }

    public LaReffb(String laref) {
        this.setLaref(laref);
    }

    public String getLaref() {
        return laref;
    }

    public void setLaref(String laref) {
        this.laref = laref;
    }

    public String getLarefprice() {
        return larefprice;
    }

    public void setLarefprice(String larefprice) {
        this.larefprice = larefprice;
    }

    public String getLarefqte() {
        return larefqte;
    }

    public void setLarefqte(String larefqte) {
        this.larefqte = larefqte;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLienimg() {
        return lienimg;
    }

    public void setLienimg(String lienimg) {
        this.lienimg = lienimg;
    }

    public String getDateen() {
        return dateen;
    }

    public void setDateen(String dateen) {
        this.dateen = dateen;
    }

    public String getIdbdext() {
        return idbdext;
    }

    public void setIdbdext(String idbdext) {
        this.idbdext = idbdext;
    }

    public String getContexts() {
        return contexts;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }


    @Override
    public String toString() {
        return "LaReffb{" +
                "laref='" + laref + '\'' +
                ", larefprice='" + larefprice + '\'' +
                ", larefqte='" + larefqte + '\'' +
                ", id='" + id + '\'' +
                ", lienimg='" + lienimg + '\'' +
                ", dateen='" + dateen + '\'' +
                ", idbdext='" + idbdext + '\'' +
                ", contexts='" + contexts + '\'' +
                '}';
    }
}