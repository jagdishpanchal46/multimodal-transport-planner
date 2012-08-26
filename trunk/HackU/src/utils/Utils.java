package utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
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
        HttpGet request = new HttpGet(url.toURI());
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
	
	public static JSONObject concatanateRoutes(JSONObject json1, JSONObject json2, boolean bool){
		//TODO complete it		
		
		return json1;
	}
	
	public static JSONObject createResultTrain(JSONObject obj) throws JSONException{
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
	    	entry.put("dt", oneres.getString("depart_time"));
	    	
	    	String[] fairs = (oneres.getString("valid_fare")).split(",");
	    	entry.put("pr", fairs[0]); 
	    	entry.put("aln", oneres.getString("number"));
	    	entry.put("al", oneres.getString("name")); 
	    	entry.put("al", oneres.getString("train_id"));
	    	entry.put("ad", oneres.getString("depart_date"));
	    	entry.put("at",".");
	    	//entry.put("at", oneres.getString("board_offset_times"));
	    	String afdarray[] = {" ", " "};
	    	entry.put("afd", afdarray); 
	    	entry.put("via", afdarray);
	    	arr.put(entry);
    	}	
	    finalresult.put(ftDate, arr);
	    newResult.put("calendar_json", finalresult);
	
	    return newResult;
	}	
}
