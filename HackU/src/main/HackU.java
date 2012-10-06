package main;
import flight.*;
import rail.*;
import utils.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HackU {
	// Global variables
	
	public static String allAirportsFile = "data/AirportDatabase.dat";
	public static String majorAirportsFile = "data/MajorAirportDatabase.dat";
	public static String allStationsFile = "data/stations.json";
	//public static String majorStationsFile = "data/majorStations.csv";
	
	public static List<Airport> allAirports = new ArrayList<Airport>();
	public static List<Airport> majorAirports = new ArrayList<Airport>();
	
	public static List<Station> allStations = new ArrayList<Station>();
	//public static List<Station> majorStations = new ArrayList<Station>();
	
	public static String url = "jdbc:mysql://172.27.22.149:3306/DB";
	
	public static void init(){
		//fillListsAirport(allAirportsFile, allAirports);
		fillListsAirport("AIRPORTS", allAirports);
		//fillListsAirport(majorAirportsFile, majorAirports);
		fillListsAirport("MAJOR_AIRPORTS", majorAirports);
		fillListsStation(allStationsFile, allStations);
		//fillListsStation(majorStationsFile, majorStations);
	}

	public static void fillListsAirport(String TABLE, List<Airport> arrayList){
		//System.out.println("MySQL Connect Example.");
		Connection conn = null;
		//String url = "jdbc:mysql://172.27.22.147:3306/DB";		
		String dbName = "DB";		  
		String userName = "root"; 
		String password = "root";
		try {
			Class.forName("com.mysql.jdbc.Driver");			  
			//conn = DriverManager.getConnection(url+dbName,userName,password);
			conn = DriverManager.getConnection(url, userName, password);
			//System.out.println("Connected to the database");
			
			Statement statement = conn.createStatement();
			String sql = "SELECT * FROM "+TABLE;
			ResultSet rs = statement.executeQuery(sql); 
			while(rs.next()){
				arrayList.add(new Airport(rs));
			}
			statement.close();
			conn.close();
			//System.out.println("Disconnected from database");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	public static void fillListsAirport(String file, List<Airport> arrayList){
		//this code fills up lists for airports, railways etc
		FileInputStream fstreamIn;
		try {
			fstreamIn = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstreamIn);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));							
									
			String line;
			try {
				while( (line=br.readLine()) != null ){					
					arrayList.add(new Airport(line.trim()));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//System.err.println("Domain when exception happened is "+domain);
			}		
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	**/

	public static void fillListsStation(String file, List<Station> arrayList){
		//System.out.println("MySQL Connect Example.");
		Connection conn = null;
		//String url = "jdbc:mysql://172.27.22.147:3306/DB";
		String dbName = "DB";		  
		String userName = "root"; 
		String password = "root";
		try {
			Class.forName("com.mysql.jdbc.Driver");			  
			//conn = DriverManager.getConnection(url+dbName,userName,password);
			conn = DriverManager.getConnection(url, userName, password);
			//System.out.println("Connected to the database");
			
			Statement statement = conn.createStatement();
			String sql = "SELECT * FROM TRAINS";
			ResultSet rs = statement.executeQuery(sql); 
			while(rs.next()){
				arrayList.add(new Station(rs));
			}
			statement.close();
			conn.close();
			//System.out.println("Disconnected from database");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	public static void fillListsStation(String file, List<Station> arrayList){
		//this code fills up lists for airports, railways etc
		String jsonStr = "";
		FileInputStream fstreamIn;
		try {
			fstreamIn = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstreamIn);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));							
									
			String line;
			try {
				while( (line=br.readLine()) != null ){
					//System.err.println(line);
					jsonStr += line.trim();				
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//System.err.println("Domain when exception happened is "+domain);
			}		
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
				
		try {			
			//System.out.println(jsonStr.substring(0,100));
			JSONArray jsonArray = new JSONArray(jsonStr);			
			//JSONObject json = new JSONObject(jsonStr);
			for(int i=0; i<jsonArray.length(); i++){	
				System.err.println(jsonArray.getJSONObject(i).toString());
				arrayList.add(new Station(jsonArray.getJSONObject(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.err.println(jsonStr);
		}
	}
	 * @throws JSONException 
	**/
	
	public static void main(String args[]) throws InterruptedException, JSONException{
		//System.out.println(process("Allahabad", "Kanpur", "20120920", "20120922", "jdbc:mysql://172.27.22.147:3306/DB"));
		System.out.println(process("Allahabad", "Kanpur", "20120920", "20120922", "jdbc:mysql://localhost:3306/DB"));
	}
	
	public static List<JSONObject> process(String src, String dest, String time1, String time2, String ip) throws JSONException{
	//public static String process(String src, String dest, String time1, String time2) throws JSONException{
		url = ip;
		init();
		//TODO take these inputs from a json file
		//String srcName = "Vadodara";
		String srcName = src;
		//String destName = "Jhansi";
		String destName = dest;
		//String startDate = "20120827";
		String startDate = time1;
		//String endDate = "20120829";
		String endDate = time2;
				
		Airport srcAirport = findAirport(srcName);
		//if(srcAirport != null)	srcAirport.tostring();
		
		Airport destAirport = findAirport(destName);
		//if(destAirport != null)	destAirport.tostring();
		
		Station srcStation = findStation(srcName);
		//if(srcStation != null)	srcStation.tostring();
		
		Station destStation = findStation(destName);			
		//if(destStation != null)	destStation.tostring();
		
		List<JSONObject> results = Collections.synchronizedList(new ArrayList<JSONObject>());
		
		//System.err.println("Results1 : "+results.size());
		
		if(srcAirport != null && destAirport != null){
			//Result result = createResultFlight();
			JSONObject ret = FlightsBetweenStations.flightsBetweenStations(srcAirport.code1, destAirport.code1, startDate, endDate);
			ret = ret.getJSONObject("calendar_json");
			
			JSONArray retArray = ret.getJSONArray(startDate);
			for(int i=0; i<retArray.length(); i++){
				JSONObject retval = retArray.getJSONObject(i);
				JSONObject temp = new JSONObject();				
				temp.put("type", "0");
				temp.put("aln", retval.getString("aln"));
				temp.put("from", srcName+"("+srcAirport.code1+")");
				temp.put("to", destName+"("+destAirport.code1+")");
				temp.put("dt", retval.getString("dt"));			
				temp.put("at", retval.getString("at"));
				temp.put("ad", retval.getString("ad"));
				temp.put("dd", startDate);
				temp.put("pr", retval.getString("pr"));
				//System.out.println(temp.toString());
				results.add(temp);
			}
		}
				
		//System.err.println("Results2 : "+results.size());
		
		if(srcStation != null && destStation != null){
			JSONObject ret = Utils.createResultTrain(TrainsBetweenStations.trainsBetweenStations(srcStation.code, destStation.code, "SL", startDate),startDate);
			ret = ret.getJSONObject("calendar_json");			
			JSONArray retArray = ret.getJSONArray(startDate);
			
			for(int i=0; i<retArray.length(); i++){
				JSONObject retval = retArray.getJSONObject(i);
				JSONObject temp = new JSONObject();				
				temp.put("type", "1");
				temp.put("from", srcName+"("+srcStation.code+")");
				temp.put("to", destName+"("+destStation.code+")");
				temp.put("aln", retval.getString("aln"));
				temp.put("dt", retval.getString("dt"));			
				temp.put("at", retval.getString("at"));
				temp.put("ad", retval.getString("ad"));
				temp.put("dd", startDate);
				temp.put("pr", retval.getString("pr"));
				//System.out.println(temp.toString());
				results.add(temp);
			}			
			//System.err.println(result.toString());			
		}
		
		//System.err.println("Results3 : "+results.size());
		
		if(srcAirport != null && destStation != null){
			/**
			 * Setting up multiple threads
			 */				
			int maxNumberOfCrawlers = 10;
			int coreNumberOfCrawlers = maxNumberOfCrawlers;
			int maxJobs = (int) (maxNumberOfCrawlers*1.5);
			int idleTime = 10; // s
			BlockingQueue<Runnable> blockingJobQueue = new ArrayBlockingQueue<Runnable>(maxJobs);
			ThreadPoolExecutor crawlerThreadPool = new ThreadPoolExecutor(
												coreNumberOfCrawlers,
												maxNumberOfCrawlers,
												idleTime,
												TimeUnit.SECONDS,
												blockingJobQueue);

			long sleepDuration = 20; // ms							
			boolean wasRefused = false;

			try {			
				//Iterator<Airport> it = allAirports.iterator();
				Iterator<Airport> it = majorAirports.iterator();
				Airport currentAirport = null;
				Station currentStation = null;
				while(it.hasNext()){
					
					if(!wasRefused){
						currentAirport = it.next();
						if(currentAirport.code1 == srcAirport.code1)	continue;
						currentStation = findStation(currentAirport.city);
						if(currentStation == null)	continue;
					}
					
					try {
						// schedule when to spawn a new crawler thread
						Thread.sleep(25);	//ms
						
						// spawn a crawler 
						//crawlerThreadPool.execute(new UserCrawler(frontier,userService,repoService,gistService,writerThreadPool,dbConnection));
						//JSONObject temp = null;
						crawlerThreadPool.execute(new FlightsBetweenStationsRunnable(
									srcAirport.code1, currentAirport.code1, currentStation.code, destStation.code, startDate, endDate, results, true, srcAirport.city, currentAirport.city, destStation.name)
								);
						//results.add(temp);
						wasRefused = false;
						// print the queue length when the number of crawls attempted so far is a multiple of 1000					
						//logger.info("Spawning a new crawler thread. Crawler no " + crawlAttemptCount);					
					} catch (RejectedExecutionException rejectedJobException) {
						//logger.info("New job refused. Sleeping...");
						//logger.debug("New job refused. Sleeping...");
						try {
							Thread.sleep(sleepDuration);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//logger.info("Woke up from sleep. Trying again...");
						//logger.debug("Woke up from sleep. Trying again...");
						
						// roll back
						wasRefused=true;
						//numTweetsProcessed --;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally{															
					}
				}			  

			} catch(NoSuchElementException e)
			{
				e.printStackTrace();
			}
			finally {						
				// initiate the shut-down procedure
				//logger.info("The crawler is shutting down...");

				crawlerThreadPool.shutdown();
				try {
					crawlerThreadPool.awaitTermination(5, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// the crawler has been shut down
					try {
						//logger.info("The crawler has been shut down successfully");
						//writerThreadPool.shutdown();
						//writerThreadPool.awaitTermination(10, TimeUnit.MINUTES);
					} finally {
					}
				}
			}		
		}
		
		//System.err.println("Results4 : "+results.size());
		
		if(srcStation != null && destAirport != null){
			/**
			 * Setting up multiple threads
			 */				
			int maxNumberOfCrawlers = 10;
			int coreNumberOfCrawlers = maxNumberOfCrawlers;
			int maxJobs = (int) (maxNumberOfCrawlers*1.5);
			int idleTime = 10; // s
			BlockingQueue<Runnable> blockingJobQueue = new ArrayBlockingQueue<Runnable>(maxJobs);
			ThreadPoolExecutor crawlerThreadPool = new ThreadPoolExecutor(
												coreNumberOfCrawlers,
												maxNumberOfCrawlers,
												idleTime,
												TimeUnit.SECONDS,
												blockingJobQueue);

			long sleepDuration = 20; // ms							
			boolean wasRefused = false;

			try {			
				//Iterator<Airport> it = allAirports.iterator();
				Iterator<Airport> it = majorAirports.iterator();
				Airport currentAirport = null;
				Station currentStation = null;
				while(it.hasNext()){
					
					if(!wasRefused){
						currentAirport = it.next();
						if(currentAirport.code1 == destAirport.code1)	continue;
						currentStation = findStation(currentAirport.city);
						if(currentStation == null) continue;
					}
					
					try {
						// schedule when to spawn a new crawler thread
						Thread.sleep(25);	//ms
						
						// spawn a crawler 
						//crawlerThreadPool.execute(new UserCrawler(frontier,userService,repoService,gistService,writerThreadPool,dbConnection));
						//JSONObject temp = null;
						crawlerThreadPool.execute(new FlightsBetweenStationsRunnable(
								srcStation.code, currentStation.code, currentAirport.code1, destAirport.code1, startDate, endDate, results, false, srcStation.name, currentStation.name, destAirport.city)
								);
						//results.add(temp);
						wasRefused = false;
						// print the queue length when the number of crawls attempted so far is a multiple of 1000					
						//logger.info("Spawning a new crawler thread. Crawler no " + crawlAttemptCount);					
					} catch (RejectedExecutionException rejectedJobException) {
						//logger.info("New job refused. Sleeping...");
						//logger.debug("New job refused. Sleeping...");
						try {
							Thread.sleep(sleepDuration);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//logger.info("Woke up from sleep. Trying again...");
						//logger.debug("Woke up from sleep. Trying again...");
						
						// roll back
						wasRefused=true;
						//numTweetsProcessed --;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally{															
					}
				}			  

			} catch(NoSuchElementException e)
			{
				e.printStackTrace();
			}
			finally {						
				// initiate the shut-down procedure
				//logger.info("The crawler is shutting down...");

				crawlerThreadPool.shutdown();
				try {
					crawlerThreadPool.awaitTermination(5, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// the crawler has been shut down
					try {
						//logger.info("The crawler has been shut down successfully");
						//writerThreadPool.shutdown();
						//writerThreadPool.awaitTermination(10, TimeUnit.MINUTES);
					} finally {
					}
				}
			}		
		}
		
		//System.err.println("Results5 : "+results.size());
		String retval = "[";
		
		int i;		
		for(i=0; i<results.size()-1; i++){
			//System.err.println(results.get(i));
			if(results.get(i) == null)	continue;
			retval += results.get(i).toString()+",";
		}
		if(results.get(i) != null)	retval += results.get(i).toString()+"]";
		else retval += "]";
		//return retval;
		return results;
		
	}

	public static Airport findAirport(String airportName){	
		Iterator<Airport> it = allAirports.iterator();
		while(it.hasNext()){
			Airport temp = it.next(); 
			//System.err.println(temp.city);
			
			if( temp.city.contains(airportName) ){
				return temp;
			}
		}
		return null;
	}
	
	public static Station findStation(String stationName){	
		Iterator<Station> it = allStations.iterator();
		while(it.hasNext()){
			Station temp = it.next(); 
			//temp.tostring();
			if(temp.code == "")	continue;
			try{
				if( temp.name.contains(stationName) ){
					return temp;
				}
			} catch(NullPointerException e){
				
			}
		}
		return null;
	}
	
	/**
	public static JSONObject createResultTrain(JSONObject train){
		return train;
	} 		
	*/
	
}

class Airport{
	//Attribrutes
	public String code1;
	public String code2;
	public String name;
	public String city;
	public double lat;
	public double lon;
	
	public Airport(String recordStr){
		try{
			String[] record = recordStr.split("\t");
			this.code1 = record[0];
			this.code2 = record[1];
			this.name = record[2];
			this.city = record[3];
			this.lat = Double.parseDouble(record[4]);
			this.lon = Double.parseDouble(record[5]);
		} catch(ArrayIndexOutOfBoundsException e){
			//System.err.println(recordStr);
		}
	}
	
	public Airport(ResultSet rs)throws SQLException{
		this.code1 = rs.getString(1);
		this.code2 = rs.getString(2);
		this.name = rs.getString(3);
		this.city = rs.getString(4);
		this.lat = Double.parseDouble(rs.getString(5));
		this.lon = Double.parseDouble(rs.getString(6));
	}
	
	public void tostring(){
		System.err.println("code1 : " + code1 + "code2 : " + code2 + " name : " + name + " city : " + city);
	}	
}

class Station{
	//Attribrutes
	public String code;	
	public String name;
	public String address;
	public String state;	
	
	public Station(JSONObject json) throws JSONException{	
		//String code = "";
		
		this.code = json.getString("code");
		this.name = json.getString("name");
		this.address = json.getString("address");
		this.state = json.getString("state");
	}	
	
	public Station(ResultSet rs) throws SQLException{
		this.code = rs.getString(2);		
		this.name = rs.getString(3);
		this.address = rs.getString(4);
		this.state = rs.getString(5);
	}

	
	public void tostring(){
		System.err.println("code : " + code + " name = " + name + " address = " + address + "state = " + state);
	}	
}