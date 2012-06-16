/**
 * @copyright
 */
package com.crash4j.service.session;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.management.MalformedObjectNameException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

/**
 * Manages multiple crash sessions.
 * @author team
 *
 */
public class CrashSessionManager
{
    protected static final Log log = LogFactory.getLog(CrashSessionManager.class);
    
    protected ConcurrentHashMap<String, CrashSession> sessions = new ConcurrentHashMap<String, CrashSession>();
    protected Thread[] handlers = null;
    protected ArrayBlockingQueue<_data_rec> queue = null;
    protected int queueDepth = 1000;
    protected int numberOfThreads = 10;
    protected volatile boolean stopall = false;

    
    class _data_rec
    {
        JSONArray data;
        CrashSession session = null;
        /**
         * @param data
         * @param session
         */
        public _data_rec(JSONArray data, CrashSession session)
        {
            this.data = data;
            this.session = session;
        }
    }
    
    /**
     * {@link CrashSessionManager} constructor
     * @param maxSessions
     */
    public CrashSessionManager()
    {
    }
    
    /**
     * @return the queueDepth
     */
    public int getQueueDepth()
    {
        return queueDepth;
    }

    /**
     * @param queueDepth the queueDepth to set
     */
    public void setQueueDepth(int queueDepth)
    {
        this.queueDepth = queueDepth;
    }

    /**
     * @return the numberOfThreads
     */
    public int getNumberOfThreads()
    {
        return numberOfThreads;
    }

    /**
     * @param numberOfThreads the numberOfThreads to set
     */
    public void setNumberOfThreads(int numberOfThreads)
    {
        this.numberOfThreads = numberOfThreads;
    }

    /**
     * Start the manager operations
     */
    public void start()
    {
        this.queue = new ArrayBlockingQueue<_data_rec>(this.queueDepth);
        this.handlers = new Thread[this.numberOfThreads];
        LinkedList<Callable<Boolean>> tasks = new LinkedList<Callable<Boolean>>();
        for (int i = 0; i < this.numberOfThreads; i++)
        {
            Runnable call = new Runnable()
            {
                @Override
                public void run()
                {
                    while (!stopall)
                    {
                        try
                        {
                            _data_rec item = CrashSessionManager.this.queue.poll(100, TimeUnit.MILLISECONDS);
                            if (item == null)
                            {
                                continue;
                            }
                            else
                            {
                                item.session.submit(item.data);
                            }
                            
                        }
                        catch (InterruptedException e)
                        {
                            return;
                        } catch (MalformedObjectNameException e)
                        {
                            log.error("", e);
                        } catch (IllegalStateException e)
                        {
                            log.error("", e);
                        } catch (NullPointerException e)
                        {
                            log.error("", e);
                        } catch (JSONException e)
                        {
                            log.error("", e);
                        }
                    }
                }
            };
            this.handlers[i] = new Thread(call);
            this.handlers[i].start();
        }
    }
    
    public void stop()
    {
        stopall = true;
    }
    
    /**
     * 
     * @param session
     * @param data
     */
    public void submit(CrashSession session, JSONArray data)
    {
        int retry = 3;
        while (retry > 0)
        try
        {
            this.queue.add(new _data_rec(data, session));
            return;
        }
        catch (IllegalStateException e)
        {
            //Here is where the queue is empty
            this.queue.poll();
            retry--;
        }
        throw new IllegalStateException("No space left in the queue.");
        
    }
    
    /**
     * Session creation
     * @param host
     * @param name
     * @return
     */
    public CrashSession newSession()
    {
        CrashSession sess = CrashSession.newSession();
        sessions.put(sess.getEtag(), sess);
        return sess;
    }
    /**
     * Get an instance of session
     * @param etag
     * @return
     */
    public CrashSession getSession(String etag)
    {
        return this.sessions.get(etag);
    }
    
    /**
     * releaseSession
     * @param etag
     * @return
     */
    public CrashSession releaseSession(String etag)
    {
        return this.sessions.remove(etag);
    }
}
