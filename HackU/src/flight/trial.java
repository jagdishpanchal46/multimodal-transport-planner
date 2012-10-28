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
public class trial
{	
	public static void main(String args[])throws Throwable
	{
		String from="HYD",via="BOM",to="DEL";
		//String from="JHS",via="CNB",to="LKO";
		int o=1,v=1,d=1;
		String sdate="20121128",edate="20121128";
		List< List<JSONObject> > results = transportcombinations(from,to,via,sdate,edate);
		try
		{
			JSONObject jsontrain=Utils.createResultTrain(TrainsBetweenStations.trainsBetweenStations(from,to,"SL",sdate),sdate);
			JSONObject all_dates1=jsontrain.getJSONObject("calendar_json");//we are getting null object
			JSONArray trains=all_dates1.getJSONArray(sdate);
			for(int i=0;i<trains.length();i++)
			{
				JSONObject dates=trains.getJSONObject(i);
				System.out.println(dates);
				//System.out.println("Train="+dates.getJSONObject("train"));
				//System.out.println("Departs="+dates.getJSONObject("dd")+" "+dates.getJSONObject("dt")+" from="+from);
				//System.out.println("Arrives="+dates.getJSONObject("ad")+" "+dates.getJSONObject("at")+" at="+from);
				//System.out.println("fare="+dates.getJSONObject("pr")+"\n\n");
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
					if(o==1&&d==1)
					{
						JSONObject jsonflight=FlightsBetweenStations.flightsBetweenStations(from,to,sdate,sdate);
						JSONObject all_dates=jsonflight.getJSONObject("calendar_json");
						JSONArray flights=all_dates.getJSONArray(sdate);
						for(int k=0;k<flights.length();k++)
						{
							JSONObject dates=flights.getJSONObject(k);
							System.out.println(dates);
							//System.out.println("Airline="+dates.getJSONObject("aln"));
							//System.out.println("Departs="+dates.getJSONObject("dd")+" "+dates.getJSONObject("dt")+" from="+from);
							//System.out.println("Arrives="+dates.getJSONObject("ad")+" "+dates.getJSONObject("at")+" at="+from);
							//System.out.println("fare="+dates.getJSONObject("pr")+"\n\n");
						}
					}
					else
					{
						System.out.println(routes);//manipulate the date here again
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
		int m=(((start_date+i)%10000)-((start_date+i)%100));
		if(m<12)
		{
			if(m==4||m==6||m==9||m==11)
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
			else if(m==2)
			{
				  if((start_date+i)%100>28)
				  {
					  start_date+=(100-28+i);
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
					start_date+=(100-31+i);
				}
				else
				{
					start_date+=i;
				}
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
		
		List<JSONObject> retvals5 =  new ArrayList<JSONObject>(),results5= new ArrayList<JSONObject>();//the different variable types store the different transport routes
		List<JSONObject> retvals1 = new ArrayList<JSONObject>(),results1=new ArrayList<JSONObject>();
		List<JSONObject> retvals2 = new ArrayList<JSONObject>(),results2=new ArrayList<JSONObject>();
		List<JSONObject> retvals3 = new ArrayList<JSONObject>(),results3=new ArrayList<JSONObject>();
		int o=1,v=1,d=1;
		JSONObject jsonFlight=null,jsonFlight1=null;
		JSONObject jsonTrain1=TrainsBetweenStations.trainsBetweenStations(origin,via,"SL",sdate);
		JSONObject jsonTrain2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",sdate);	
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
				JSONObject json_Train2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",date(k,sdate));
			 
			try
			{
				retvals1= Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(json_Train2,date(k,sdate)),true,date(k,sdate),origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
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
				JSONObject json_Train2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",date(k,sdate));
			try
			{
				retvals2= Utils.concatanateRoutes(jsonFlight1, Utils.createResultTrain(json_Train2,date(k,sdate)),false,date(k,sdate),origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");//note that bool is false
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
				retvals3= Utils.concatanateRoutes(jsonFlight2,jsonFlight3,true,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
				for(int k=0;k<=end_date-start_date;k++)
				{
					/*start_date+=k;
					sdate=sdate.substring(0,6)+Integer.toString(start_date);;*/
					retvals2= Utils.concatanateRoutes(jsonFlight3,Utils.createResultTrain(jsonTrain2,date(k,sdate)),false,date(k,sdate),origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
					retvals1= Utils.concatanateRoutes(jsonFlight2,Utils.createResultTrain(jsonTrain2,date(k,sdate)),true,date(k,sdate),origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
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
				retvals5=Utils.concatanateRoutes(Utils.createResultTrain(jsonTrain1,sdate),Utils.createResultTrain(jsonTrain2, date(k,sdate)),true,date(k,sdate),origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
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
