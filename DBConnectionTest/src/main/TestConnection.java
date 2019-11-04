package main;

import java.io.FileReader;
import java.io.Serializable;
import java.sql.*;
import java.util.Map.Entry;
import java.util.Properties;

public class TestConnection implements Serializable{

	private static int a = Integer.MAX_VALUE;
	transient static String value= "This is a string";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Connection con = null;
         //String conUrl = "jdbc:sqlserver://localhost\\Test:1433;Database=Test;";
         //String conURL = "jdbc:sqlserver://localhost:1433;database=Test;";
	   try {
        // ...
		FileReader file = new FileReader("src\\resources\\db.properties");
		Properties p=new Properties();
		p.load(file);
		//System.out.println(value);
		//System.out.println(a);
		
//		for(Entry<Object, Object> prop : p.entrySet()) {
//			System.out.println(prop.getKey()+" - "+prop.getValue());
//		}
	    con = DriverManager.getConnection(p.getProperty("conUrl")+"database="+p.getProperty("database")+";",p.getProperty("username"),p.getProperty("password"));
	    Statement statement = con.createStatement();
		
	    ResultSet resultSet = statement.executeQuery("select * from "+p.getProperty("table"));
	    while(resultSet.next()){
	    	System.out.println(resultSet.getString(1));
	    }
	    p.setProperty("database", "Sample");
	    p.setProperty("table", "ToDoList");
	    con = DriverManager.getConnection(p.getProperty("conUrl")+"database="+p.getProperty("database")+";",p.getProperty("username"),p.getProperty("password"));
	    statement = con.createStatement();
		
	    resultSet = statement.executeQuery("select * from "+p.getProperty("table"));
	    while(resultSet.next()){
	    	System.out.println(resultSet.getString(2));
	    }
	    if(con != null) {
	    	System.out.println("Success !!!");
	    	//System.out.println(con.getClientInfo());
	    }
        con.close();
 	  } catch (Exception e) { e.printStackTrace(); }
           finally {
             if (con != null) try { con.close(); } catch(Exception e) {}
           }
	}

}
