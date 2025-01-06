package com.example.javaexamen;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Commande {
    private int commandeId;
    private Client client;
    private List<Repas> repas;
    private BigDecimal total;
    private Date datecommande;

    public Date getDateCommande() {
        return datecommande;
    }

    public void setDatecommande(Date datecommande) {
        this.datecommande = datecommande;
    }

    // Calcul du total de la commande
    public BigDecimal calculerTotal() {
        BigDecimal totalCommande = BigDecimal.ZERO;
        for (Repas repasItem : repas) {
            totalCommande = totalCommande.add(repasItem.calculerPrixTotal()); // Ajouter le prix de chaque repas
        }
        return totalCommande;
    }

    // Getters et Setters
    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Repas> getRepas() {
        return repas;
    }

    public void setRepas(List<Repas> repas) {
        this.repas = repas;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
