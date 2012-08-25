package main;

import java.sql.Connection;
import java.sql.DriverManager;

public class Process {
	public static void main(String[] args) {
		  System.out.println("MySQL Connect Example.");
		  Connection conn = null;
		  String url = "jdbc:mysql://172.27.22.18/";
		  String dbName = "MultimodalTransport";		  
		  String userName = "root"; 
		  String password = "";
		  try {
			  Class.forName("com.mysql.jdbc.Driver");			  
			  //conn = DriverManager.getConnection(url+dbName,userName,password);
			  conn = DriverManager.getConnection("jdbc:mysql://172.27.22.18/MultimodalTransport?"
		              + "user=root&password=");
			  System.out.println("Connected to the database");
			  conn.close();
			  System.out.println("Disconnected from database");
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	}	
}