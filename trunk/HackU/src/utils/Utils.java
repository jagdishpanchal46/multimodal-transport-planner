package utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
	public static String readPage(URL url) throws Exception {

        DefaultHttpClient client = new DefaultHttpClient();
      
        //HttpGet request = new HttpGet(url.toURI());
        HttpGet request = new HttpGet(url.toString());
        HttpResponse response = client.execute(request);

        Reader reader = null;
        try {
            reader = new InputStreamReader(response.getEntity().getContent());

            StringBuffer sb = new StringBuffer();
            {
                int read;
                char[] cbuf = new char[1024];
                while ((read = reader.read(cbuf)) != -1)
                    sb.append(cbuf, 0, read);
            }

            return sb.toString();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	public static String getHalt(String at, String dt){
		String retval="";
		int atime = 60*Integer.parseInt(at.split(":")[0]) + Integer.parseInt(at.split(":")[1]);
		int dtime = 60*Integer.parseInt(dt.split(":")[0]) + Integer.parseInt(dt.split(":")[1]);
		
		int diff = dtime - atime;
		retval += Integer.toString(diff/60)+":"+Integer.toString(diff%60);
		
		return retval;
	}
	public static int getHalt1(String at, String dt){
		String retval="";
		int atime = 60*Integer.parseInt(at.split(":")[0]) + Integer.parseInt(at.split(":")[1]);
		int dtime = 60*Integer.parseInt(dt.split(":")[0]) + Integer.parseInt(dt.split(":")[1]);
		
		int diff = dtime - atime;
		retval += Integer.toString(diff/60)+":"+Integer.toString(diff%60);
		
		return diff;
	}
	
	public static List<JSONObject> concatanateRoutes(JSONObject json1, JSONObject json2, boolean bool, String date, String from, String via, String to) throws JSONException{
		//TODO complete it		
		List<JSONObject> retval = new ArrayList<JSONObject>();
		if(bool){
			// means first flight, then train
			try{
				JSONObject allDates = json1.getJSONObject("calendar_json");
				//System.err.println(allDates.toString());
				JSONArray flights = allDates.getJSONArray(date);
				//System.err.println(flights.toString());
				
				allDates = json2.getJSONObject("calendar_json");
				JSONArray trains = allDates.getJSONArray(date);
				//System.err.println(trains.toString());				
				
				for(int i=0; i<flights.length(); i++){
					for(int j=0; j<trains.length(); j++){
						JSONObject flightTemp = flights.getJSONObject(i);						
						String at = flightTemp.getString("at");
						if(at != "."){
							JSONObject trainTemp = trains.getJSONObject(j); 
							String dt = trainTemp.getString("dt");
							if(getHalt1(at,dt)>120){
								JSONObject temp = new JSONObject();
								//TODO build object
								temp.put("type", "2");
								
								temp.put("from", from);
								temp.put("via", via);
								temp.put("to", to);
								
								temp.put("dt1", flightTemp.getString("dt"));									
								temp.put("at1", flightTemp.getString("at"));
								temp.put("ad1", flightTemp.getString("ad"));
								temp.put("dd1", date);
								temp.put("pr1", flightTemp.getString("pr"));
								
								temp.put("dt2", trainTemp.getString("dt"));									
								temp.put("at2", trainTemp.getString("at"));
								temp.put("ad2", trainTemp.getString("ad"));
								temp.put("dd2", trainTemp.getString("dd"));
								temp.put("pr2", trainTemp.getString("pr"));
								
								temp.put("pr", Double.toString( Double.parseDouble(flightTemp.getString("pr"))+Double.parseDouble(trainTemp.getString("pr")) ) );
								
								temp.put("ht", getHalt(at,dt));
								
								retval.add(temp);
							}
						}						
					}
				}
			} catch(JSONException e){		
				e.printStackTrace();
			}
		} else {
			try{
				JSONObject allDates = json1.getJSONObject("calendar_json");
				//System.err.println(allDates.toString());
				JSONArray trains = allDates.getJSONArray(date);
				//System.err.println(trains.toString());
				
				allDates = json2.getJSONObject("calendar_json");
				JSONArray flights = allDates.getJSONArray(date);
				//System.err.println(flights.toString());						
				
				for(int i=0; i<trains.length(); i++){
					for(int j=0; j<flights.length(); j++){
						JSONObject trainTemp = trains.getJSONObject(i); 
						String at = trainTemp.getString("at");
						if(at != "."){
							JSONObject flightTemp = flights.getJSONObject(j); 
							String dt = flightTemp.getString("dt");
							if(getHalt1(at,dt)>120){
								JSONObject temp = new JSONObject();
								//TODO build object

								temp.put("type", "3");
								
								temp.put("from", from);
								temp.put("via", via);
								temp.put("to", to);
								
								temp.put("dt1", trainTemp.getString("dt"));									
								temp.put("at1", trainTemp.getString("at"));
								temp.put("ad1", trainTemp.getString("ad"));
								temp.put("dd1", trainTemp.getString("dd"));
								temp.put("pr1", trainTemp.getString("pr"));
								
								temp.put("dt2", flightTemp.getString("dt"));									
								temp.put("at2", flightTemp.getString("at"));
								temp.put("ad2", flightTemp.getString("ad"));
								temp.put("dd2", "NA");
								temp.put("pr2", flightTemp.getString("pr"));
								
								temp.put("pr", Double.toString( Double.parseDouble(flightTemp.getString("pr"))+Double.parseDouble(trainTemp.getString("pr")) ) );
								
								temp.put("ht", getHalt(at,dt));
								
								retval.add(temp);
							}
						}
					}
				}				
			} catch(JSONException e){
				e.printStackTrace();
			}
		}
	
		
		return retval;
	}
	
	
	public static JSONObject createResultTrain(JSONObject obj, String _date) throws JSONException{
		//TODO fix it
		
		String to[], from_stations[], to_code[], fare[], from_code[],from[], stops[];
	    String distance[], running_days[], preferred_class[], valid_fare[], name[];
	    String to_stations[], board_offset_times[], board_offset_days[], valid_classes[], train_id[];
	    String number[], arrive_time[], duration[], fare_class[], depart_time[];
	    String arrive_date[], depart_date[], allow_book[];
	    
	    /**
   		FileInputStream fstream = new FileInputStream("/home/prajyoti/Documents/HackU/formatStation");
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String line = "", file = "";
	    while((line = br.readLine()) != null) file += line;	    
	    JSONObject rootTrain = new JSONObject(file);
	    **/
	    
	    JSONObject rootTrain = obj;
	    
	    int i = 1;
	    String x; 
	    JSONObject newResult = new JSONObject();
   		
   		JSONObject finalresult = new JSONObject();
   		
   		//newResult.add(date1);
   		//{calender_json : {date1 : [] , date2: [....], date3: [....]}}
   		//currently show results only for a particular date
   		StringTokenizer tokenizer;
   		String ftDate = "";
   		JSONArray arr = new JSONArray();
	    for(i = 1; i <= rootTrain.length(); i++){
	    	x = Integer.toString(i);
	    	JSONObject oneres = rootTrain.getJSONObject(x);
	    	/**
	    	to[i] = oneres.getString("to");
	    	from_stations[i] = oneres.getString("from_stations");
	    	to_code[i] = oneres.getString("to_code");
	    	fare[i] = oneres.getString("fare");
	    	from_code[i] = oneres.getString("from_code");
	    	from[i] = oneres.getString("from");
	    	from[i] = oneres.getString("from");
	    	stops[i] = oneres.getString("stops");
	    	distance[i] = oneres.getString("distance");
	    	running_days[i] = oneres.getString("running_days");
	    	preferred_class[i] = oneres.getString("preferred_class");
	    	valid_fare[i] = oneres.getString("valid_fare");
	    	//name[i] = oneres.getString("name");
	    	to_stations[i] = oneres.getString("to_stations");
	    	//board_offset_times[i] = oneres.getString("board_offset_times");
	    	board_offset_days[i] = oneres.getString("board_offset_days");
	    	valid_classes[i] = oneres.getString("valid_classes");
	    	//train_id[i] = oneres.getString("train_id");
	    	number[i] = oneres.getString("number");
	    	arrive_time[i] = oneres.getString("arrive_time"); fare_class[i] = oneres.getString("fare_class"); 
	    	duration[i] = oneres.getString("duration"); 
	    	//depart_time[i] = oneres.getString("depart_time");
	    	arrive_date[i] = oneres.getString("arrive_date");
	    	//depart_date[i] = oneres.getString("depart_date"); 
	    	//allow_book[i] = oneres.getString("allow_book");
	    	 * 
	    	 */
	    	tokenizer = new StringTokenizer(oneres.getString("depart_date"), "/");
	    	String year, date, month;
	    	date = tokenizer.nextToken();
	    	month = tokenizer.nextToken();
	    	year = tokenizer.nextToken(); 
	    	ftDate = year+month+date;
	    	JSONObject entry = new JSONObject();	    	
	    	
	    	String[] fairs = (oneres.getString("valid_fare")).split(",");
	    	entry.put("aln", oneres.getString("number"));
	    	entry.put("dt", oneres.getString("depart_time"));
	    	entry.put("at", oneres.getString("arrive_time"));
	    	entry.put("ad", oneres.getString("arrive_date"));
	    	entry.put("dd", oneres.getString("depart_date"));
	    	entry.put("pr", fairs[0]); 	    	
	    	//entry.put("al", oneres.getString("name")); 
	    	//entry.put("al", oneres.getString("train_id"));	    		    
	    	//entry.put("at", oneres.getString("board_offset_times"));
	    	//String afdarray[] = {" ", " "};
	    	//entry.put("afd", afdarray); 
	    	//entry.put("via", afdarray);
	    	//entry.put("to", oneres.getString("to"));
	    	arr.put(entry);
    	}	
	    finalresult.put(_date, arr);
	    newResult.put("calendar_json", finalresult);
	
	    return newResult;
	}	
}
