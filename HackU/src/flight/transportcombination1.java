package flight;

import rail.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Utils;
public abstract class transportcombination1 implements Runnable
{
	String origin,destination,via,sdate,edate;
	List<JSONObject> results1,results2,results3,results4,results5,results6;
	public transportcombination1(String origin1,String destination1,String via1,String sdate1,String edate1,List<JSONObject> _results1,List<JSONObject> _results2,List<JSONObject> _results3,List<JSONObject> _results4,List<JSONObject> _results5,List<JSONObject> _results6)
	{
		this.origin=origin1;
		this.destination=destination1;
		this.via=via1;
		this.sdate=sdate1;
		this.edate=edate1;
		this.results1=_results1;
		this.results2=_results2;
		this.results3=_results3;
		this.results4=_results4;
		this.results5=_results5;
		this.results6=_results6;
	}
	public void execute() throws Throwable
	{
		int o=1,v=1,d=1;
		JSONObject jsonTrain2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",sdate);
		JSONObject jsonTrain1=TrainsBetweenStations.trainsBetweenStations(origin,via,"SL",sdate);
		JSONObject jsonTrain=TrainsBetweenStations.trainsBetweenStations(origin,destination,"SL",sdate);
		List<JSONObject> retvals5=Utils.concatanateRoutes(Utils.createResultTrain(jsonTrain1,sdate),Utils.createResultTrain(jsonTrain2,sdate),
		true,sdate,origin+"("+origin+")",via+"("+via+")",destination+"("+destination+")");//why sdate only for trains??
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
			List<JSONObject> retvals1= Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(jsonTrain2,sdate),true, 	               			        sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
			for(int i=0; i<retvals5.size(); i++)
			{					
				this.results5.add( retvals5.get(i) );
			}
			for(int i=0;i<retvals1.size();i++)
			{
				this.results1.add( retvals1.get(i) );
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		}
		else if(o==1&&v==0&&d==1)//what about the direct flights??
		{
		JSONObject jsonFlight=FlightsBetweenStations.flightsBetweenStations(origin,destination,sdate,edate);
		try
		{
			for(int i=0;i<retvals5.size();i++)
			{
				this.results5.add( retvals5.get(i) );
			}
		}finally{}//included finally instead of catch
		}
		else if(o==0&&v==1&&d==1)
		{
		JSONObject jsonFlight=FlightsBetweenStations.flightsBetweenStations(via,destination,sdate,edate);
		try
		{
			List<JSONObject> retvals2= Utils.concatanateRoutes(jsonFlight, Utils.createResultTrain(jsonTrain2,sdate),false,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");//note that bool is false
			for(int i=0;i<retvals2.size();i++)
			{
				this.results2.add( retvals2.get(i) );
			}
			for(int i=0;i<retvals5.size();i++)
			{
				this.results5.add( retvals5.get(i) );
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
			List<JSONObject> retvals1= Utils.concatanateRoutes(jsonFlight2,Utils.createResultTrain(jsonTrain2,sdate),true,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")");
			for(int i=0;i<retvals1.size();i++)
			{
				this.results1.add( retvals1.get(i) );
			}
			for(int i=0;i<retvals2.size();i++)
			{
				this.results2.add( retvals2.get(i) );
			}
			for(int i=0;i<retvals3.size();i++)
			{
				this.results3.add( retvals3.get(i) );
			}
			for(int i=0;i<retvals5.size();i++)
			{
				this.results5.add( retvals5.get(i) );
			}
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				for(int i=0;i<retvals5.size();i++)
				{
					this.results5.add( retvals5.get(i) );
				}
			}finally{}
		}
	}
}