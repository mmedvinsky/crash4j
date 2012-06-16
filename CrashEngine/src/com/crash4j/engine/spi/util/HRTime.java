/**
 * 
 */
package com.crash4j.engine.spi.util;


/**
 * Utility functions for high resolution timeouts
 * @author <MM>
 *
 */
public class HRTime
{
    public static long loopfactor = 1000;
    /**
     * System defendant collaboration to achieve nanosecond precision delays.
     * @param turns
     */
    public static void callibrate(long turns)
    {
        long startNano = System.nanoTime();
        for (int i = 0; i < turns; i++)
        {
            Thread.yield();
        }
        long endNano = System.nanoTime() - startNano;
        loopfactor = endNano/turns;
    }
    /**
     * delay for a said number of nanoseconds
     * @param dt time in nanoseconds to delay.
     * @return long number of nanoseconds elapsed
     */
    public static void delay(long ns)
    {
        //try
        //{
    	    //System.out.println("delay for "+(ns));
            long x = System.nanoTime();
            while ((System.nanoTime() - x) < ns)
            {
                Thread.yield();
            }
            //long turns = (ns*loopfactor)/2000;
            //for (int i = 0; i < loopfactor; i++)
            //{
            //    Thread.yield();
            //}
            //TimeUnit.NANOSECONDS.sleep(ns);
        //} 
        //catch (InterruptedException e)
        //{
        //}
    }
}
