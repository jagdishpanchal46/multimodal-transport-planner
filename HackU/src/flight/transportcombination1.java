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
	public static void main(String args[]){
		String[][] places = { {"SC", "BBS", "HWH"}, {"HYD", "BBI", "CCU"} };
		String[] dates = {"20121128","20121230"};
		String classTrain = "3A";
		int[] flags = {1,1,1};
		printTransportCombinations(places, dates, classTrain, flags);
	}
	
	public static void printTransportCombinations(String[][] places, String date[], String classTrain, int[] flags ){
		//String from="HYD",via="LKO",to="KNU";
		//String from="JHS",via="CNB",to="LKO";
		
		String from = places[0][0], via= places[0][1], to=places[0][2];		
		String from1 = places[1][0], via1 = places[1][1], to1=places[1][2];
				
		String sdate = date[0] , edate = date[1];
		
		String train_class= classTrain;
		
		int o = flags[0],v = flags[1],d = flags[2];
		
		List< List<JSONObject> > results = transportcombinations(from, to, via, from1, to1, via1, classTrain, sdate, edate, o, v, d);
		
		try
		{
			JSONObject jsontrain=Utils.createResultTrain(TrainsBetweenStations.trainsBetweenStations(from,to,train_class,sdate),sdate);
			JSONObject all_dates1=jsontrain.getJSONObject("calendar_json");
			JSONArray trains=all_dates1.getJSONArray(sdate);
			for(int i=0;i<trains.length();i++)
			{
				JSONObject dates1=trains.getJSONObject(i);
				System.out.println("Train="+dates1.getString("aln"));
				System.out.println("Departs="+dates1.getString("dd")+"   "+dates1.getString("dt")+" from="+from);
				System.out.println("Arrives="+dates1.getString("ad")+"   "+dates1.getString("at")+" at="+to);
				System.out.println("fare="+dates1.getString("pr")+"\n\n");
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		try
		{
			if(o==1&&d==1)
			{
				JSONObject jsonflight=FlightsBetweenStations.flightsBetweenStations(from1,to1,sdate,sdate);
				JSONObject all_dates=jsonflight.getJSONObject("calendar_json");
				JSONArray flights=all_dates.getJSONArray(sdate);
				for(int k=0;k<flights.length();k++)
				{
					JSONObject dates=flights.getJSONObject(k);
					
					System.out.println("Airline="+dates.getString("al"));
					System.out.println("Departs="+sdate+" "+dates.getString("dt")+" from="+from);
					System.out.println("Arrives="+dates.getString("ad")+" "+dates.getString("at")+" at="+to);
					System.out.println("fare="+dates.getString("pr")+"\n\n");
				}
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		try
		{
			for(int i=0;i<results.size();i++)
			{
				List<JSONObject> result_at_i=results.get(i);
				for(int j=0;j<result_at_i.size();j++)
				{
					JSONObject routes=result_at_i.get(j);
					System.out.println("type:"+routes.getString("type"));
					System.out.print("mode 1:"+routes.getString("mode1")+"\t");
					System.out.println("mode 2:"+routes.getString("mode2"));
					System.out.println("from:"+routes.getString("from")+"\t"+"via:"+routes.getString("via")+"\t"+routes.getString("to"));
					System.out.print("departure date 1="+routes.getString("departure date 1")+"\t");
					System.out.println("departure time 1="+routes.getString("departure time 1"));
					System.out.print("arrival date 1="+routes.getString("arrival date 1")+"\t");
					System.out.println("arrival time 1="+routes.getString("arrival time 1"));
					System.out.print("departure date 2="+routes.getString("departure date 2")+"\t");
					System.out.println("departure time 2="+routes.getString("departure time 2"));
					System.out.print("arrival date 2="+routes.getString("arrival date 2")+"\t");
					System.out.println("arrival time 2="+routes.getString("arrival time 2"));
					System.out.println("halt time(in minutes):"+routes.getString("halting time(in min)"));
					System.out.print("price 1="+routes.getString("price 1")+"\t\t");
					System.out.println("price 2="+routes.getString("price 2"));
					System.out.println("total price="+routes.getString("total price"));
					System.out.println("\n");
					
				}
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void print(List<JSONObject> retval)
	{
		try{
			List<JSONObject> result_at_i = retval;
			for(int j=0;j<result_at_i.size();j++)
			{
				JSONObject routes=result_at_i.get(j);
				System.out.println("type:"+routes.getString("type"));
				System.out.print("mode 1:"+routes.getString("mode1")+"\t");
				System.out.println("mode 2:"+routes.getString("mode2"));
				System.out.println("from:"+routes.getString("from")+"\t"+"via:"+routes.getString("via")+"\t"+routes.getString("to"));
				System.out.print("departure date 1="+routes.getString("departure date 1")+"\t");
				System.out.println("departure time 1="+routes.getString("departure time 1"));
				System.out.print("arrival date 1="+routes.getString("arrival date 1")+"\t");
				System.out.println("arrival time 1="+routes.getString("arrival time 1"));
				System.out.print("departure date 2="+routes.getString("departure date 2")+"\t");
				System.out.println("departure time 2="+routes.getString("departure time 2"));
				System.out.print("arrival date 2="+routes.getString("arrival date 2")+"\t");
				System.out.println("arrival time 2="+routes.getString("arrival time 2"));
				System.out.println("halt time(in minutes):"+routes.getString("halting time(in min)"));
				System.out.print("price 1="+routes.getString("price 1")+"\t\t");
				System.out.println("price 2="+routes.getString("price 2"));
				System.out.println("total price="+routes.getString("total price"));
				System.out.println("\n");
				
			}
		} catch(JSONException e){
			// do nothing for now
		}
	}
	public static String date(int i, String sdate)
	{
		//String s=""; 
		//char[] date = sdate.toCharArray();
		
		int start_date = Integer.parseInt(sdate);		
		int m = (start_date%10000)/100;
				
		//System.out.println(m);
		if(m<12)
		{
			if(m==4||m==6||m==9||m==11)
			{
				if((start_date+i)%100>30)
				{
					start_date += (100-30+i);//we add (100-30)
					//System.out.println(i+'\n'+start_date);
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
		//System.out.println(s);
		return Integer.toString(start_date);
	}
	public static List< List<JSONObject> > transportcombinations(	String origin,
																	String destination,
																	String via,
																	String origin1,
																	String destination1,
																	String via1,
																	String classTrain,
																	String sdate,
																	String edate,
																	int o,int v,int d){
		
		String train_class = classTrain;
		//int end_date=Integer.parseInt(edate);
		//int start_date=Integer.parseInt(sdate);
		//int diff=end_date-start_date;
		List< List<JSONObject> > retval =  new ArrayList< List<JSONObject> >();
		
		//flight---->train
		List<JSONObject> retvals1 = new ArrayList<JSONObject>(), results1=new ArrayList<JSONObject>();
		
		//train----->flight
		List<JSONObject> retvals2 = new ArrayList<JSONObject>(), results2=new ArrayList<JSONObject>();
		
		//flight---->flight
		List<JSONObject> retvals3 = new ArrayList<JSONObject>(), results3=new ArrayList<JSONObject>();
		
		//train----->train
		List<JSONObject> retvals5 = new ArrayList<JSONObject>(), results5=new ArrayList<JSONObject>();				
		
		
		JSONObject jsonFlight1 = null;
		JSONObject jsonFlight2 = null;
		
		JSONObject jsonTrain1 = TrainsBetweenStations.trainsBetweenStations(origin,via,"SL",sdate);
		JSONObject jsonTrain2 = null;		
		
		/* // TODO Auto-generated catch block
		 * variable type(result,retvals)                          transport route
		 * 1                                                   flight---->train
		 * 2                                                   train----->flight
		 * 3												   flight---->flight
		 * 4												   flight
		 * 5												   train----->train
		 * 6												   train
		 */
		
		if(o==1 && v==1 && d==0)
		{
			jsonFlight1 = FlightsBetweenStations.flightsBetweenStations(origin1,via1,sdate,sdate);
			
			for(int k=0; !(date(k,sdate).equals(edate)); k++) {			
				/*start_date+=k;
				sdate=sdate.substring(0,6)+Integer.toString(start_date);*/
				
				jsonTrain2 = TrainsBetweenStations.trainsBetweenStations( via, destination, train_class, date(k,sdate) );
			 
				try{
					retvals1= Utils.concatanateRoutes(  jsonFlight1 , 
														Utils.createResultTrain(jsonTrain2, date(k,sdate) ),
														date(k,sdate),
														origin+"("+origin +")",
														via+"("+via+")", 
														destination+"("+destination+")",
														sdate,
														1
													 );
					//	in the date field of the above function should we send jsonFlight's date or date(k,sdate)
					//we need to create another field to send jsonFlight's date separately
					
					// TODO : temporarily printing all the results
					//results1.addAll(retvals1);
					print(retvals1);
					
					/**
					for(int i=0;i<retvals1.size();i++)
					{
						results1.add( retvals1.get(i) );						
					}**/
				}
				catch(JSONException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		else if(o==0&&v==1&&d==1)
		{
			jsonFlight1=FlightsBetweenStations.flightsBetweenStations(via1,destination1,sdate,edate);
			
			jsonTrain1=TrainsBetweenStations.trainsBetweenStations(origin1,via1,train_class,sdate);
			//for(int k=0;k<=diff;k++)
			//{
				/*start_date+=k;
				sdate=sdate.substring(0,6)+Integer.toString(start_date);*/
				//JSONObject json_Train2=TrainsBetweenStations.trainsBetweenStations(via,destination,"SL",date(k,sdate));
			try
			{
				retvals2= Utils.concatanateRoutes(jsonFlight1, Utils.createResultTrain(jsonTrain1,sdate),sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")",sdate,2);//note that bool is false
				
				//results2.addAll(retvals2);
				print(retvals2);
				
				/**
				for(int i=0;i<retvals2.size();i++)
				{
					results2.add( retvals2.get(i) );
				}
				*/
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
			//}
		}
		
		else if(o==1&&v==1&&d==1)
		{
			//System.out.println(sdate+" "+edate);
			//jsonFlight1 = FlightsBetweenStations.flightsBetweenStations(origin1,destination1,sdate,sdate);
			
			jsonFlight2 = FlightsBetweenStations.flightsBetweenStations(origin1,via1,sdate,sdate);
			
			JSONObject jsonFlight3 = FlightsBetweenStations.flightsBetweenStations(via1,destination1,sdate,edate);
			
			//System.out.println(jsonFlight3);
			try
			{
				//retvals3= Utils.concatanateRoutes(jsonFlight2,jsonFlight3,sdate,origin1+"("+origin1 +")",via1+"("+via1+")", destination1+"("+destination1+")",sdate,3);
				//System.out.println(retvals3);
				
				JSONObject json_train1 = TrainsBetweenStations.trainsBetweenStations(origin,via,train_class,sdate);
				json_train1 = Utils.createResultTrain(json_train1,sdate);
				//retvals2= Utils.concatanateRoutes(Utils.createResultTrain(json_train1,sdate),jsonFlight3,sdate,origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")",sdate,2);
				for(int k=0;!(date(k,sdate).equals(edate));k++)
				{
					retvals2 = Utils.concatanateRoutes(json_train1, jsonFlight3, date(k,sdate), origin+"("+origin +")", via+"("+via+")", destination+"("+destination+")", sdate, 2);										
					retvals3 = Utils.concatanateRoutes(jsonFlight2,jsonFlight3,date(k,sdate),origin1+"("+origin1 +")",via1+"("+via1+")", destination1+"("+destination1+")",sdate,3);
					
					JSONObject json_Train2 = TrainsBetweenStations.trainsBetweenStations(via,destination,train_class,date(k,sdate));					
					retvals1= Utils.concatanateRoutes(jsonFlight2,Utils.createResultTrain(json_Train2,date(k,sdate)),date(k,sdate),origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")",sdate,1);
										
					//results1.addAll(retvals1);
					print(retvals1);
					//results2.addAll(retvals2);
					print(retvals2);
					//results3.addAll(retvals3);
					print(retvals3);
					
					/**
					for(int i=0;i<retvals1.size();i++)
					{
						results1.add( retvals1.get(i) );
					}
					for(int i=0;i<retvals2.size();i++)
					{
						results2.add( retvals2.get(i) );
					}**/
				}
				
				/**
				for(int i=0;i<retvals3.size();i++)
				{
					results3.add( retvals3.get(i) );
				}**/
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
		}

		try{
			jsonTrain1 = Utils.createResultTrain(jsonTrain1,sdate);
			for(int k=0;!(date(k,sdate).equals(edate));k++)
			{
				/*start_date+=k;
				sdate1=sdate.substring(0,6)+Integer.toString(start_date);;*/
				//System.out.println(!(date(k,sdate).equals(edate)));
				jsonTrain2=TrainsBetweenStations.trainsBetweenStations(via,destination,train_class,date(k,sdate));						
				
					retvals5 = Utils.concatanateRoutes(jsonTrain1, Utils.createResultTrain(jsonTrain2, date(k,sdate)),date(k,sdate),origin+"("+origin +")",via+"("+via+")",destination+"("+destination+")",sdate,4);
					//retvals5=Utils.concatanateRoutes(Utils.createResultTrain(jsonTrain1,sdate),Utils.createResultTrain(jsonTrain2, date(k,sdate)),true,date(k,sdate),origin+"("+origin +")",via+"("+via+")", destination+"("+destination+")",sdate);
					//System.out.println(retvals5);
					/**
					for(int i=0;i<retvals5.size();i++)
					{
						//retvals5.get
						//print(retvals5.get(i),date(k,sdate),origin,destination);
						results5.add( retvals5.get(i));
					}**/
					//results5.addAll(retvals5);
					print(retvals5);
			}
		} catch(JSONException e){
			e.printStackTrace();
		}

		
		//results is list of JSON object
		//therefore retval is list of list of JSON objects
		
		//TODO: commented temporarily
		//retval.add(results1);
		//retval.add(results2);
		//retval.add(results3);
		//retval.add(results5);
	
		return retval;
		
	}
}