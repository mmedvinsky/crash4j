/**
 * 
 */
package com.crash4j.engine;

/**
 * @author <MM>
 *
 */
public class UnknownResourceException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public UnknownResourceException()
    {
    }

    /**
     * @param arg0
     */
    public UnknownResourceException(String message)
    {
        super(message);
    }

    /**
     * @param arg0
     */
    public UnknownResourceException(Throwable e)
    {
        super(e);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public UnknownResourceException(String msg, Throwable e)
    {
        super(msg, e);
    }

}
