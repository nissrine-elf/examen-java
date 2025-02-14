package com.example.javaexamen;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RepasDAO {
    private Connection connection;

    public RepasDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE - Ajouter un repas
    public boolean addRepas(Repas repas) {
        String query = "INSERT INTO repas (prix, plat_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setBigDecimal(1, repas.getPrix());
            stmt.setInt(2, repas.getPlatPrincipal().getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int repasId = generatedKeys.getInt(1);
                    repas.setRepasId(repasId);

                    // Ajouter les suppléments associés au repas
                    addSupplementsToRepas(repasId, repas.getSupplements());
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ajouter les suppléments à un repas
    private void addSupplementsToRepas(int repasId, List<Supplement> supplements) {
        String query = "INSERT INTO repas_supplement (repas_id, supplement_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Supplement supplement : supplements) {
                stmt.setInt(1, repasId);
                stmt.setInt(2, supplement.getSupplementId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ - Récupérer un repas par son ID
    public Repas getRepasById(int repasId) {
        String query = "SELECT * FROM repas WHERE repas_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, repasId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Repas repas = new Repas(
                        rs.getInt("repas_id"),
                        rs.getBigDecimal("prix"),
                        getPlatPrincipalById(rs.getInt("plat_id")),
                        getSupplementsForRepas(repasId)
                );
                return repas;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer le plat principal d'un repas par son ID
    private PlatPrincipal getPlatPrincipalById(int platPrincipalId) {
        String query = "SELECT * FROM platPrincipal WHERE platPrincipal_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, platPrincipalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new PlatPrincipal(
                        rs.getInt("plat_id"),
                        rs.getString("nom"),
                        rs.getBigDecimal("prix"),null,null
                );
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
                "JOIN repas_supplement rs ON s.supplement_id = rs.supplement_id " +
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

    // READ - Récupérer tous les repas
    public List<Repas> getAllRepas() {
        List<Repas> repasList = new ArrayList<>();
        String query = "SELECT * FROM repas";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

    // UPDATE - Mettre à jour un repas
    public boolean updateRepas(Repas repas) {
        String query = "UPDATE repas SET prix = ?, platPrincipal_id = ? WHERE repas_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBigDecimal(1, repas.getPrix());
            stmt.setInt(2, repas.getPlatPrincipal().getId());
            stmt.setInt(3, repas.getRepasId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Mettre à jour les suppléments du repas
                updateSupplementsForRepas(repas.getRepasId(), repas.getSupplements());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mettre à jour les suppléments associés à un repas
    private void updateSupplementsForRepas(int repasId, List<Supplement> supplements) {
        String deleteQuery = "DELETE FROM repas_supplement WHERE repas_id = ?";
        String insertQuery = "INSERT INTO repas_supplement (repas_id, supplement_id) VALUES (?, ?)";

        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            // Supprimer les anciens suppléments
            deleteStmt.setInt(1, repasId);
            deleteStmt.executeUpdate();

            // Ajouter les nouveaux suppléments
            for (Supplement supplement : supplements) {
                insertStmt.setInt(1, repasId);
                insertStmt.setInt(2, supplement.getSupplementId());
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE - Supprimer un repas
    public boolean deleteRepas(int repasId) {
        String query = "DELETE FROM repas WHERE repas_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, repasId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Supprimer les suppléments associés au repas
                deleteSupplementsForRepas(repasId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Supprimer les suppléments associés à un repas
    private void deleteSupplementsForRepas(int repasId) {
        String query = "DELETE FROM repas_supplement WHERE repas_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, repasId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Calculer le prix total du repas
    public BigDecimal calculerPrixTotal(Repas repas) {
        return repas.calculerPrixTotal();
    }
}
