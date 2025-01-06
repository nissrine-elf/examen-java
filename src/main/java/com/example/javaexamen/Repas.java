package com.example.javaexamen;

import java.math.BigDecimal;
import java.util.List;

public class Repas {
    private int repasId;
    private PlatPrincipal platPrincipal;
    private List<Supplement> supplements;
    private BigDecimal prix;

    public Repas(int repasId, BigDecimal prix, PlatPrincipal platPrincipal, List<Supplement> supplements) {
        this.repasId = repasId;
        this.prix = prix;
        this.platPrincipal = platPrincipal;
        this.supplements = supplements;
    }

    public int getRepasId() {
        return repasId;
    }

    public void setRepasId(int repasId) {
        this.repasId = repasId;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public PlatPrincipal getPlatPrincipal() {
        return platPrincipal;
    }

    public void setPlatPrincipal(PlatPrincipal platPrincipal) {
        this.platPrincipal = platPrincipal;
    }

    public List<Supplement> getSupplements() {
        return supplements;
    }

    public void setSupplements(List<Supplement> supplements) {
        this.supplements = supplements;
    }
    public BigDecimal calculerPrixTotal() {
        BigDecimal total = platPrincipal.getPrix(); // Prix du plat principal
        for (Supplement supplement : supplements) {
            total = total.add(supplement.getPrix()); // Ajouter le prix des suppléments
        }
        return total;
    }
    @Override
    public String toString() {
        return "Repas{" +
                "repasId=" + repasId +
                ", platPrincipal=" + platPrincipal +
                ", supplements=" + supplements +
                ", prix=" + prix +
                '}';
    }
}
