package rail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Utils;

public class TrainsBetweenStationsRunnable implements Runnable {
	//private static final boolean DEBUG = true;

	// Attributes	
	String station1;
	String station2;
	String date;
	String Class;
	JSONObject json; 
	
	//Constructors
	public TrainsBetweenStationsRunnable(String _station1, String _station2, String _date, String _Class, JSONObject _json){
		this.station1 = _station1;
		this.station2 = _station2;
		this.date = _date;
		this.Class = _Class;
		this.json = _json;
	}
	
	@Override
	public void run(){
		String urlString = "http://www.cleartrip.com/trains/results?";
		urlString += "from_station="+station1;
		urlString += "&to_station="+station2;
		urlString += "&class="+Class;
		urlString += "&adults=1&children=0&male_seniors=0&female_seniors=0";
		
		String dateFormatted = Integer.parseInt(date.substring(6, 8)) + "-" + Integer.parseInt(date.substring(4, 6)) + "-" + Integer.parseInt(date.substring(0, 4));
		
		urlString += "&date="+dateFormatted;
		
		//System.out.println(urlString);	
		URL url;
		try {
			url = new URL(urlString);
			String doc = Utils.readPage(url);
			//doc = Jsoup.connect("http://www.google.com").get();
			String jsonTemp = doc.toString().split("trains:")[1];
			jsonTemp = jsonTemp.split("trips:")[0];
			//System.out.println(json);			
			JSONObject jsonObject = new JSONObject(jsonTemp);
			//System.out.println(jsonObject.toString());
			json = jsonObject;
		} catch (IOException e) {
			e.printStackTrace();
			//System.err.println(url);
			System.out.println("Exception occured");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}