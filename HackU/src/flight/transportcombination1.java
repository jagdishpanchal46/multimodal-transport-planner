package flight;

import rail.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import utils.Utils;
public class transportcombination1
{	
	public static void main(String args){
		String from="Kanpur",via="Lucknow",to="Hyderabad";
		List< List<JSONObject> > results = transportcombinations(from,to,via,"20121019","20121020",null,null,null,null,null,null);	// pass the arguments
		for(int i=0;i<results.size();i++)
		{
			List<JSONObject> result_at_i=results.get(i);
			for(int j=0;j<result_at_i.size();j++)
			{
				try
				{
					JSONObject dates=result_at_i.get(j).getJSONObject("calender_json");
					JSONArray routes=dates.getJSONArray("20121019");
					for(int k=0;k<routes.length();k++)
					{
						JSONObject individual_route=routes[k];
						System.out.println("Airline="+individual_route.getJSONObject("aln"));
						System.out.println("Departs="+individual_route.getJSONObject("dd")+" "+individual_route.getJSONObject("dt")+" from="+from);
						System.out.println("Arrives="+individual_route.getJSONObject("ad")+" "+individual_route.getJSONObject("at")+" at="+from);
						System.out.println("fare="+individual_route.getJSONObject("pr")+"\n\n");
						System.out.println("Train="+individual_route.getJSONObject("name"));
						System.out.println("Departs="+individual_route.getJSONObject("depart_date")+" "+individual_route.getJSONObject("depart_time")+" from="+individual_route.getJSONObject("from"));
						System.out.println("Arrives="+individual_route.getJSONObject("arrive_date")+" "+individual_route.getJSONObject("arrive_time")+" at="+from);
						System.out.println("fare="+individual_route.getJSONObject("fare"));
					}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
				}
			}
		}
		//iterate over and print results
	}
	
	public static List< List<JSONObject> > transportcombinations(String origin,String destination,String via,String sdate,String edate,List<JSONObject> results1,List<JSONObject> results2,List<JSONObject> results3,List<JSONObject> results4,List<JSONObject> results5,List<JSONObject> results6)
	{
		List< List<JSONObject> > retval=  new ArrayList< List<JSONObject> >();
		
		List<JSONObject> retvals5 = null;
		List<JSONObject> retvals1 = null;
		List<JSONObject> retvals2 = null;
		List<JSONObject> retvals3 = null;
		List<JSONObject> retvals4 = null;
		List<JSONObject> retvals6 = null;
		int o=1,v=1,d=1;
		JSONObject jsonFlight=null,jsonFlight1=null;
		JSONObject jsonTrain2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",sdate);
		JSONObject jsonTrain1=TrainsBetweenStations.trainsBetweenStations(origin,via,"SL",sdate);
		JSONObject jsonTrain=TrainsBetweenStations.trainsBetweenStations(origin,destination,"SL",sdate);		
		try {
			retvals5 = Utils.concatanateRoutes(Utils.createResultTrain(jsonTrain1,sdate),Utils.createResultTrain(jsonTrain2,sdate), true,sdate,origin+"("+origin+")",via+"("+via+")",destination+"("+destination+")");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
			jsonFlight= FlightsBetweenStations.flightsBetweenStations(origin,via,sdate,edate);
			try
			{
				retvals1= Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(jsonTrain2,sdate),true,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
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
		else if(o==1&&v==0&&d==1)
		{
			//JSONObject jsonFlight=FlightsBetweenStations.flightsBetweenStations(origin,destination,sdate,edate);
		}
		else if(o==0&&v==1&&d==1)
		{
			jsonFlight1=FlightsBetweenStations.flightsBetweenStations(via,destination,sdate,edate);
			try
			{
				retvals2= Utils.concatanateRoutes(jsonFlight1, Utils.createResultTrain(jsonTrain2,sdate),false,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");//note that bool is false
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
		else if(o==1&&v==1&&d==1)
		{
			jsonFlight=FlightsBetweenStations.flightsBetweenStations(origin,destination,sdate,edate);
			JSONObject jsonFlight2=FlightsBetweenStations.flightsBetweenStations(origin,via,sdate,edate);
			JSONObject jsonFlight3=FlightsBetweenStations.flightsBetweenStations(via,destination,sdate,edate);
			try
			{
				retvals3= Utils.concatanateRoutes(jsonFlight2,jsonFlight3,true,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
				retvals2= Utils.concatanateRoutes(jsonFlight3,Utils.createResultTrain(jsonTrain2,sdate),false,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
				retvals1= Utils.concatanateRoutes(jsonFlight2,Utils.createResultTrain(jsonTrain2,sdate),true,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
				for(int i=0;i<retvals1.size();i++)
				{
					results1.add( retvals1.get(i) );
				}
				for(int i=0;i<retvals2.size();i++)
				{
					results2.add( retvals2.get(i) );
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
		else
		{
		}
		
		retval.add(results1);
		retval.add(results2);
		retval.add(results3);
		retval.add(results4);
		retval.add(results5);
		retval.add(results6);
	
		return retval;
		
	}
}