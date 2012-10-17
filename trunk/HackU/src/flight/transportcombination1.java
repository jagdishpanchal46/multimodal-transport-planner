package flight;

import rail.*;

import java.io.*;
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
						JSONObject jsonflight=FlightsBetweenStations.flightsBetweenStations(from,to,sdate,edate);
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
			/*for(int j=0;j<result_at_i.size();j++)
			{
				try
				{
					JSONObject dates=result_at_i.get(j);
						if(o==1&&v==1&&d==1)
						{
							System.out.println("Train="+dates.getJSONObject("name"));
							System.out.println("Departs="+dates.getJSONObject("depart_date")+" "+dates.getJSONObject("depart_time")+" from="+dates.getJSONObject("from"));
							System.out.println("Arrives="+dates.getJSONObject("arrive_date")+" "+dates.getJSONObject("arrive_time")+" at="+from);
							System.out.println("fare="+dates.getJSONObject("fare"));
						}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
				}
			}*/
		}
	}
		//iterate over and print results
	public static List< List<JSONObject> > transportcombinations(String origin,String destination,String via,String sdate,String edate)
	{
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
		/*try {
			retvals5 = Utils.concatanateRoutes(Utils.createResultTrain(jsonTrain1,sdate),Utils.createResultTrain(jsonTrain2,sdate), true,sdate,origin+"("+origin+")",via+"("+via+")",destination+"("+destination+")");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(); 
		}*/
		/* variable type(result,retvals)                          transport route
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
			int end_date=Integer.parseInt(edate);
			int start_date=Integer.parseInt(sdate);
			for(int k=0;k<=end_date-start_date;k++)
			{
				start_date+=k;
				sdate=sdate.substring(0,6)+start_date.toString();
				jsonTrain2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",sdate);
			 
			try
			{
				retvals1= Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(jsonTrain2,sdate),true,sdate,edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
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
		else if(o==1&&v==0&&d==1)
		{
			
		}
		else if(o==0&&v==1&&d==1)
		{
			jsonFlight1=FlightsBetweenStations.flightsBetweenStations(via,destination,sdate,edate);
			jsonTrain1=TrainsBetweenStations.trainsBetweenStations(origin,via,"SL",sdate);
			int end_date=Integer.parseInt(edate);
			int start_date=Integer.parseInt(sdate);
			for(int k=0;k<=end_date-start_date;k++)
			{
				start_date+=k;
				sdate=sdate.substring(0,6)+start_date.toString();
			try
			{
				retvals2= Utils.concatanateRoutes(jsonFlight1, Utils.createResultTrain(jsonTrain2,sdate),false,sdate,edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");//note that bool is false
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
			int end_date=Integer.parseInt(edate);
			int start_date=Integer.parseInt(sdate);
			jsonFlight=FlightsBetweenStations.flightsBetweenStations(origin,destination,sdate,sdate);
			JSONObject jsonFlight2=FlightsBetweenStations.flightsBetweenStations(origin,via,sdate,sdate);
			JSONObject jsonFlight3=FlightsBetweenStations.flightsBetweenStations(via,destination,sdate,edate);
			try
			{
				retvals3= Utils.concatanateRoutes(jsonFlight2,jsonFlight3,true,sdate,edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
				for(int k=0;k<=end_date-start_date;k++)
				{
					start_date+=k;
					sdate=sdate.substring(0,6)+start_date.toString();
					retvals2= Utils.concatanateRoutes(jsonFlight3,Utils.createResultTrain(jsonTrain2,sdate),false,sdate,edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
					retvals1= Utils.concatanateRoutes(jsonFlight2,Utils.createResultTrain(jsonTrain2,sdate),true,sdate,edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
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
		jsonTrain1=TrainsBetweenStations.trainsBetweenStations(origin,via,"SL",sdate);
		int end_date=Integer.parseInt(edate);
		int start_date=Integer.parseInt(sdate);
		String sdate1="";
		for(int k=0;k<=end_date-start_date;k++)
		{
			start_date+=k;
			sdate1=sdate.substring(0,6)+start_date.toString();
			jsonTrain2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",sdate1);
			try
			{
				retvals5=Utils.concatanateRoutes(Utils.createResultTrain(jsonTrain1,sdate),Utils.createResultTrain(jsonTrain2, sdate1),true,sdate1,edate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
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
		
		retval.add(results1);
		retval.add(results2);
		retval.add(results3);
		retval.add(results5);
	
		return retval;
		
	}
}