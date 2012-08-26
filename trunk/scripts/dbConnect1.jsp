<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd"> 
<%@ page import="java.sql.*" %> 
<%@ page import="java.io.*" %>
<
String ip = "172.27.22.147";
String port = "3306";
String dbName = "DB";
String username = "root";
String password = "root";
/* Create string of connection url within specified format with machine name, port number and database name. Here machine name id localhost and database name is usermaster. */ 
String connectionURL = "jdbc:mysql://172.22.27.147:3306/DB";

// declare a connection by using Connection interface 
Connection connection = null; 

// Load JBBC driver "com.mysql.jdbc.Driver"
Class.forName("com.mysql.jdbc.Driver").newInstance(); 
%>
<script type="text/javascript">
alert("hi1");
</script>
<%
connection = DriverManager.getConnection(connectionURL, username,password);
%>
<script type="text/javascript">
alert("hi2");
</script>