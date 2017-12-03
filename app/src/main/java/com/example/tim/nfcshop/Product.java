package com.example.tim.nfcshop;

/**
 * Created by Gurt on 29.11.17.
 */

public class Product {

    int produktId;
    String nazov;
    double cena;
    int picture;

    public Product(String nazov, double cena, int picture) {
        this.nazov = nazov;
        this.cena = cena;
        this.picture = picture;
    }
    public Product(int produktId, String nazov, double cena, int picture) {
        this.picture = produktId;
        this.nazov = nazov;
        this.cena = cena;
        this.picture = picture;
    }

    public Product() {
    }

    public int getProduktId() {
        return produktId;
    }

    public void setProduktId(int produktId) {
        this.produktId = produktId;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
