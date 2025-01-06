package com.example.javaexamen;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {
    private Connection connection;

    public CommandeDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE - Ajouter une commande
    public boolean addCommande(Commande commande) {
        String query = "INSERT INTO commande (clientId, dateCommande) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, commande.getClient().getClientId());
            stmt.setDate(2, Date.valueOf(commande.getDateCommande()));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int commandeId = generatedKeys.getInt(1);
                    commande.setCommandeId(commandeId);
                    // Ajouter les repas associés à la commande
                    addRepasToCommande(commandeId, commande.getRepas());
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ajouter des repas à une commande
    private void addRepasToCommande(int commandeId, List<Repas> repasList) {
        String query = "INSERT INTO CommandeRepas (commandeId, repasId) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Repas repas : repasList) {
                stmt.setInt(1, commandeId);
                stmt.setInt(2, repas.getRepasId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ - Récupérer une commande par son ID
    public Commande getCommandeById(int commandeId) {
        String query = "SELECT * FROM Commande WHERE commandeId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Commande commande = new Commande();
                commande.setCommandeId(rs.getInt("commandeId"));
                commande.setClientId(rs.getInt("clientId"));
                commande.setDateCommande(rs.getDate("dateCommande").toLocalDate());
                commande.setRepas(getRepasForCommande(commandeId));
                return commande;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer les repas associés à une commande
    private List<Repas> getRepasForCommande(int commandeId) {
        List<Repas> repasList = new ArrayList<>();
        String query = "SELECT r.* FROM Repas r "
                + "JOIN CommandeRepas cr ON r.repasId = cr.repasId "
                + "WHERE cr.commandeId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Repas repas = new Repas();
                repas.setRepasId(rs.getInt("repasId"));
                repas.setPlatPrincipalId(rs.getInt("platPrincipalId"));
                repasList.add(repas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return repasList;
    }

    // Calculer le total de la commande
    public BigDecimal calculerPrixTotal(Commande commande) {
        BigDecimal total = BigDecimal.ZERO;
        for (Repas repas : commande.getRepas()) {
            RepasDAO repasDAO = new RepasDAO(connection);
            total = total.add(repasDAO.calculerPrixTotal(repas));
        }
        return total;
    }
}
