/**
 * @copyright
 */
package com.crash4j;

/**
 * Base engine exception
 */
public class EngineException extends Exception
{
    /**
     * long EngineException.java
     */
    private static final long serialVersionUID = -5210787010966922590L;

    /**
     * 
     */
    public EngineException()
    {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public EngineException(String m, Throwable c)
    {
        super(m, c);
    }

    /**
     * @param arg0
     */
    public EngineException(String m)
    {
        super(m);
    }

    /**
     * @param arg0
     */
    public EngineException(Throwable c)
    {
        super(c);
    }

}
