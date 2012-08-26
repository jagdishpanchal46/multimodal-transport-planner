<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd"> 
<%@ page import="java.sql.*" %> 
<%@ page import="java.io.*" %>
<%@ page import="main.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
String srcName;
String destName;
String date;
List<JSONObject> json;
String ip = "172.27.22.147";

srcName = request.getParameter("srcName");
destName = request.getParameter("destName");
date = request.getParameter("date");
json = HackU.process(srcName,destName,date,date,ip);
JSONArray array = new JSONArray();
for(int i=0;i<json.size();i++)
{
  JSONObject obj = json.get(i);
  array.put(obj);
}
out.println(array);
%>