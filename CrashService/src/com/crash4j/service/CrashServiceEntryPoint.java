/**
 * @copyright
 */
package com.crash4j.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.crash4j.service.session.CrashSession;
import com.crash4j.service.session.CrashSessionManager;

/**
 * @author team
 *
 */
@Path("/services")
public class CrashServiceEntryPoint
{    
    //This number can not e hardcoded.
    @Autowired
    public CrashSessionManager manager = null;
    public ConcurrentHashMap<String, ConcurrentSkipListSet<String>> apiKeySessions = 
    		new ConcurrentHashMap<String, ConcurrentSkipListSet<String>>();
    
    //@Context Request request
    
    /**
     * @return the manager
     */
    public CrashSessionManager getManager()
    {
        return manager;
    }

    /**
     * @param manager the manager to set
     */
    public void setManager(CrashSessionManager manager)
    {
        this.manager = manager;
    }
    
    @GET @Path("/resources/{etag}") @Produces({"application/json"})
    public JSONArray getResources(@PathParam("etag") String etag)
    {
        CrashSession session = manager.getSession(etag);
        if (session == null)
        {
            throw new WebApplicationException(412);
        }
        JSONArray arr = new JSONArray();
        Collection<JSONObject> coll =  session.getResources();
        for (JSONObject jsonObject : coll) {
        	arr.put(jsonObject);
		}
        return arr;
    }
    
    @GET @Path("/resources/stats/{etag}/{resid}") @Produces({"application/json"})
    public JSONObject getResourceStats(@PathParam("etag") String etag, @PathParam("resid") String rid)
    {
        CrashSession session = manager.getSession(etag);
        if (session == null)
        {
            throw new WebApplicationException(412);
        }
        return session.getResourceById(rid);
    }
    

    @GET @Path("/init/{apikey}") @Produces({"application/json"})
    public JSONObject init(@PathParam("apikey") String apikey)
    {
        //Api Key should be associated with profile.  
        CrashSession session = manager.newSession();
        //Add to apikey collections
        ConcurrentSkipListSet<String> sessions = apiKeySessions.get(apikey);
        if (sessions == null)
        {
        	sessions = new ConcurrentSkipListSet<String>();
        	apiKeySessions.put(apikey, sessions);
        }
        sessions.add(session.getEtag());
        
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("sessionid", session.getEtag());
            return obj;
        } catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @GET @Path("/sessions/{apikey}") @Produces({"application/json"})
    public JSONArray getSessions(@PathParam("apikey") String apikey)
    {
        //Api Key should be associated with profile.  
    	ConcurrentSkipListSet<String> sessions = apiKeySessions.get(apikey);
        JSONArray obj = new JSONArray();
        if (sessions != null)
        {
	        for (String s : sessions) 
	        {
				obj.put(s);
			}
        }
        return obj;
    }
    
    
    @POST @Path("/events") @Consumes({"application/json"})
    public JSONObject messageHandler(@HeaderParam("If-Match") String etag, 
            JSONArray data)
    {  
        CrashSession session = manager.getSession(etag);
        if (session == null)
        {
            throw new WebApplicationException(412);
        } 
        System.out.println(data.length());
        manager.submit(session, data);
        return new JSONObject();
    }

    @POST @Path("/simulation/upload") 
    public JSONObject addSimulation(@FormParam("etag") String etag, 
    		@FormParam("simulation") String sim) 
    {  
    	try
    	{	    	
	    	JSONObject simulation = new JSONObject(sim);
	        CrashSession session = manager.getSession(etag);
	        if (session == null)
	        {
	            throw new WebApplicationException(412);
	        }
	        session.addSimulation(simulation);
	        return new JSONObject();
    	}
    	catch (Exception e)
    	{
    		
    	}
    	return null;
    }

    @GET @Path("/simulation/update")
    public JSONArray getSimulation(@HeaderParam("If-Match") String etag)
    {  
        CrashSession session = manager.getSession(etag);
        if (session == null)
        {
            throw new WebApplicationException(412);
        }
        JSONArray param = new JSONArray();
        session.getSimulationForDistribution(param);
        return param;
    }

    
    @GET @Path("/commands")
    public JSONArray getUpdates(@HeaderParam("If-Match") String etag)
    {  
        CrashSession session = manager.getSession(etag);
        if (session == null)
        {
            throw new WebApplicationException(412);
        }
        return session.updateCommands();
    }
    
    
    @GET @Path("/shutdown") @Produces({"application/json"})
    public JSONObject shutdown(@HeaderParam("If-Match") String etag)
    {
        //Api Key should be associated with profile.  
        manager.releaseSession(etag);
        return new JSONObject();
    }
}
