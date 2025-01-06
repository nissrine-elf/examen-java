package com.example.javaexamen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO {

    private Connection connection;

    // Constructeur
    public IngredientDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE - Ajouter un ingrédient
    public boolean addIngredient(Ingredient ingredient) {
        String query = "INSERT INTO ingredient (nom, description, prix) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, ingredient.getNom());
            stmt.setString(2, ingredient.getDescription());
            stmt.setBigDecimal(3, ingredient.getPrix());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - Récupérer un ingrédient par son ID
    public Ingredient getIngredientById(int ingredientId) {
        String query = "SELECT * FROM ingredient WHERE ingredient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ingredientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Ingredient ingredient = new Ingredient(rs.getInt("ingredient_id"),rs.getString("nom"),rs.getString("description"),rs.getBigDecimal("prix"));

                return ingredient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - Récupérer tous les ingrédients
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        String query = "SELECT * FROM ingredient";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Ingredient ingredient = new Ingredient(rs.getInt("ingredient_id"),rs.getString("nom"),rs.getString("description"),rs.getBigDecimal("prix"));

                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    // UPDATE - Mettre à jour un ingrédient
    public boolean updateIngredient(Ingredient ingredient) {
        String query = "UPDATE ingredient SET nom = ?, description = ?, prix = ? WHERE ingredient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, ingredient.getNom());
            stmt.setString(2, ingredient.getDescription());
            stmt.setBigDecimal(3, ingredient.getPrix());
            stmt.setInt(4, ingredient.getIngredientId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Supprimer un ingrédient
    public boolean deleteIngredient(int ingredientId) {
        String query = "DELETE FROM ingredient WHERE ingredient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ingredientId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Associer un ingrédient à un plat avec une quantité spécifique
    public boolean addIngredientToPlat(int platId, int ingredientId, double quantity) {
        String query = "INSERT INTO plat_ingredient (plat_id, ingredient_d, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, platId);
            stmt.setInt(2, ingredientId);
            stmt.setDouble(3, quantity);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtenir les ingrédients pour un plat particulier
    public List<Ingredient> getIngredientsForPlat(int platId) {
        List<Ingredient> ingredients = new ArrayList<>();
        String query = "SELECT i.* FROM ingredient i "
                + "JOIN plat_ingredient pi ON i.ingredient_id = pi.ingredient_id "
                + "WHERE pi.plat_d = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, platId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = new Ingredient(rs.getInt("ingredient_id"),rs.getString("nom"),rs.getString("description"),rs.getBigDecimal("prix"));

                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    // Obtenir la quantité d'un ingrédient pour un plat particulier
    public Double getIngredientQuantityForPlat(int platId, int ingredientId) {
        String query = "SELECT quantity FROM plat_ingredient WHERE plat_id = ? AND ingredient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, platId);
            stmt.setInt(2, ingredientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("quantite");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
