/**
 * @copyright
 */
package com.crash4j.engine.spi.remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.crash4j.engine.ResourceService;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.context.ThreadContext;
import com.crash4j.engine.spi.json.JSONArray;
import com.crash4j.engine.spi.json.JSONBufferedStatsCollector;
import com.crash4j.engine.spi.json.JSONException;
import com.crash4j.engine.spi.json.JSONObject;
import com.crash4j.engine.spi.json.JSONTokener;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;

/**
 * {@link CrashServiceAdapter} handles all communications 
 * between running JVM and remote instance of CrashService
 * 
 * The connection management of remote communications and its style 
 * is enforced here...
 * 
 * 
 * @author team
 *
 */
public class CrashServiceAdapter extends JSONBufferedStatsCollector implements Runnable
{
    protected Log log = LogFactory.getLog(CrashServiceAdapter.class);
    protected String host = null;
    protected int port;
    protected String apikey;
    protected String cs = null;
    protected ScheduledExecutorService refresher = Executors.newScheduledThreadPool(1);
    protected ResourceService engineService = new ResourceService();
    
    /**
     * Initialization constructor
     * @param host
     * @param port
     * @param apikey
     */
    public CrashServiceAdapter(String host, int port, String apikey)
    {
        this.host = host;
        this.port = port;
        this.apikey = apikey;
    }
    
    /**
     * Initialization constructor
     * @param host
     * @param port
     * @param apikey
     */
    public CrashServiceAdapter()
    {
    }
    
    
    
    /**
     * @return the host
     */
    public String getHost()
    {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host)
    {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * @return the apikey
     */
    public String getApikey()
    {
        return apikey;
    }

    /**
     * @param apikey the apikey to set
     */
    public void setApikey(String apikey)
    {
        this.apikey = apikey;
    }

    /**
     * Send the current payload to the server
     * @param arr
     * @throws IOException 
     */
    protected void sendMessages(JSONArray arr) throws IOException
    {
        ThreadContext.beginIgnore();
        try
        {
            String payload = arr.toString();
            if (arr.length() == 0)
            {
            	return;
            }
            URL u = new URL("http", this.host, this.port, "/crash4j/core/services/events");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.addRequestProperty("If-Match", cs);
            conn.addRequestProperty("Accept", "application/json");
            conn.addRequestProperty("Content-type", "application/json");
            conn.addRequestProperty("Content-length", Integer.toString(payload.length()));
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(payload);
            osw.flush();
            int code = conn.getResponseCode();
            if (code >= 400)
            {
            	log.logError("Http communications with server failed: "+code);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
        }
    }
    
    /**
     * Disconnect from the remote server.
     * @throws IOException
     */
    public void disconnect() throws IOException
    {
        ThreadContext.beginIgnore();
        try
        {
            StringBuilder b = new StringBuilder("/crash4j/core/services/shutdown");
            URL u = new URL("http", this.host, this.port, b.toString());
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.addRequestProperty("If-Match", cs);
            InputStream iis = conn.getInputStream();
            iis.close();
        }
        finally
        {
            ThreadContext.endIgnore();
        }
    }
    /**
     * Connect to remote server and establish a session.
     * @throws IOException
     */
    public void connect() throws IOException
    {
        ThreadContext.beginIgnore();
        try
        {
            StringBuilder b = new StringBuilder("/crash4j/core/services/init/");
            b.append(this.apikey);
            URL u = new URL("http", this.host, this.port, b.toString());
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            InputStream iis = conn.getInputStream();
            
            try
            {
                JSONObject response = new JSONObject(new JSONTokener(iis));
                cs = response.getString("sessionid");
                this.refresher.scheduleAtFixedRate(this, 0, 5000, TimeUnit.MILLISECONDS);
            } 
            catch (JSONException e)
            {
                throw new IOException(e);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
        }
    }

    
    /**
     * Connect to remote server and establish a session.
     * @throws IOException
     */
    public void getAssignedSimulations() throws IOException
    {
        ThreadContext.beginIgnore();
        try
        {
            StringBuilder b = new StringBuilder("/crash4j/core/services/simulation/update");
            URL u = new URL("http", this.host, this.port, b.toString());
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.addRequestProperty("If-Match", cs);
            InputStream iis = conn.getInputStream();
            JSONArray sims = new JSONArray(new JSONTokener(iis));
            iis.close();
            for (int i = 0; i < sims.length(); i++) 
            {
            	JSONObject sim = sims.getJSONObject(i);
                engineService.addSimulation(sim.toString());
                engineService.startSimulation(sim.getString("id"));
			}
        }
        catch (Exception e)
        {
        	throw new IOException(e);
        }
        finally
        {
            ThreadContext.endIgnore();
        }
    }
    
    
	@Override
	public void run() 
	{
		try
		{
			JSONArray response = null;
	        ThreadContext.beginIgnore();
	        try
	        {
	            StringBuilder b = new StringBuilder("/crash4j/core/services/commands");
	            URL u = new URL("http", this.host, this.port, b.toString());
	            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
	            conn.addRequestProperty("If-Match", cs);
	            InputStream iis = conn.getInputStream();
                response = new JSONArray(new JSONTokener(iis));
	            iis.close();
	        }
	        finally
	        {
	            ThreadContext.endIgnore();
	        }
	        
	        if (response != null)
	        {
		        for (int i = 0; i < response.length(); i++)
		        {
		        	if (response.getString(i).equalsIgnoreCase("sim"))
		        	{
		        		//Update simulations
		        		getAssignedSimulations();
		        	}
		        }
	        }
		}
		catch (Exception e)
		{
			log.logError("Failed to get server commands", e);
		}
	}
 }
