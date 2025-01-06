package com.example.javaexamen;

import java.math.BigDecimal;

public class Supplement {
    private int supplementId;
    private String nom;
    private String description;
    private BigDecimal prix;

    public Supplement(int supplementId, String nom, String description, BigDecimal prix) {
        this.supplementId = supplementId;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    public int getSupplementId() {
        return supplementId;
    }

    public void setSupplementId(int supplementId) {
        this.supplementId = supplementId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Supplement{" +
                "supplementId=" + supplementId +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                '}';
    }
}
