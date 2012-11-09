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

//changes to be made in utils include adding a parameter in concatanateroutes
//should be done so that the sdate of jsonflight remains the same
public class Utils {
	public static String readPage(URL url) throws Exception {

        DefaultHttpClient client = new DefaultHttpClient();
      
        HttpGet request = new HttpGet(url.toURI());
        request = new HttpGet(url.toString());//HttpGet request = new HttpGet(url.toString());
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
	public static int diff_dates(int m,int adate,int ddate)
	{
		int diff1=ddate-adate;
		if(m==4||m==6||m==9||m==11)
		{
			if(adate==30&&ddate!=30)
			{
				//System.out.println(30-adate+ddate);
				return (30-adate+ddate);
			}
			else
			{
				return diff1;
			}
		}
		else if(m==2)
		{
			if(adate==28&&ddate!=28)
			{
				return (28-adate+ddate);
			}
		}
		else
		{
			if(adate==31&&ddate!=31)
			{
				return (31-adate+ddate);
			}
		}
		return 0;
	}
	public static int getHalt2(String ad, String dd){
		//System.out.println(dd);
		int month=Integer.parseInt(ad.substring(3,5));
		//System.out.println(month);
		int adate = Integer.parseInt(ad.substring(0,2));
		int ddate = Integer.parseInt(dd.substring(0,2));
		//System.out.println(ddate-adate);
		return diff_dates(month,adate,ddate)*24*60;
	}
	public static int getHalt3(String ad,String dd,String at,String dt)
	{
		int a=getHalt1(at,dt);
		int b=getHalt2(ad,dd);
		return (a+b);
	}
	
	public static String convertDateFormat(int flag, String date){
		String retval = "";
		if(flag == 0){
			retval = date.substring(6,date.length()) + "/" + date.substring(4,6) + "/" + date.substring(0,4);  
		} else{
			String[] splitted = date.split("/");
			retval = splitted[2]+splitted[1]+splitted[0];
		}		
		return retval;
	}
	
	public static List<JSONObject> concatanateRoutes(JSONObject json1, JSONObject json2/*, boolean bool*/, String date, String from, String via, String to,String sdate,int index) throws JSONException{	
		int halt_time=500;
		List<JSONObject> retval = new ArrayList<JSONObject>();
		if(index==1){
			// means first flight, then train
			try{
				JSONObject allDates = json1.getJSONObject("calendar_json");
				//System.out.println(allDates.toString());
				//System.err.println(allDates.toString());
				JSONArray flights = allDates.getJSONArray(sdate);
				//System.err.println(flights.toString());
				
				allDates = json2.getJSONObject("calendar_json");
				JSONArray trains = allDates.getJSONArray(date);
				//System.out.println(trains);
				//System.out.println(flights);
				//System.err.println(trains.toString());				
				
				for(int i=0; i<flights.length(); i++){
					/*JSONObject flightTemp = flights.getJSONObject(i);
					 *String at = flightTemp.getString("at");
					 *String ad = flightTemp.getString("ad");
					 *int ad1 = Integer.parseInt(ad.substring(6,8));
					 *int edate = Integer.parseInt(end_date.substring(6,8));
					 *for(int i=0;i<=(edate-ad1);i++)
					 *{
					 *		allDates = json2.getJSONObject("calendar_json");
					 *		JSONArray trains = allDates.getJSONArray(date.substring(0,6)+(ad1+i).toString());*/
					//above changes are not required as it is changed in tc1
					for(int j=0; j<trains.length(); j++){
						JSONObject flightTemp = flights.getJSONObject(i);
						//System.out.println(flightTemp);
						String at = flightTemp.getString("at");
						String ad=flightTemp.getString("ad");
						if(at != "."){
							JSONObject trainTemp = trains.getJSONObject(j);
							//System.out.println(trainTemp);
							String dt = trainTemp.getString("dt");
							
							//String dd=trainTemp.getString("dd");
							String dd = convertDateFormat(0,date);
							if(getHalt3(ad,dd,at,dt)>120&&getHalt3(ad,dd,at,dt)<halt_time)/*&&(getHalt2(ad,dd))*24*60+getHalt1(at,dt)<300*/{
								//System.out.println("1...........");
								JSONObject temp = new JSONObject();
								//System.out.println(flightTemp);
								//TODO build object
								temp.put("type", "FLIGHT then TRAIN");
								
								temp.put("from", from);
								temp.put("via", via);
								temp.put("to", to);
								temp.put("mode1", flightTemp.getString("aln"));
								temp.put("departure time 1", flightTemp.getString("dt"));									
								temp.put("arrival time 1", flightTemp.getString("at"));
								temp.put("arrival date 1", flightTemp.getString("ad"));
								temp.put("departure date 1",sdate);//flightTemp.getString("dd"));
								temp.put("price 1", flightTemp.getString("pr"));
								
								temp.put("mode2", trainTemp.getString("aln"));
								temp.put("departure time 2", trainTemp.getString("dt"));									
								temp.put("arrival time 2", trainTemp.getString("at"));
								temp.put("arrival date 2", trainTemp.getString("ad"));
								temp.put("departure date 2", trainTemp.getString("dd"));
								temp.put("price 2", trainTemp.getString("pr"));
								temp.put("halting time(in min)",Integer.toString(getHalt3(ad,dd,at,dt)) );
								
								temp.put("total price", Double.toString( Double.parseDouble(flightTemp.getString("pr"))+Double.parseDouble(trainTemp.getString("pr")) ) );
								
								//temp.put("ht", getHalt(at,dt));
								//System.out.println("flight then train");
								//System.out.println(temp);
								
								retval.add(temp);      
							}
						}						
					}
					//System.out.println(retval.toString());
				}
			} catch(JSONException e){		
				e.printStackTrace();
			}
		} else if(index==2){
			try{
				JSONObject allDates = json1.getJSONObject("calendar_json");
				//System.err.println(allDates.toString());
				JSONArray trains = allDates.getJSONArray(sdate);
				//System.err.println(trains.toString());
				
				allDates = json2.getJSONObject("calendar_json");
				JSONArray flights = allDates.getJSONArray(date);
				//System.err.println(flights.toString());						
				
				for(int i=0; i<trains.length(); i++){
					/*JSONObject trainTemp = trains.getJSONObject(i);
					 *String at = trainTemp.getString("at");
					 *String ad = trainTemp.getString("ad");
					 *int ad1 = Integer.parseInt(ad.substring(6,8));
					 *int edate = Integer.parseInt(end_date.substring(6,8));
					 *for(int i=0;i<=(edate-ad1);i++)
					 *{
					 *		allDates = json2.getJSONObject("calendar_json");
					 *		JSONArray trains = allDates.getJSONArray(date.substring(0,6)+(ad1+i).toString());*/
					//above changes are not required
					for(int j=0; j<flights.length(); j++){
						JSONObject trainTemp = trains.getJSONObject(i); 
						String at = trainTemp.getString("at");
						String ad=trainTemp.getString("ad");
						if(at != "."){
							JSONObject flightTemp = flights.getJSONObject(j); 
							String dt = flightTemp.getString("dt");
							String dd=convertDateFormat(0,date);//flightTemp.getString("ad");
							//String ad1=flightTemp.getString("ad");
							//System.out.println(trainTemp);
							if(getHalt3(ad,dd,at,dt)>120&&getHalt3(ad,dd,at,dt)<halt_time){
								//System.out.println("2...........");
								JSONObject temp = new JSONObject();
								//TODO build object

								temp.put("type", "TRAIN then FLIGHT");
								
								temp.put("from", from);
								temp.put("via", via);
								temp.put("to", to);
								
								temp.put("mode1", trainTemp.getString("aln"));
								temp.put("departure time 1", trainTemp.getString("dt"));									
								temp.put("arrival time 1", trainTemp.getString("at"));
								temp.put("arrival date 1", trainTemp.getString("ad"));
								temp.put("departure date 1", trainTemp.getString("dd"));
								temp.put("price 1", trainTemp.getString("pr"));
								
								temp.put("mode2", flightTemp.getString("aln"));
								temp.put("departure time 2", flightTemp.getString("dt"));									
								temp.put("arrival time 2", flightTemp.getString("at"));
								temp.put("arrival date 2", flightTemp.getString("ad"));
								temp.put("departure date 2", dd);
								temp.put("price 2", flightTemp.getString("pr"));
								temp.put("halting time(in min)",Integer.toString(getHalt3(ad,dd,at,dt)) );
								temp.put("total price", Double.toString( Double.parseDouble(flightTemp.getString("pr"))+Double.parseDouble(trainTemp.getString("pr")) ) );
								//System.out.println("train then flight");
								//System.out.println(temp);
								retval.add(temp);
							}
						}
					}
				}				
			} catch(JSONException e){
				e.printStackTrace();
			}
		}
		else if(index==3)
		{
			try{
				//System.out.println(json1);
				//System.out.println(json2);
				JSONObject allDates = json1.getJSONObject("calendar_json");
				//System.out.println(allDates.toString());
				//System.err.println(allDates.toString());
				JSONArray flights1 = allDates.getJSONArray(sdate);
				//System.err.println(flights.toString());
				
				allDates = json2.getJSONObject("calendar_json");
				JSONArray flights2 = allDates.getJSONArray(date);
				//System.out.println(trains);
				//System.out.println(flights);
				//System.err.println(trains.toString());				
				
				for(int i=0; i<flights1.length(); i++){
					for(int j=0; j<flights2.length(); j++){
						JSONObject flight1Temp = flights1.getJSONObject(i);
						//System.out.println(flightTemp);
						String at = flight1Temp.getString("at");
						String ad=flight1Temp.getString("ad");
						if(at != "."){
							JSONObject flight2Temp = flights2.getJSONObject(j);
							//System.out.println(trainTemp);
							String dt = flight2Temp.getString("dt");
							
							//String dd=trainTemp.getString("dd");
							String dd = convertDateFormat(0,date);
							if(getHalt3(ad,dd,at,dt)>120&&getHalt3(ad,dd,at,dt)<halt_time)/*&&(getHalt2(ad,dd))*24*60+getHalt1(at,dt)<300*/{
								JSONObject temp = new JSONObject();
								//System.out.println("3............");
								//System.out.println(flightTemp);
								//TODO build object
								temp.put("type", "FLIGHT then FLIGHT");
								
								temp.put("from", from);
								temp.put("via", via);
								temp.put("to", to);
								temp.put("mode1", flight1Temp.getString("aln"));
								temp.put("departure time 1", flight1Temp.getString("dt"));									
								temp.put("arrival time 1", flight1Temp.getString("at"));
								temp.put("arrival date 1", flight1Temp.getString("ad"));
								temp.put("departure date 1",sdate);//flightTemp.getString("dd"));
								temp.put("price 1", flight1Temp.getString("pr"));
								
								temp.put("mode2", flight2Temp.getString("aln"));
								temp.put("departure time 2", flight2Temp.getString("dt"));									
								temp.put("arrival time 2", flight2Temp.getString("at"));
								temp.put("arrival date 2", flight2Temp.getString("ad"));
								temp.put("departure date 2", convertDateFormat(0,date));
								temp.put("price 2", flight2Temp.getString("pr"));
								temp.put("halting time(in min)",Integer.toString(getHalt3(ad,dd,at,dt)) );
								
								temp.put("total price", Double.toString( Double.parseDouble(flight1Temp.getString("pr"))+Double.parseDouble(flight2Temp.getString("pr")) ) );
								
								//temp.put("ht", getHalt(at,dt));
								//System.out.println("anushuman");
								//System.out.println("flight then flight");
								//System.out.println(temp);
								
								retval.add(temp);      
							}
						}						
					}
					//System.out.println(retval.toString());
				}
			} catch(JSONException e){		
				e.printStackTrace();
			}
		}
		else if(index==4)
		{
			//System.out.println("4........");
			try{
				JSONObject allDates = json1.getJSONObject("calendar_json");
				//System.out.println(allDates.toString());
				//System.err.println(allDates.toString());
				JSONArray trains1 = allDates.getJSONArray(sdate);
				//System.err.println(flights.toString());
				
				allDates = json2.getJSONObject("calendar_json");
				JSONArray trains2 = allDates.getJSONArray(date);
				//System.out.println(trains);
				//System.out.println(flights);
				//System.err.println(trains.toString());				
				
				for(int i=0; i<trains1.length(); i++){
					/*JSONObject flightTemp = flights.getJSONObject(i);
					 *String at = flightTemp.getString("at");
					 *String ad = flightTemp.getString("ad");
					 *int ad1 = Integer.parseInt(ad.substring(6,8));
					 *int edate = Integer.parseInt(end_date.substring(6,8));
					 *for(int i=0;i<=(edate-ad1);i++)
					 *{
					 *		allDates = json2.getJSONObject("calendar_json");
					 *		JSONArray trains = allDates.getJSONArray(date.substring(0,6)+(ad1+i).toString());*/
					//above changes are not required as it is changed in tc1
					for(int j=0; j<trains2.length(); j++){
						JSONObject flightTemp = trains1.getJSONObject(i);
						//System.out.println(flightTemp);
						String at = flightTemp.getString("at");
						String ad=flightTemp.getString("ad");
						if(at != "."){
							JSONObject trainTemp = trains2.getJSONObject(j);
							//System.out.println(trainTemp);
							String dt = trainTemp.getString("dt");
							String dd=trainTemp.getString("dd");
							//System.out.println(ad);
							//System.out.println("above is ad and below is dd");
							//System.out.println(dd);
							//System.out.println(ad+'\n'+dd+'\n'+at+'\n'+dt);
							//System.out.println(getHalt1(at,dt));
							//System.out.println(getHalt2(ad,dd)*24*60+getHalt1(at,dt));
							//above statement is returning false
							//String ad1=trainTemp.getString("ad");
							if(getHalt3(ad,dd,at,dt)>120&&getHalt3(ad,dd,at,dt)<halt_time)/*&&(getHalt2(ad,dd))*24*60+getHalt1(at,dt)<300*/{
								JSONObject temp = new JSONObject();
								//System.out.println(flightTemp);
								//TODO build object
								temp.put("type", "TRAIN then TRAIN");
								
								temp.put("from", from);
								temp.put("via", via);
								temp.put("to", to);
								temp.put("mode1", flightTemp.getString("aln"));
								temp.put("departure time 1", flightTemp.getString("dt"));									
								temp.put("arrival time 1", flightTemp.getString("at"));
								temp.put("arrival date 1", flightTemp.getString("ad"));
								temp.put("departure date 1",sdate);//flightTemp.getString("dd"));
								temp.put("price 1", flightTemp.getString("pr"));
								
								temp.put("mode2", trainTemp.getString("aln"));
								temp.put("departure time 2", trainTemp.getString("dt"));									
								temp.put("arrival time 2", trainTemp.getString("at"));
								temp.put("arrival date 2", trainTemp.getString("ad"));
								temp.put("departure date 2", trainTemp.getString("dd"));
								temp.put("price 2", trainTemp.getString("pr"));
								temp.put("halting time(in min)",Integer.toString(getHalt3(ad,dd,at,dt)) );
								
								temp.put("total price", Double.toString( Double.parseDouble(flightTemp.getString("pr"))+Double.parseDouble(trainTemp.getString("pr")) ) );
								
								//temp.put("ht", getHalt(at,dt));
								//System.out.println("train then train");
								//System.out.println(temp);
								retval.add(temp);      
							}
						}						
					}
					//System.out.println(retval.toString());
				}
			} catch(JSONException e){		
				e.printStackTrace();
			}
		}
	
		//System.out.println(retval);
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
	    	//System.out.println(date);
	    	month = tokenizer.nextToken();
	    	//System.out.println(month);
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
