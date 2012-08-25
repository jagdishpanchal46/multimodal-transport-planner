package flight;

import utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class FlightsBetweenStations {
	public static void main(String args[]){
		System.out.println(flightsBetweenStations("DEL", "BLR", "20120827", "20120828"));
	}		
	
	public static JSONObject flightsBetweenStations(String _airport1, String _airport2, String _startDate, String _endDate){
		String urlString = "http://www.cleartrip.com/flights/calendar/calendarstub.json?";
		urlString += "from="+_airport1;
		urlString += "&to="+_airport2;
		urlString += "&start_date="+_startDate;
		urlString += "&end_date="+_endDate;
		
		System.out.println(urlString);
		//Document doc;		
		URL url;
		try {
			url = new URL(urlString);
			String doc;
			try {
				doc = Utils.readPage(url);
				//System.out.println(doc);
				JSONObject jsonObject = new JSONObject(doc);
				System.out.println(jsonObject.toString());
				return jsonObject;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}		
	}
}
