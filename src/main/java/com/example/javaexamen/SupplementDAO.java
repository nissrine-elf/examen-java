package com.example.javaexamen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class SupplementDAO {
    private Connection connection;

    public SupplementDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE - Ajouter un supplément
    public boolean addSupplement(Supplement supplement) {
        String query = "INSERT INTO supplement (nom, description, prix) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, supplement.getNom());
            stmt.setString(2, supplement.getDescription());
            stmt.setBigDecimal(3, supplement.getPrix());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - Récupérer un supplément par son ID
    public Supplement getSupplementById(int supplementId) {
        String query = "SELECT * FROM supplement WHERE supplement_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, supplementId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Supplement supplement = new Supplement(rs.getInt("supplement_id"),rs.getString("nom"),rs.getString("description"),rs.getBigDecimal("prix"));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - Récupérer tous les suppléments
    public List<Supplement> getAllSupplements() {
        List<Supplement> supplements = new ArrayList<>();
        String query = "SELECT * FROM supplement";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Supplement supplement = new Supplement(rs.getInt("supplement_id"),rs.getString("nom"),rs.getString("description"),rs.getBigDecimal("prix"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplements;
    }

    // UPDATE - Mettre à jour un supplément
    public boolean updateSupplement(Supplement supplement) {
        String query = "UPDATE supplement SET nom = ?, description = ?, prix = ? WHERE supplement_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, supplement.getNom());
            stmt.setString(2, supplement.getDescription());
            stmt.setBigDecimal(3, supplement.getPrix());
            stmt.setInt(4, supplement.getSupplementId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Supprimer un supplément
    public boolean deleteSupplement(int supplementId) {
        String query = "DELETE FROM supplement WHERE supplement_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, supplementId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Ajouter un supplément à un repas via la table de jointure RepasSupplement
    public boolean addSupplementToRepas(int repasId, int supplementId) {
        String query = "INSERT INTO repas_supplement (repas_id, supplement_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, repasId);
            stmt.setInt(2, supplementId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupérer les suppléments associés à un repas
    public List<Supplement> getSupplementsForRepas(int repasId) {
        List<Supplement> supplements = new ArrayList<>();
        String query = "SELECT s.* FROM supplement s "
                + "JOIN repas_supplement rs ON s.supplement_id = rs.supplement_id "
                + "WHERE rs.repas_id = ?";
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
}
