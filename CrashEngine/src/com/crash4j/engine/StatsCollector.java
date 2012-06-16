/**
 * 
 */
package com.crash4j.engine;

import com.crash4j.engine.spi.stats.Monitor;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;
import com.crash4j.engine.types.classtypes.IOTypes;
/**
 * Collector object that is registered by the caller, 
 * and asynchronously 
 */
public interface StatsCollector
{
    /**
     * @return last access time in milliseconds.
     */
    public long getLastAccessTime();
    
    /**
     * Called to start the collector
     */
    public void start();
    
    /**
     * Called to stop the collector.
     */
    public void stop();
    
    /**
     * Begin collection
     */
    public void begin();
    
    /**
     * End collection
     */
    public void end();
    
    /**
     * Called when resource is entered during collection
     * @param res resource that is entered
     */
    public void enterResource(Resource res);
    
    /**
     * Called when resource is exited during collection
     * @param res that is exited
     */
    public void exitResource(Resource res);
    
    /**
     * This method is very specifically disconnected by basic data types from the 
     * rest of the code in order to allow for the implementor to not to depend on crash4j development artifacts
     * @param resource is a {@link String} representation of the resource.
     * @param action is a {@link IOTypes} string representation.
     * @param stype is a {@link StatTypes} string representation.
     * @param utype is a {@link UnitTypes} string representation
     * @param count is a total count of submissions against this monitor
     * @param max is a maximum value of sample parameter
     * @param min is a minimal value of sample parameter
     * @param utime is a last time the {@link Monitor} was updated
     * @param lastValue is a last value that was submitted 
     * @param sample is an actual value of this monitor.
     */
    public void submitStats(Resource resource, 
                            Action action, 
                            StatTypes stype, 
                            UnitTypes utype, 
                            long totaltime,
                            long count,
                            double max, 
                            double min, 
                            long utime, 
                            double lastValue,
                            double sample);
}
