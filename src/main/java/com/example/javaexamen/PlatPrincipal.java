package com.example.javaexamen;

import java.math.BigDecimal;
import java.util.List;

public class PlatPrincipal {
    private int id;
    private String nom;

    private BigDecimal prix;
     // Prix du plat principal
    private List<Ingredient> ingredients; // Liste des ingrédients dans ce plat
    private List<Supplement> supplements;

    public PlatPrincipal(int id, String nom, BigDecimal prix, List<Ingredient> ingredients, List<Supplement> supplements) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.ingredients = ingredients;
        this.supplements = supplements;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Supplement> getSupplements() {
        return supplements;
    }

    public void setSupplements(List<Supplement> supplements) {
        this.supplements = supplements;

    }
    public BigDecimal calculerPrix() {
        BigDecimal prixTotal = prix;
        for (Supplement supplement : supplements) {
            prixTotal = prixTotal.add(supplement.getPrix());
        }
        return prixTotal;
    }

    @Override
    public String toString() {
        return "PlatPrincipal{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", ingredients=" + ingredients +
                ", supplements=" + supplements +
                '}';
    }
}
