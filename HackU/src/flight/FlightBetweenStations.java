package flight;

import utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class FlightBetweenStations {
	public static void main(String args[]){
		String stationId1 = "DEL";
		String stationId2 = "PAR";
		
		//System.setProperty("http.proxyHost", "vsnlproxy.iitk.ac.in");
		//System.setProperty("http.proxyPort", "3128");		

		//String url = "http://indiarailinfo.com/search/"+stationIdIRI1+"/0/"+stationIdIRI2;
		
		String urlString = "http://www.cleartrip.com/flights/calendar/calendarstub.json?";
		urlString += "from="+stationId1 ;
		urlString += "&to="+stationId2 ;
		urlString += "&start_date="+20120801;
		urlString += "&end_date="+20120831;
		
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
