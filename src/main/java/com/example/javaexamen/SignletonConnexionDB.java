package com.example.javaexamen;

import java.sql.Connection;
import java.sql.DriverManager;

public class SignletonConnexionDB {
    private static Connection con;
    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/java_examen","root","");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getCon(){
        return con;
    }
}
