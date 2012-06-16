/**
 * 
 */
package com.crash4j.junit.report;

import java.util.concurrent.Callable;

import com.crash4j.Stat;
import com.crash4j.engine.client.json.JSONArray;
import com.crash4j.engine.client.json.JSONObject;

/**
 * Prepare the {@link Stat} data for charting based on 
 * the resource identifier.
 */
public class ChartAggregate implements Callable<JSONObject> 
{
	protected JSONArray ja = null;
	/**
	 * Default constructor
	 */
	public ChartAggregate(JSONArray array) 
	{
		ja = array;
	}

	
	
	/**
	 * Perform aggregation for charting
	 */
	@Override
	public JSONObject call() throws Exception 
	{
		JSONObject output = new JSONObject();
		for (int i = 0; i < ja.length(); i++)
		{
			JSONObject co = ja.optJSONObject(i);
			String etag = co.optString("id");
			
			if (etag.trim().isEmpty())
			{
				continue;
			}
			
			JSONObject record = output.optJSONObject(etag);
			if (record == null)
			{
				record = new JSONObject();
				record.put("id", etag);				
				record.put("toff", Long.MAX_VALUE);				
				output.put(etag, record);
			}
			
			JSONObject stat = co.optJSONObject("stat");
			if (stat != null)
			{
				JSONArray actions = stat.names();
				for (int a = 0; a < actions.length(); a++)
				{
					String actionS = actions.getString(a);
					JSONObject actionObj = stat.optJSONObject(actionS);
					JSONObject recordedAction = record.optJSONObject(actionS);
					if (recordedAction == null)
					{
						recordedAction = new JSONObject();
						record.put(actionS, recordedAction);
						JSONArray da = new JSONArray();
						da.put(new JSONArray()); //time
						da.put(new JSONArray()); //realtime
						da.put(new JSONArray()); //throughput
						da.put(new JSONArray()); //errors
						recordedAction.put("data", da);
					}
					
					JSONArray data = recordedAction.optJSONArray("data");

					JSONObject timeO = actionObj.optJSONObject("time");
					if (timeO != null)
					{
						long tm = timeO.optLong("timestamp");
						if (record.optLong("toff") >= tm)
						{
							record.put("toff", tm);
						}
						JSONArray tuple = new JSONArray();
						tuple.put(tm);
						tuple.put(timeO.optDouble("avg"));
						data.optJSONArray(0).put(tuple);
					}
					JSONObject realtimeO = actionObj.optJSONObject("actualtime");
					if (realtimeO != null)
					{
						long tm = realtimeO.optLong("timestamp");
						if (record.optLong("toff") >= tm)
						{
							record.put("toff", tm);
						}
						JSONArray tuple = new JSONArray();
						tuple.put(tm);
						tuple.put(realtimeO.optDouble("avg"));
						data.optJSONArray(1).put(tuple);
					}
					JSONObject throughputO = actionObj.optJSONObject("throughput");
					if (throughputO != null)
					{
						long tm = throughputO.optLong("timestamp");
						if (record.optLong("toff") >= tm)
						{
							record.put("toff", tm);
						}
						JSONArray tuple = new JSONArray();
						tuple.put(tm);
						tuple.put(throughputO.optDouble("avg"));
						data.optJSONArray(2).put(tuple);
					}
					JSONObject errorsO = actionObj.optJSONObject("error");
					if (errorsO != null)
					{
						long tm = errorsO.optLong("timestamp");
						if (record.optLong("toff") >= tm)
						{
							record.put("toff", tm);
						}
						JSONArray tuple = new JSONArray();
						tuple.put(tm);
						tuple.put(errorsO.optLong("count"));
						data.optJSONArray(3).put(tuple);
					}
				}
			}
		}
		//Second path to offset times 
		//and remove empty stats
		JSONArray arr = output.names();
		for (int i = 0; i < arr.length(); i++)
		{
			String etag = arr.getString(i);
			JSONObject rec = output.optJSONObject(etag);
			long toff = rec.optLong("toff");
			JSONArray actions = rec.names();
			for (int j = 0; j < actions.length(); j++)
			{
				String aname = actions.optString(j);
				JSONObject obj = rec.optJSONObject(aname);
				JSONArray data = null;
				if (obj != null)
				{
					data = obj.optJSONArray("data");
				}
				if (data == null)
				{
					continue;
				}
				for (int k = 0; k < data.length(); k++)
				{
					JSONArray types = data.optJSONArray(k);
					for (int l = 0; l < types.length(); l++)
					{
						JSONArray tuple = types.optJSONArray(l);
						long ty = tuple.getLong(0);
						tuple.put(0, ty - toff);
					}
				}
			}
		}
		return output;
	}
}
