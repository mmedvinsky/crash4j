/**
 * 
 */
package com.crash4j.engine.spi.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author <MM>
 *
 */
public class LogFactory
{
    protected static FileOutputStream logfile = null;
    protected static File logf = null;
    protected static int mask = 0;
    protected static LogWriter lw = null;
    
    public static final int LOG_ERROR = 2;
    public static final int LOG_THROWABLE = 4;
    public static final int LOG_TRACE = 8;
    public static final int LOG_INFO = 16;
    
    
    static class _log implements Log
    {
        String name = null;
        _log(String name)
        {
            this.name = name;
        }
        @Override
        public void logThrowable(Throwable e)
        {
            if ((mask & 4) == 4)
            {
                StringBuilder b = new StringBuilder("X").append("\t").append(this.name);
                b.append("\t").append(Thread.currentThread().getId()).append("\t").append(System.currentTimeMillis());
                b.append("\t").append(e.getClass().getName()).append("\t").append(e.getMessage()).append("\n");
                lw.write(b.toString());
            }
        }

        @Override
        public void logError(String message, Throwable e)
        {
            if ((mask & 2) == 2)
            {
                StringBuilder b = new StringBuilder("E").append("\t").append(this.name);
                b.append("\t").append(Thread.currentThread().getId()).append("\t").append(System.currentTimeMillis());
                b.append("\t").append(e.getClass().getName()).append("\t").append(message).append("\n");
                lw.write(b.toString());
            }
        }

        @Override
        public void logError(Object ...message)
        {
            if ((mask & 2) == 2)
            {
                StringBuilder b = new StringBuilder("E").append("\t").append(this.name);
                b.append("\t").append(Thread.currentThread().getId()).append("\t").append(System.currentTimeMillis());
                b.append("\t");
                for (Object s : message) 
                {
                    b.append(s).append(" ");
				}
                b.append("\n");                
                lw.write(b.toString());
            }
        }

        @Override
        public void logTrace(Object ...message)
        {
            if ((mask & 8) == 8)
            {
                StringBuilder b = new StringBuilder("T").append("\t").append(this.name);
                b.append("\t").append(Thread.currentThread().getId()).append("\t").append(System.currentTimeMillis());
                b.append("\t");
                for (Object s : message) 
                {
                    b.append(s.toString()).append(" ");
				}
                b.append("\n");
                lw.write(b.toString());
            }
        }

        
        @Override
        public void logInfo(Object ...message)
        {
            if ((mask & 16) == 16)
            {
                StringBuilder b = new StringBuilder("E").append("\t").append(this.name);
                b.append("\t").append(Thread.currentThread().getId()).append("\t").append(System.currentTimeMillis());
                b.append("\t");
                for (Object s : message) 
                {
                    b.append(s.toString()).append(" ");
				}
                b.append("\n");
                lw.write(b.toString());
            }
        }        
    }
    
    /**
     * Initialize log space
     * @param logname
     * @throws IOException 
     */
    public static void init(String logname) throws IOException
    {
        if (logname == null)
        {
            logf = File.createTempFile("log4j", ".log");
        }
        else
        {
            logf = new File(logname);
        }
        logfile = new FileOutputStream(logf);
        lw = new LogWriter(logfile);
    }
    
    /**
     * Initialize log space
     * @param logname
     * @throws IOException 
     */
    public static void init(File lf) throws IOException
    {
        if (lf == null)
        {
            logf = File.createTempFile("log4j", ".log");
        }
        else
        {
            logf = lf;
        }
        logfile = new FileOutputStream(logf);
        lw = new LogWriter(logfile);
    }
    
    /**
     * Initialize log space
     * @param logname
     * @throws IOException 
     */
    public static void close() throws IOException
    {
        logfile.close();
    }
    
    public static Log getLog(String area)
    {
        return new _log(area);
    }
    public static Log getLog(Class area)
    {
        return new _log(area.getName());
    }

    /**
     * @return the mask
     */
    public static int getMask()
    {
        return mask;
    }

    /**
     * @param mask the mask to set
     */
    public static void setMask(int mask)
    {
        LogFactory.mask = mask;
    }
    
}
