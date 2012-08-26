package flight;

import rail.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Utils;

public class FlightsBetweenStationsRunnable implements Runnable {
	//private static final boolean DEBUG = true;

	// Attributes	
	String airport1;
	String airport2;
	String station1;
	String station2;
	String startDate;
	String endDate;
	List<JSONObject> results; 
	boolean bool;
	
	//Constructors
	public FlightsBetweenStationsRunnable(String _airport1, String _airport2, String _station1, 
			String _station2, String _startDate, String _endDate, List<JSONObject> _results, boolean _bool){
		this.airport1 = _airport1;
		this.airport2 = _airport2;
		this.station1 = _station1;
		this.station2 = _station2;
		this.startDate = _startDate;
		this.endDate = _endDate;
		this.results = _results;
		this.bool = _bool;
	}
	
	@Override
	public void run(){
		if(bool){
			JSONObject jsonFlight = FlightsBetweenStations.flightsBetweenStations(airport1, airport2, startDate, endDate);
			JSONObject jsonTrain = TrainsBetweenStations.trainsBetweenStations(station1, station2, "SL", startDate);
			try {
				this.results.add( Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(jsonTrain), true) );
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else{
			JSONObject jsonTrain = TrainsBetweenStations.trainsBetweenStations(airport1, airport2, "SL", startDate);
			JSONObject jsonFlight = FlightsBetweenStations.flightsBetweenStations(station1, station2, startDate, endDate);
			try {
				this.results.add( Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(jsonTrain), false) );
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/**
		String urlString = "http://www.cleartrip.com/flights/calendar/calendarstub.json?";
		urlString += "from="+airport1;
		urlString += "&to="+airport2;
		urlString += "&start_date="+startDate;
		urlString += "&end_date="+endDate;
		
		//System.out.println(urlString);
		JSONObject jsonFlight;
		
		URL url;
		try {
			url = new URL(urlString);
			String doc;
			try {
				doc = Utils.readPage(url);
				//System.out.println(doc);
				jsonFligh = new JSONObject(doc);				
				//System.out.println(json.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		**/			
	}
}