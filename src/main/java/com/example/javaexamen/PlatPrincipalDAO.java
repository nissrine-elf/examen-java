package com.example.javaexamen;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatPrincipalDAO {

    private Connection connection;

    public PlatPrincipalDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE - Ajouter un plat principal
    public boolean addPlatPrincipal(PlatPrincipal plat) {
        String query = "INSERT INTO platprincipal (nom, prix) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, plat.getNom());
            stmt.setBigDecimal(2, plat.getPrix());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public PlatPrincipal getPlatPrincipalById(int platId) {
        String query = "SELECT * FROM platprincipal WHERE plat_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, platId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                PlatPrincipal plat = new PlatPrincipal(rs.getInt("plat_id"),rs.getString("nom"),rs.getBigDecimal("prix"),null,null);

                return plat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - Récupérer tous les plats principaux
    public List<PlatPrincipal> getAllPlatPrincipaux() {
        List<PlatPrincipal> plats = new ArrayList<>();
        String query = "SELECT * FROM platprincipal";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                PlatPrincipal plat = new PlatPrincipal(rs.getInt("plat_id"),rs.getString("nom"),rs.getBigDecimal("prix"),null,null);
                plats.add(plat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plats;
    }


    public void updatePlatPrincipal(int p ,PlatPrincipal plat) {
        Connection con=SignletonConnexionDB.getCon();
        try{

            PreparedStatement pstm2=con.prepareStatement("update platprincipal  set  nom = ? , prix=?   WHERE plat_id = ?");
            pstm2.setString(1, plat.getNom());
            pstm2.setBigDecimal(2, plat.getPrix());
            pstm2.setInt(4, p);

            pstm2.executeUpdate();
        }catch (SQLException e){e.printStackTrace();}


    }
    public void deletplat(int o){
        Connection con=SignletonConnexionDB.getCon();
        try{



            PreparedStatement pstm=con.prepareStatement(" delete from platprincipal WHERE plat_id = ?");
            pstm.setInt(1, o);
            pstm.executeUpdate();
        }catch (SQLException e){e.printStackTrace();}
    }


    public boolean addIngredientToPlat(int platId, int ingredientId, double quantity) {
        String query = "INSERT INTO plat_ingredient (plat_id, ingredient_id, quantite) VALUES (?, ?, ?)";
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




    // Récupérer les ingrédients d'un plat
    public List<Ingredient> getIngredientsForPlat(int platId) {
        List<Ingredient> ingredients = new ArrayList<>();
        String query = "SELECT i.* FROM Ingredient i "
                + "JOIN plat_ingredient pi ON i.ingredient_id = pi.ingredient_id "
                + "WHERE pi.plat_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, platId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = new Ingredient(rs.getInt("ingredient_id"),rs.getString("nom"),rs.getString("description"),rs.getBigDecimal("prix"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }








    }




