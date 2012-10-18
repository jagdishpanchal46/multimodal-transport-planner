package flight;

import rail.*;

//import java.net.MalformedURLException;
//import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import utils.Utils;
public class transportcombination1
{	
	public static void main(String args){
		String from="Kanpur",via="Lucknow",to="Hyderabad";
		int o=1,v=1,d=1;
		String sdate="20121019",edate="20121021";
		List< List<JSONObject> > results = transportcombinations(from,to,via,sdate,edate);
		JSONObject jsontrain=TrainsBetweenStations.trainsBetweenStations(from,to,"SL",sdate);
		try
		{
			JSONObject all_dates1=jsontrain.getJSONObject("calender_json");
			JSONArray trains=all_dates1.getJSONArray(sdate);
			for(int i=0;i<trains.length();i++)
			{
				JSONObject dates=trains.getJSONObject(i);
				System.out.println("Train="+dates.getJSONObject("train"));
				System.out.println("Departs="+dates.getJSONObject("dd")+" "+dates.getJSONObject("dt")+" from="+from);
				System.out.println("Arrives="+dates.getJSONObject("ad")+" "+dates.getJSONObject("at")+" at="+from);
				System.out.println("fare="+dates.getJSONObject("pr")+"\n\n");
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		for(int i=0;i<results.size();i++)
		{
			List<JSONObject> result_at_i=results.get(i);
			for(int j=0;j<result_at_i.size();j++)
			{
				JSONObject routes=result_at_i.get(j);
				try
				{
					if(o==1&&d==1)//need to do similar thing for trains
					{
						JSONObject jsonflight=FlightsBetweenStations.flightsBetweenStations(from,to,sdate,sdate);
						JSONObject all_dates=jsonflight.getJSONObject("calender_json");
						JSONArray flights=all_dates.getJSONArray(sdate);//manipulate the date here again
						for(int k=0;k<flights.length();k++)
						{
							JSONObject dates=flights.getJSONObject(k);
							System.out.println("Airline="+dates.getJSONObject("aln"));
							System.out.println("Departs="+dates.getJSONObject("dd")+" "+dates.getJSONObject("dt")+" from="+from);
							System.out.println("Arrives="+dates.getJSONObject("ad")+" "+dates.getJSONObject("at")+" at="+from);
							System.out.println("fare="+dates.getJSONObject("pr")+"\n\n");
						}
					}
					else
					{
						System.out.println(routes);
					}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	public static String date(int i,String sdate)
	{
		int start_date=Integer.parseInt(sdate);
		if((((start_date+i)%10000)-((start_date+i)%100))<12)
		{
			if((start_date+i)%100>30)
			{
				start_date+=(100-30+i);//we add (100-30)
			}
			else
			{
				start_date+=i;
			}
		}
		else
		{
			if((start_date+i)%100>31)
			{
				start_date+=(10000-31-1100+i);//to change year,month and the date
			}
			else
			{
				start_date+=i;
			}
			
		}
		String s=Integer.toString(start_date);
		return s;
	}
	public static List< List<JSONObject> > transportcombinations(String origin,String destination,String via,String sdate,String edate)
	{
		int end_date=Integer.parseInt(edate);
		int start_date=Integer.parseInt(sdate);
		int diff=end_date-start_date;
		List< List<JSONObject> > retval=  new ArrayList< List<JSONObject> >();
		
		List<JSONObject> retvals5 = null,results5=null;
		List<JSONObject> retvals1 = null,results1=null;
		List<JSONObject> retvals2 = null,results2=null;
		List<JSONObject> retvals3 = null,results3=null;
		int o=1,v=1,d=1;
		JSONObject jsonFlight=null,jsonFlight1=null;
		JSONObject jsonTrain1=TrainsBetweenStations.trainsBetweenStations(origin,via,"SL",sdate);
		JSONObject jsonTrain2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",sdate);
		JSONObject jsonTrain=TrainsBetweenStations.trainsBetweenStations(origin,destination,"SL",sdate);		
		/* // TODO Auto-generated catch block
		 * variable type(result,retvals)                          transport route
		 * 1                                                   flight---->train
		 * 2                                                   train----->flight
		 * 3												   flight---->flight
		 * 4												   flight
		 * 5												   train----->train
		 * 6												   train
		 */
		if(o==1&&v==1&&d==0)
		{
			jsonFlight= FlightsBetweenStations.flightsBetweenStations(origin,via,sdate,sdate);
			for(int k=0;k<=diff;k++)
			{
				/*start_date+=k;
				sdate=sdate.substring(0,6)+Integer.toString(start_date);*/
				JSONObject json_Train2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",date(k,sdate));
			 
			try
			{
				retvals1= Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(json_Train2,date(k,sdate)),true,date(k,sdate),edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
				for(int i=0;i<retvals1.size();i++)
				{
					results1.add( retvals1.get(i) );
				}
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
			}
		}
		else if(o==0&&v==1&&d==1)
		{
			jsonFlight1=FlightsBetweenStations.flightsBetweenStations(via,destination,sdate,edate);
			jsonTrain1=TrainsBetweenStations.trainsBetweenStations(origin,via,"SL",sdate);
			for(int k=0;k<=diff;k++)
			{
				/*start_date+=k;
				sdate=sdate.substring(0,6)+Integer.toString(start_date);*/
				JSONObject json_Train2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",date(k,sdate));
			try
			{
				retvals2= Utils.concatanateRoutes(jsonFlight1, Utils.createResultTrain(json_Train2,date(k,sdate)),false,date(k,sdate),edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");//note that bool is false
				for(int i=0;i<retvals2.size();i++)
				{
					results2.add( retvals2.get(i) );
				}
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
			}
		}
		else if(o==1&&v==1&&d==1)
		{
			jsonFlight=FlightsBetweenStations.flightsBetweenStations(origin,destination,sdate,sdate);
			JSONObject jsonFlight2=FlightsBetweenStations.flightsBetweenStations(origin,via,sdate,sdate);
			JSONObject jsonFlight3=FlightsBetweenStations.flightsBetweenStations(via,destination,sdate,edate);
			try
			{
				retvals3= Utils.concatanateRoutes(jsonFlight2,jsonFlight3,true,sdate,edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
				for(int k=0;k<=end_date-start_date;k++)
				{
					/*start_date+=k;
					sdate=sdate.substring(0,6)+Integer.toString(start_date);;*/
					retvals2= Utils.concatanateRoutes(jsonFlight3,Utils.createResultTrain(jsonTrain2,date(k,sdate)),false,date(k,sdate),edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
					retvals1= Utils.concatanateRoutes(jsonFlight2,Utils.createResultTrain(jsonTrain2,date(k,sdate)),true,date(k,sdate),edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
					for(int i=0;i<retvals1.size();i++)
					{
						results1.add( retvals1.get(i) );
					}
					for(int i=0;i<retvals2.size();i++)
					{
						results2.add( retvals2.get(i) );
					}
				}
				for(int i=0;i<retvals3.size();i++)
				{
					results3.add( retvals3.get(i) );
				}
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
		}
		for(int k=0;k<=diff;k++)
		{
			/*start_date+=k;
			sdate1=sdate.substring(0,6)+Integer.toString(start_date);;*/
			jsonTrain2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",date(k,sdate));
			try
			{
				retvals5=Utils.concatanateRoutes(Utils.createResultTrain(jsonTrain1,date(k,sdate)),Utils.createResultTrain(jsonTrain2, date(k,sdate)),true,date(k,sdate),edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
				for(int i=0;i<retvals5.size();i++)
				{
					results5.add( retvals5.get(i));
				}
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
		}
		
		//results is list of JSON object
		//therefore retval is list of list of JSON objects
		retval.add(results1);
		retval.add(results2);
		retval.add(results3);
		retval.add(results5);
	
		return retval;
		
	}
}