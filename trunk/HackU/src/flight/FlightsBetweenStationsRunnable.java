package flight;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import utils.Utils;

public class FlightsBetweenStationsRunnable implements Runnable {
	//private static final boolean DEBUG = true;

	// Attributes	
	String airport1;
	String airport2;
	String startDate;
	String endDate;
	JSONObject json; 
	
	//Constructors
	public FlightsBetweenStationsRunnable(String _airport1, String _airport2, String _startDate, String _endDate, JSONObject _json){
		this.airport1 = _airport1;
		this.airport2 = _airport2;
		this.startDate = _startDate;
		this.endDate = _endDate;
		this.json = _json;
	}
	
	@Override
	public void run(){
		String urlString = "http://www.cleartrip.com/flights/calendar/calendarstub.json?";
		urlString += "from="+airport1;
		urlString += "&to="+airport2;
		urlString += "&start_date="+startDate;
		urlString += "&end_date="+endDate;
		
		System.out.println(urlString);
		
		URL url;
		try {
			url = new URL(urlString);
			String doc;
			try {
				doc = Utils.readPage(url);
				//System.out.println(doc);
				this.json = new JSONObject(doc);
				System.out.println(json.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}