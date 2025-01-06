package com.example.javaexamen;

import java.math.BigDecimal;

public class Ingredient {
    private int ingredientId;
    private String nom;
    private String description;
    private BigDecimal prix;

    public Ingredient(int ingredientId, String nom, String description, BigDecimal prix) {
        this.ingredientId = ingredientId;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
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
        return "Ingredient{" +
                "ingredientId=" + ingredientId +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                '}';
    }
}
