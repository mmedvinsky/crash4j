/**
 * 
 */
package com.crash4j.engine.spi.instrument.delegates;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceAware;
import com.crash4j.engine.spi.util.ArrayUtil;

/**
 * Delegate method used to wrap {@link InputStream} instances when needed
 * 
 * @author <MM>
 *
 */
public class java_io_InputStream extends InputStream implements ResourceAware
{
    protected InputStream is = null;
    protected WeakReference<Object> owner = null;
    protected ResourceSpec ownerSpec = null;
    //9 methods in order
    protected ResourceSpec specs[] = new ResourceSpec[4];
    protected ResourceSpi resource;
    protected Log log = LogFactory.getLog(java_io_InputStream.class);
    
    /**
     * @return the resource
     */
    public ResourceSpi getResource()
    {
        return resource;
    }

    /**
     * @param resource the resource to set
     */
    public void setResource(ResourceSpi resource)
    {
        this.resource = resource;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        return is.equals(obj);
    }

    /**
     * @see java.lang.Object#finalize()
     */

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return is.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return is.toString();
    }

    /**
     * Delegating constructor
     */
    public java_io_InputStream(Object is, ResourceSpec spec, Object owner)
    {
        this.is = (InputStream)is;
        this.ownerSpec = spec;
        this.owner = new WeakReference<Object>(owner);
        
        //Prepare all specifications for this stub.
        //java.net.Socket!java.io.InputStream#read()I;=4
        StringBuffer b = new StringBuffer(spec.getEntityName());
        b.append("!").append("java.io.InputStream").append("#");
        
        specs[0] = ResourceManagerSpi.getByFullSignature(b.toString()+"read()I;");
        specs[1] = ResourceManagerSpi.getByFullSignature(b.toString()+"close()V;");
        specs[2] = ResourceManagerSpi.getByFullSignature(b.toString()+"read([B)I;");
        specs[3] = ResourceManagerSpi.getByFullSignature(b.toString()+"read([BII)I;");
    
    }

    /**
     * @see java.io.InputStream#available()
     */
    @Override
    public int available() throws IOException
    {
        return is.available();
    }

    /**
     * @see java.io.InputStream#mark(int)
     */
    @Override
    public synchronized void mark(int readlimit)
    {
        is.mark(readlimit);
    }

    /**
     * @see java.io.InputStream#markSupported()
     */
    @Override
    public boolean markSupported()
    {
        return is.markSupported();
    }

    /**
     * @see java.io.InputStream#reset()
     */
    @Override
    public synchronized void reset() throws IOException
    {
        is.reset();
    }

    /**
     * @see java.io.InputStream#skip(long)
     */
    @Override
    public long skip(long n) throws IOException
    {
        return is.skip(n);
    }

    /**
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException
    {
        Object ref = null;
        int rv = 0;
        Exception ee = null;
        try
        {
            ref = EventHandler.begin(specs[0].getId(), null, this.owner.get());
            //long n = System.nanoTime();
            rv = ((InputStream) this.is).read();
            //System.err.println("read: "+(System.nanoTime() - n));
        }
        catch (IOException e)
        {
            ee = e;
            throw e;
        }
        catch (Throwable e)
        {
        	IOException newe = new IOException(e);
            ee = newe;
            throw newe;
        }
        finally
        {
            try
            {
                rv = EventHandler.end(ref, ee, rv);
            }
            catch (Throwable e)
            {
                if (ee == null)
                {
                    if (e instanceof IOException)
                    {
                        throw (IOException)e;
                    }
                    else
                    {
                       log.logError("Unexpected exception", e);
                    }
                }
            }
        }
        return rv;
    }


    /**
     * @see java.io.InputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        Object ref = null;
        Exception ee = null;
        try
        {
            ref = EventHandler.begin(specs[1].getId(), null, this.owner.get());
            ((InputStream) this.is).close();
        }
        catch (IOException e)
        {
            ee = e;
            throw e;
        }
        catch (Throwable e)
        {
        	IOException newe = new IOException(e);
            ee = newe;
            throw newe;
        }
        finally
        {
            try
            {
                EventHandler.end(ref, ee);
            }
            catch (Throwable e)
            {
                if (ee == null)
                {
                    if (e instanceof IOException)
                    {
                        throw (IOException)e;
                    }
                    else
                    {
                       log.logError("Unexpected exception", e);
                    }
                }
            }
        }
    }



    /**
     * @see java.io.InputStream#read(byte[], int, int)
     */
    @SuppressWarnings("finally")
    @Override
    public int read(byte[] b, int n, int len) throws IOException
    {
        Object ref = null;
        int rv = 0;
        Exception ee = null;
        try
        {
            Object arr = Array.newInstance(Object.class, 3);
            ArrayUtil.set(arr,  0, b);
            ArrayUtil.set(arr,  1, n);
            ArrayUtil.set(arr,  2, len);
            
            ref = EventHandler.begin(specs[3].getId(), arr, this.owner.get());
            
            
            b =  (byte[])EventHandler.handleParameter(ref, 0, b);
            n = EventHandler.handleParameter(ref, 1, n);
            len = EventHandler.handleParameter(ref, 2, len);
            
            
            rv = ((InputStream) this.is).read(b, n, len);
        }
        catch (IOException e)
        {
            ee = e;
            throw e;
        }
        catch (Throwable e)
        {
        	IOException newe = new IOException(e);
            ee = newe;
            throw newe;
        }
        finally
        {
            try
            {
                rv = EventHandler.end(ref, ee, rv);
            }
            catch (Throwable e)
            {
                if (ee == null)
                {
                    if (e instanceof IOException)
                    {
                        throw (IOException)e;
                    }
                    else
                    {
                       log.logError("Unexpected exception", e);
                    }
                }
            }
        }
        return rv;
    }

    /**
     * @see java.io.InputStream#read(byte[])
     */
    @SuppressWarnings("finally")
    @Override
    public int read(byte[] b) throws IOException
    {
        Object ref = null;
        int rv = 0;
        Exception ee = null;
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, b);
            
            ref = EventHandler.begin(specs[2].getId(), arr, this.owner.get());
            
            b = (byte[])EventHandler.handleParameter(ref, 0, b);
            
            rv = ((InputStream) this.is).read(b);
        }
        catch (IOException e)
        {
            ee = e;
            throw e;
        }
        catch (Throwable e)
        {
        	IOException newe = new IOException(e);
            ee = newe;
            throw newe;
        }
        finally
        {
            try
            {
                rv = EventHandler.end(ref, ee, rv);
            }
            catch (Throwable e)
            {
                if (ee == null)
                {
                    if (e instanceof IOException)
                    {
                        throw (IOException)e;
                    }
                    else
                    {
                       log.logError("Unexpected exception", e);
                    }
                }
            }
        }
        return rv;
    }
    
    protected Object __data__ = null;
	@Override
	public Object getData() 
	{
		return __data__;
	}

	@Override
	public void setData(Object data) 
	{
		__data__ = data;
	}
}
