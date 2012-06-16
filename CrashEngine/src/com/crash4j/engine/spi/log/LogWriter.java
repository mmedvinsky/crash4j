/**
 * 
 */
package com.crash4j.engine.spi.log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author <MM>
 *
 */
public class LogWriter
{
    protected FileOutputStream fos = null;
    
    /**
     * {@link FileInputStream} overload that will only use native read
     * @param fs
     */
    public LogWriter(FileOutputStream fs)
    {
        fos = fs;
    }
    
    /**
     * Write bypassing the instrumentation
     * @param chars
     */
    public synchronized void write(CharSequence chars)
    {
        for (int i = 0; i < chars.length(); i++)
        {
            try
            {
                fos.write(chars.charAt(i));
            } 
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
    }
    /**
     * Write bypassing the instrumentation
     * @param chars
     */
    public synchronized void write(int v)
    {
        write(v+"");
    }
    /**
     * Write bypassing the instrumentation
     * @param chars
     */
    public synchronized void write(char v)
    {
        write(v+"");
    }
    /**
     * Write bypassing the instrumentation
     * @param chars
     */
    public synchronized  void write(long v)
    {
        write(v+"");
    }
}
