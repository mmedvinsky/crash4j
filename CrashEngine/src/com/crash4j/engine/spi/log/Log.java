/**
 * 
 */
package com.crash4j.engine.spi.log;

/**
 * Log interface defines all types of logging that can be done.
 * @author <MM>
 *
 */
public interface Log
{
    /**
     * Logs exception as it happens.  Will print stack trace is asked.
     */
    public void logThrowable(Throwable e);
    /**
     * Log error message and optional exception 
     * @param message
     * @param e
     */
    public void logError(String message, Throwable e);
    /**
     * Log error message and optional exception 
     * @param message
     * @param e
     */
    public void logError(Object ...message);
    
    /**
     * Log trace levelk message
     * @param message
     */
    public void logTrace(Object ...message);
    
    /**
     * Log informational message
     * @param message
     */
    public void logInfo(Object ...message);
}
