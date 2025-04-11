package fr.fisa.webservicedemo.model;

public class BodyMessage {

    private String nom;

    public BodyMessage(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
