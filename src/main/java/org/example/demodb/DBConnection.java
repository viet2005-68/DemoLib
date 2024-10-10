package org.example.demodb;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public Connection databaseLink;
    public Connection getConnection() {
        String databaseName = "book";
        String databaseUser = "root";
        String databasePassword = "16102005";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser,databasePassword);
        } catch (Exception e) {
            System.out.println("Not Success");
            e.printStackTrace();
        }
        return databaseLink;
    }
}
