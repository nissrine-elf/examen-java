package com.example.javaexamen;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandeDAO {
    private Connection connection;

    public CommandeDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE - Ajouter une commande
    public boolean addCommande(Commande commande) {
        String query = "INSERT INTO commande (client_id, date_commande, total) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, commande.getClient().getClientId());
            stmt.setDate(2, new java.sql.Date(commande.getDateCommande().getTime()));
            stmt.setBigDecimal(3, commande.calculerTotal());
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

    // Ajouter les repas associés à une commande
    private void addRepasToCommande(int commandeId, List<Repas> repas) {
        String query = "INSERT INTO CommandeRepas (commandeId, repasId) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Repas repasItem : repas) {
                stmt.setInt(1, commandeId);
                stmt.setInt(2, repasItem.getRepasId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ - Récupérer une commande par son ID
    public Commande getCommandeById(int commandeId) {
        String query = "SELECT * FROM commande WHERE commande_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Commande commande = new Commande();
                commande.setCommandeId(rs.getInt("commande_id"));
                commande.setClient(getClientById(rs.getInt("client_id")));
                commande.setDatecommande(rs.getDate("date_commande"));
                commande.setRepas(getRepasForCommande(commandeId));
                commande.setTotal(commande.calculerTotal());
                return commande;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer un client par son ID
    private Client getClientById(int clientId) {
        String query = "SELECT * FROM client WHERE client_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Client client = new Client(rs.getInt("client_id"),rs.getString("nom"),rs.getString("prenom"),rs.getString("telephone"),null);


                // Ajouter d'autres propriétés ici
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer les repas associés à une commande
    private List<Repas> getRepasForCommande(int commandeId) {
        List<Repas> repasList = new ArrayList<>();
        String query = "SELECT r.* FROM repas r " +
                "JOIN commande_repas cr ON r.repasId = cr.repas_id " +
                "WHERE cr.commande_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Repas repas = new Repas(
                        rs.getInt("repas_id"),
                        rs.getBigDecimal("prix"),
                        getPlatPrincipalById(rs.getInt("platPrincipal_id")),
                        getSupplementsForRepas(rs.getInt("repas_id"))
                );
                repasList.add(repas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return repasList;
    }

    // Récupérer le plat principal d'un repas
    private PlatPrincipal getPlatPrincipalById(int platPrincipalId) {
        String query = "SELECT * FROM platPrincipal WHERE platPrincipal_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, platPrincipalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new PlatPrincipal(rs.getInt("plat_id"),rs.getString("nom"),rs.getBigDecimal("prix"),null,null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer les suppléments associés à un repas
    private List<Supplement> getSupplementsForRepas(int repasId) {
        List<Supplement> supplements = new ArrayList<>();
        String query = "SELECT s.* FROM supplement s " +
                "JOIN RepasSupplement rs ON s.supplementId = rs.supplement_id " +
                "WHERE rs.repas_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, repasId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Supplement supplement = new Supplement(rs.getInt("supplement_id"),rs.getString("nom"),rs.getString("description"),rs.getBigDecimal("prix"));

                supplements.add(supplement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplements;
    }

    // READ - Récupérer toutes les commandes
    public List<Commande> getAllCommandes() {
        List<Commande> commandesList = new ArrayList<>();
        String query = "SELECT * FROM commande";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setCommandeId(rs.getInt("commande_id"));
                commande.setClient(getClientById(rs.getInt("client_id")));
                commande.setDatecommande(rs.getDate("date-commande"));
                commande.setRepas(getRepasForCommande(rs.getInt("commande_id")));
                commande.setTotal(commande.calculerTotal());
                commandesList.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandesList;
    }

    // UPDATE - Mettre à jour une commande
    public boolean updateCommande(Commande commande) {
        String query = "UPDATE Commande SET clientId = ?, datecommande = ?, total = ? WHERE commandeId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, commande.getClient().getClientId());
            stmt.setDate(2, new java.sql.Date(commande.getDateCommande().getTime()));
            stmt.setBigDecimal(3, commande.calculerTotal());
            stmt.setInt(4, commande.getCommandeId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Mettre à jour les repas associés à la commande
                updateRepasForCommande(commande.getCommandeId(), commande.getRepas());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mettre à jour les repas associés à une commande
    private void updateRepasForCommande(int commandeId, List<Repas> repas) {
        String deleteQuery = "DELETE FROM CommandeRepas WHERE commandeId = ?";
        String insertQuery = "INSERT INTO CommandeRepas (commandeId, repasId) VALUES (?, ?)";

        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            // Supprimer les anciens repas associés à la commande
            deleteStmt.setInt(1, commandeId);
            deleteStmt.executeUpdate();

            // Ajouter les nouveaux repas associés à la commande
            for (Repas repasItem : repas) {
                insertStmt.setInt(1, commandeId);
                insertStmt.setInt(2, repasItem.getRepasId());
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE - Supprimer une commande
    public boolean deleteCommande(int commandeId) {
        String query = "DELETE FROM Commande WHERE commandeId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, commandeId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Supprimer les repas associés à la commande
                deleteRepasForCommande(commandeId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Supprimer les repas associés à une commande
    private void deleteRepasForCommande(int commandeId) {
        String query = "DELETE FROM CommandeRepas WHERE commandeId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, commandeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
