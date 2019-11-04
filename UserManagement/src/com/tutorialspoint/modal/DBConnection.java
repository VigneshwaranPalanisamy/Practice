package com.tutorialspoint.modal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	
	private static DBConnection instance;
    private Connection connection;
    private String url = "jdbc:sqlserver://localhost:1433;DatabaseName=Test;";
    private String username = "vignesh.palani";
    private String password = "vignesh@321";

    private DBConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(url, username, password);
            if(connection != null) {
            	System.out.println("Database Connection Creation Success!!!");
            }
        } catch (Exception ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DBConnection();
        }

        return instance;
    }
    
    public static void main(String args[]) {
		
    	try {
			DBConnection dbConnection = DBConnection.getInstance();
			Connection con = dbConnection.getConnection();
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from Users");
			while(resultSet.next()){
				System.out.println(resultSet.getString(1));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
	}

}
