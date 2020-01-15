package com.example.sih.Models;

public class ProductModel {
    public String proname;
    public String proprice;
    public String maxquantity;
    public String proddate;
    public String about;
    public String extra;
    public String cat;

    public ProductModel() {}

    public ProductModel(String proname, String proprice, String proddate) {
        this.proname = proname;
        this.proprice = proprice;
        this.proddate = proddate;
    }

    public ProductModel(String proname, String proprice, String maxquantity, String proddate, String about, String extra, String cat) {
        this.proname = proname;
        this.proprice = proprice;
        this.maxquantity = maxquantity;
        this.proddate = proddate;
        this.about = about;
        this.extra = extra;
        this.cat = cat;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getProprice() {
        return proprice;
    }

    public void setProprice(String proprice) {
        this.proprice = proprice;
    }

    public String getMaxquantity() {
        return maxquantity;
    }

    public void setMaxquantity(String maxquantity) {
        this.maxquantity = maxquantity;
    }

    public String getProddate() {
        return proddate;
    }

    public void setProddate(String proddate) {
        this.proddate = proddate;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }
}
