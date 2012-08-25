<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd"> 
<%@ page import="java.sql.*" %> 
<%@ page import="java.io.*" %>
<% 
try {
String ip = "172.27.22.147";
String port = "3306";
String dbName = "DB";
String username = "root";
String password = "root";
/* Create string of connection url within specified format with machine name, port number and database name. Here machine name id localhost and database name is usermaster. */ 
String connectionURL = "jdbc:mysql://172.27.22.147:3306/DB";

// declare a connection by using Connection interface 
Connection connection = null; 

// Load JBBC driver "com.mysql.jdbc.Driver"
Class.forName("com.mysql.jdbc.Driver").newInstance(); 

/* Create a connection by using getConnection() method that takes parameters of string type connection url, user name and password to connect to database. */ 
connection = DriverManager.getConnection(connectionURL, username,password);
// check weather connection is established or not by isClosed() method 
if(!connection.isClosed())
{ 
out.println("Success");
connection.close();
}
else
{
  out.println("Closed");
}
}
catch(SQLException ex){
out.println(ex.getSQLState());
}
%>