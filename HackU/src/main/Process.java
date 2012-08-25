package main;
import flight.*;
import rail.*;

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
import java.util.ArrayList;
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

public class Process {
	/**
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
	**/
	
	// Global variables
	
	public static String allAirportsFile = "data/AirportDatabase.dat";
	public static String majorAirportsFile = "data/MajorAirportDatabase.dat";
	public static String allStationsFile = "data/stations.csv";
	public static String majorStationsFile = "data/majorStations.csv";
	
	public static List<Airport> allAirports = new ArrayList<Airport>();
	public static List<Airport> majorAirports = new ArrayList<Airport>();
	
	public static List<Station> allStations = new ArrayList<Station>();
	public static List<Station> majorStations = new ArrayList<Station>();
	
	public static void init(){
		fillListsAirport(allAirportsFile, allAirports);
		fillListsAirport(majorAirportsFile, majorAirports);
		fillListsStation(allStationsFile, allStations);
		fillListsStation(majorStationsFile, majorStations);
	}

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
					arrayList.add(new Airport(line));
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
			JSONArray jsonArray = new JSONArray(jsonStr);
			//JSONObject json = new JSONObject(jsonStr);
			for(int i=0; i<jsonArray.length(); i++){				
				arrayList.add(new Station(jsonArray.getJSONObject(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws InterruptedException{
		init();
		
		//TODO take these inputs from a json file
		String srcName = "Jhansi";
		String destName = "Bangalore";
		String startDate = "20120827";
		String endDate = "20120829";
				
		Airport srcAirport = findAirport(srcName);
		Airport destAirport = findAirport(destName);
		
		Station srcStation = findStation(srcName);
		Station destStation = findStation(destName);			
		
		List<JSONObject> results = new ArrayList<JSONObject>();
		
		if(srcAirport != null && destAirport != null){
			//Result result = createResultFlight();			
			results.add(FlightsBetweenStations.flightsBetweenStations(srcAirport.code1, destAirport.code1, startDate, endDate));
		}
		
		if(srcStation != null && destStation != null){
			JSONObject result = createResultTrain(TrainsBetweenStations.trainsBetweenStations(srcStation.code, destStation.code, startDate, endDate));
			results.add(result);
		}
		
		if(srcAirport != null){
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
				Iterator<Airport> it = allAirports.iterator();
				Airport currentAirport = null;
				while(it.hasNext()){
					
					if(!wasRefused){
						currentAirport = it.next();
					}
					
					try {
						// schedule when to spawn a new crawler thread
						Thread.sleep(25);	//ms
						
						// spawn a crawler 
						//crawlerThreadPool.execute(new UserCrawler(frontier,userService,repoService,gistService,writerThreadPool,dbConnection));
						JSONObject temp = null;
						crawlerThreadPool.execute(new FlightsBetweenStationsRunnable(srcAirport.code1, currentAirport.code1, startDate, endDate, temp));
						results.add(temp);
						wasRefused = false;
						// print the queue length when the number of crawls attempted so far is a multiple of 1000					
						//logger.info("Spawning a new crawler thread. Crawler no " + crawlAttemptCount);					
					} catch (RejectedExecutionException rejectedJobException) {
						//logger.info("New job refused. Sleeping...");
						//logger.debug("New job refused. Sleeping...");
						Thread.sleep(sleepDuration);
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
	}

	public static Airport findAirport(String airportName){	
		Iterator<Airport> it = allAirports.iterator();
		while(it.hasNext()){
			Airport temp = it.next(); 
			if( temp.city == airportName){
				return temp;
			}
		}
		return null;
	}
	
	public static Station findStation(String stationName){	
		Iterator<Station> it = allStations.iterator();
		while(it.hasNext()){
			Station temp = it.next(); 
			if( temp.name.contains(stationName) || temp.address.contains(stationName) ){
				return temp;
			}
		}
		return null;
	}
	
	public static JSONObject createResultTrain(JSONObject train){
		return train;
	} 	
	
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
		String[] record = recordStr.split("\t");
		this.code1 = record[0];
		this.code2 = record[1];
		this.name = record[2];
		this.city = record[3];
		this.lat = Double.parseDouble(record[4]);
		this.lon = Double.parseDouble(record[5]);
	}	
}

class Station{
	//Attribrutes
	public String code;	
	public String name;
	public String address;
	public String state;	
	
	public Station(JSONObject json) throws JSONException{		
		this.code = json.getString("code");
		this.name = json.getString("name");
		this.address = json.getString("address");
		this.state = json.getString("state");
	}	
}