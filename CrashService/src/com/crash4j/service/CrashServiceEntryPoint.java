/**
 * @copyright
 */
package com.crash4j.service;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.crash4j.service.session.CrashSession;
import com.crash4j.service.session.CrashSessionManager;
import com.crash4j.service.session.Stat;
import com.crash4j.service.session.StatHolder;

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
    public CrashSession getResources(@PathParam("etag") String etag)
    {
        CrashSession session = manager.getSession(etag);
        if (session == null)
        {
            throw new WebApplicationException(412);
        }
        return session;
    }
    
    @GET @Path("/resources/stats/{etag}/{resid}") @Produces({"application/json"})
    public StatHolder getResourceStats(@PathParam("etag") String etag, @PathParam("resid") String rid)
    {
        CrashSession session = manager.getSession(etag);
        if (session == null)
        {
            throw new WebApplicationException(412);
        }
        return new StatHolder(session,rid);
    }
    

    @GET @Path("/init/{apikey}") @Produces({"application/json"})
    public JSONObject init(@PathParam("apikey") String apikey)
    {
        //Api Key should be associated with profile.  
        CrashSession session = manager.newSession();
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
    
    
    @POST @Path("/events") @Consumes({"application/json"})
    public JSONObject messageHandler(@HeaderParam("If-Match") String etag, 
            JSONArray data)
    {  
        CrashSession session = manager.getSession(etag);
        if (session == null)
        {
            throw new WebApplicationException(412);
        }
        manager.submit(session, data);
        return new JSONObject();
    }
    
    
    
    @GET @Path("/shutdown") @Produces({"application/json"})
    public JSONObject shutdown(@HeaderParam("If-Match") String etag)
    {
        //Api Key should be associated with profile.  
        manager.releaseSession(etag);
        return new JSONObject();
    }
}
