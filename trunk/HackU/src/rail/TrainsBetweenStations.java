package rail;
import java.io.IOException;
import java.net.URL;

import org.json.*;

import utils.Utils;

public class TrainsBetweenStations {
	public static void main(String args[]){
		String stationIdIRI1 = "1225";
		String stationIdIRI2 = "481";
		
		//System.setProperty("http.proxyHost", "vsnlproxy.iitk.ac.in");
		//System.setProperty("http.proxyPort", "3128");		

		//String url = "http://indiarailinfo.com/search/"+stationIdIRI1+"/0/"+stationIdIRI2;
		
		String urlString = "http://www.cleartrip.com/trains/results?from_station=JHS&to_station=CNB&class=2S&date=26-8-2012&adults=1&children=0&male_seniors=0&female_seniors=0";
		
		System.out.println(urlString);	
		URL url;
		try {
			url = new URL(urlString);
			String doc = Utils.readPage(url);
			//doc = Jsoup.connect("http://www.google.com").get();
			String json = doc.toString().split("trains:")[1];
			json = json.split("trips:")[0];
			//System.out.println(json);			
			JSONObject jsonObject = new JSONObject(json);
			System.out.println(jsonObject.toString());
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
	}
}
