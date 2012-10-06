package flight;

import rail.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Utils;
public class transportcombination1
{	
	public static void main(String args){
		//List< List<JSONObject> > results = transportcombinations();	// pass the arguments
		
		//iterate over and print results
	}
	
	public static List< List<JSONObject> > transportcombinations(String origin,String destination,String via,String sdate,String edate,List<JSONObject> results1,List<JSONObject> results2,List<JSONObject> results3,List<JSONObject> results4,List<JSONObject> results5,List<JSONObject> results6)
	{
		List< List<JSONObject> > retval=  new ArrayList< List<JSONObject> >();
		
		List<JSONObject> retvals5;
		List<JSONObject> retvals1 = null;
		
		int o=1,v=1,d=1;
		JSONObject jsonTrain2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",sdate);
		JSONObject jsonTrain1=TrainsBetweenStations.trainsBetweenStations(origin,via,"SL",sdate);
		JSONObject jsonTrain=TrainsBetweenStations.trainsBetweenStations(origin,destination,"SL",sdate);		
		try {
			retvals5 = Utils.concatanateRoutes(Utils.createResultTrain(jsonTrain1,sdate),Utils.createResultTrain(jsonTrain2,sdate), true,sdate,origin+"("+origin+")",via+"("+via+")",destination+"("+destination+")");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//why sdate only for trains??
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
			JSONObject jsonFlight= FlightsBetweenStations.flightsBetweenStations(origin,via,sdate,edate);
			try
			{
				retvals1= Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(jsonTrain2,sdate),true, 	               			        sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
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
		else if(o==1&&v==0&&d==1)//what about the direct flights??
		{
			//JSONObject jsonFlight=FlightsBetweenStations.flightsBetweenStations(origin,destination,sdate,edate);
		}
		else if(o==0&&v==1&&d==1)
		{
			JSONObject jsonFlight=FlightsBetweenStations.flightsBetweenStations(via,destination,sdate,edate);
			try
			{
				List<JSONObject> retvals2= Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(jsonTrain2,sdate),false,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");//note that bool is false
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
			JSONObject jsonFlight1=FlightsBetweenStations.flightsBetweenStations(origin,destination,sdate,edate);
			JSONObject jsonFlight2=FlightsBetweenStations.flightsBetweenStations(origin,via,sdate,edate);
			JSONObject jsonFlight3=FlightsBetweenStations.flightsBetweenStations(via,destination,sdate,edate);
			try
			{
				List<JSONObject> retvals3= Utils.concatanateRoutes(jsonFlight2,jsonFlight3,true,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
				List<JSONObject> retvals2= Utils.concatanateRoutes(jsonFlight3,Utils.createResultTrain(jsonTrain2,sdate),false,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
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
		
		retval.add(retvals1);
		//retval.add(retvals2);
		//retval.add(retvals3);
		retval.add(retvals1);
		retval.add(retvals1);
		retval.add(retvals1);
	
		return retval;
		
	}
}