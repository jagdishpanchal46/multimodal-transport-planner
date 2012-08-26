<%@ page import="java.sql.*" %> 
<%@ page import="java.io.*" %>

<%@ page import="org.json.simple.*" %>
<% 
String ip = "172.27.22.147";
String port = "3306";
String dbName = "DB";
String username = "root";
String password = "root";
/* Create string of connection url within specified format with machine name, port number and database name. Here machine name id localhost and database name is usermaster. */ 
String connectionURL = "jdbc:mysql://172.27.22.147:3306/DB";

// declare a connection by using Connection interface 
Connection connection = null; 
try
{
// Load JBBC driver "com.mysql.jdbc.Driver"
Class.forName("com.mysql.jdbc.Driver").newInstance(); 

/* Create a connection by using getConnection() method that takes parameters of string type connection url, user name and password to connect to database. */ 
connection = DriverManager.getConnection(connectionURL, username,password);
// check weather connection is established or not by isClosed() method 
}
catch(SQLException ex){
out.println(ex.getSQLState());
}
String str = request.getParameter("str");

// Queries
Statement statement = connection.createStatement();
ResultSet rs = null;
ResultSetMetaData rsmd = null;

String query = "SELECT * FROM AIRPORTS WHERE CITY_NAME LIKE \'"+str+"%\'";
rs = statement.executeQuery(query);
rsmd = rs.getMetaData();

JSONArray json = new JSONArray();
JSONArray json1 = new JSONArray();
JSONArray json2 = new JSONArray();
while(rs.next()) {
  int numColumns = rsmd.getColumnCount();
  JSONObject obj = new JSONObject();

  for (int i=1; i<numColumns+1; i++) {
    String column_name = rsmd.getColumnName(i);

    if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
      obj.put(column_name, rs.getArray(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
      obj.put(column_name, rs.getInt(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
      obj.put(column_name, rs.getBoolean(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
      obj.put(column_name, rs.getBlob(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
      obj.put(column_name, rs.getDouble(column_name)); 
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
      obj.put(column_name, rs.getFloat(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
      obj.put(column_name, rs.getInt(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
      obj.put(column_name, rs.getNString(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
      obj.put(column_name, rs.getString(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
      obj.put(column_name, rs.getInt(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
      obj.put(column_name, rs.getInt(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
      obj.put(column_name, rs.getDate(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
    obj.put(column_name, rs.getTimestamp(column_name));   
    }
    else{
      obj.put(column_name, rs.getObject(column_name));
    }
  }
  json1.add(obj);
}

// Trains
query = "SELECT * FROM TRAINS WHERE name LIKE \'"+str+"%\'";
rs = statement.executeQuery(query);
rsmd = rs.getMetaData();

while(rs.next()) {
  int numColumns = rsmd.getColumnCount();
  JSONObject obj = new JSONObject();

  for (int i=1; i<numColumns+1; i++) {
    String column_name = rsmd.getColumnName(i);

    if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
      obj.put(column_name, rs.getArray(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
      obj.put(column_name, rs.getInt(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
      obj.put(column_name, rs.getBoolean(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
      obj.put(column_name, rs.getBlob(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
      obj.put(column_name, rs.getDouble(column_name)); 
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
      obj.put(column_name, rs.getFloat(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
      obj.put(column_name, rs.getInt(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
      obj.put(column_name, rs.getNString(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
      obj.put(column_name, rs.getString(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
      obj.put(column_name, rs.getInt(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
      obj.put(column_name, rs.getInt(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
      obj.put(column_name, rs.getDate(column_name));
    }
    else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
    obj.put(column_name, rs.getTimestamp(column_name));   
    }
    else{
      obj.put(column_name, rs.getObject(column_name));
    }
  }
  json2.add(obj);
}
json.add(json1);
json.add(json2);
json.writeJSONString(out); 
%>