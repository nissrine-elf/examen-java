package com.example.javaexamen;

import java.util.List;

public class Client {
    private int clientId;
    private String nom;
    private String prenom;
    private List<Commande> commandes;
    private String telephone;

    public Client(int clientId, String nom, String prenom, String telephone, List<Commande> commandes) {
        this.clientId = clientId;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.commandes = commandes;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", commandes=" + commandes +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
