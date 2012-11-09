package rail;
import java.io.IOException;
import java.net.URL;

import org.json.*;

import utils.Utils;

// Class to get train running information from cleartrip.com
public class TrainsBetweenStations {
	
	// Driver function
	public static void main(String args[]){
		//System.out.println(trainsBetweenStations("JHS", "CNB", "2S", "20120826"));
		//System.out.println(trainsBetweenStations("LKO", "CNB", "2S", "20121130"));
		System.out.println(trainsBetweenStations("SC", "TATA", "2S", "20121130"));
	}
	
	public static JSONObject trainsBetweenStations(String station1, String station2, String Class, String date){
		String urlString = "http://www.cleartrip.com/trains/results?";
		urlString += "from_station="+station1;
		urlString += "&to_station="+station2;
		urlString += "&class="+Class;
		urlString += "&adults=1&children=0&male_seniors=0&female_seniors=0";
		
		String dateFormatted = Integer.parseInt(date.substring(6, 8)) + "-" + Integer.parseInt(date.substring(4, 6)) + "-" + Integer.parseInt(date.substring(0, 4));
		
		urlString += "&date="+dateFormatted;
		
		//System.err.println(urlString);	
		URL url;
		try {
			url = new URL(urlString);
			String doc = Utils.readPage(url);
			//doc = Jsoup.connect("http://www.google.com").get();
			String json = doc.toString().split("trains:")[1];
			json = json.split("trips:")[0];
			//System.out.println(json);			
			JSONObject jsonObject = new JSONObject(json);
			//System.out.println(jsonObject.toString());
			return jsonObject;
			/**
			Element body = doc.getElementsByTag("body").first();
			Element para = body.getElementsContainingOwnText("URL: ").first();
			String text = para.text();
			  
			url = text.substring(6, text.length()-2); //URL: "http://onion.com/I9bAm1"
			//System.out.println(allUrls[i]);
			//domainName = (new URL(url)).getHost();
			 * 
			 */
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
		return null;
	}
}
