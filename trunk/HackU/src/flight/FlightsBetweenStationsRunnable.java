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
	String from;
	String via;
	String to;
	
	//Constructors
	public FlightsBetweenStationsRunnable(String _airport1, String _airport2, String _station1, 
			String _station2, String _startDate, String _endDate, List<JSONObject> _results, boolean _bool, String _from, String _via, String _to){
		this.airport1 = _airport1;
		this.airport2 = _airport2;
		this.station1 = _station1;
		this.station2 = _station2;
		this.startDate = _startDate;
		this.endDate = _endDate;
		this.results = _results;
		this.bool = _bool;
		this.from = _from;
		this.via = _via;
		this.to = _to;
	}
	
	@Override
	public void run(){
		if(bool){
			JSONObject jsonFlight = FlightsBetweenStations.flightsBetweenStations(airport1, airport2, startDate, endDate);
			JSONObject jsonTrain = TrainsBetweenStations.trainsBetweenStations(station1, station2, "SL", startDate);
			try {
				List<JSONObject> retvals = Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(jsonTrain, startDate), 
						true, startDate, from+"("+airport1+")", via+"("+airport2+")", to+"("+station2+")");
				for(int i=0; i<retvals.size(); i++){					
					this.results.add( retvals.get(i) );
				}				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else{
			JSONObject jsonTrain = TrainsBetweenStations.trainsBetweenStations(airport1, airport2, "SL", startDate);
			JSONObject jsonFlight = FlightsBetweenStations.flightsBetweenStations(station1, station2, startDate, endDate);
			try {
				List<JSONObject> retvals = Utils.concatanateRoutes( Utils.createResultTrain(jsonTrain, startDate), jsonFlight, false, startDate, from+"("+airport1+")", via+"("+airport2+")", to+"("+station2+")"); 
				for(int i=0; i<retvals.size(); i++){					
					this.results.add( retvals.get(i) );
				}				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}				
	}
}