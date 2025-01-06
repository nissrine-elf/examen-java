package com.example.javaexamen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void AddClient(Client c){
        Connection con=SignletonConnexionDB.getCon();
        try{//int clientId, String nom, String prenom, String telephone)
            PreparedStatement pstm=con.prepareStatement("INSERT INTO client (nom, prenom, telephone) VALUES (?,?,?)");
            pstm.setString(1, c.getNom());
            pstm.setString(2, c.getPrenom());
            pstm.setString(3, c.getTelephone());

            pstm.executeUpdate();
        }catch (SQLException e){e.printStackTrace();}

    }
    public List<Client> listAllclien(){
        List<Client> lp=new ArrayList<>();
        Connection connection=SignletonConnexionDB.getCon();
        try{
            PreparedStatement pts=connection.prepareStatement("select * from client");
            ResultSet rs=pts.executeQuery();

            while (rs.next()){

                Client c=new Client(rs.getInt("client_id"),rs.getString("nom"),rs.getString("prenom"),rs.getString("telephone"),null);
                lp.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lp;
    }
    public void deletCl(int o){
        Connection con=SignletonConnexionDB.getCon();
        try{
            PreparedStatement pstm1=con.prepareStatement("select * from commande where id_cleint=?");
            pstm1.setInt(1,o);
            ResultSet rs1=pstm1.executeQuery();
            if(rs1.next()){
                PreparedStatement pstm2=con.prepareStatement("update commande set  id_client =  NULL WHERE id_claint = ?");
                pstm2.setInt(1, o);
                pstm2.executeUpdate();
            }
            PreparedStatement pstm=con.prepareStatement(" delete from client WHERE client_id = ?");
            pstm.setInt(1, o);
            pstm.executeUpdate();
        }catch (SQLException e){e.printStackTrace();}
    }
    public void update(int o,Client c){
        Connection con=SignletonConnexionDB.getCon();
        try{

                PreparedStatement pstm2=con.prepareStatement("update client  set  nom = ? , prenom=? , telephone=?   WHERE client_id = ?");
                pstm2.setString(1, c.getNom());
            pstm2.setString(2, c.getPrenom());
            pstm2.setString(3, c.getTelephone());
            pstm2.setInt(4, o);

                pstm2.executeUpdate();
        }catch (SQLException e){e.printStackTrace();}
    }

}
