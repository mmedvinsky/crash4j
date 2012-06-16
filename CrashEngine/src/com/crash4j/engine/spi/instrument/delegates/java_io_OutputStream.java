/**
 * 
 */
package com.crash4j.engine.spi.instrument.delegates;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceAware;
import com.crash4j.engine.spi.util.ArrayUtil;

/**
 * Delegate method used to wrap {@link OutputStream} instances when needed
 * 
 * @author <MM>
 *
 */
public class java_io_OutputStream extends OutputStream implements ResourceAware
{
    protected OutputStream os = null;
    protected WeakReference<Object> owner = null;
    //9 methods in order
    protected ResourceSpec specs[] = new ResourceSpec[4];
    protected ResourceSpi resource;
   
    /**
     * Delegating constructor
     */
    public java_io_OutputStream(Object os, ResourceSpec spec, Object owner)
    {
        this.os = (OutputStream)os;
        this.owner = new WeakReference<Object>(owner);
        
        //Prepare all specifications for this stub.
        //java.net.Socket!java.io.InputStream#read()I;=4
        StringBuffer b = new StringBuffer(spec.getEntityName());
        b.append("!").append("java.io.OutputStream").append("#");
        
        specs[0] = ResourceManagerSpi.getByFullSignature(b.toString()+"write(I)V;");
        specs[1] = ResourceManagerSpi.getByFullSignature(b.toString()+"close()V;");
        specs[2] = ResourceManagerSpi.getByFullSignature(b.toString()+"write([B)V;");
        specs[3] = ResourceManagerSpi.getByFullSignature(b.toString()+"write([BII)V;");
    
    }

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
        // TODO Auto-generated method stub
        return os.equals(obj);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        // TODO Auto-generated method stub
        return os.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return os.toString();
    }

    /**
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException
    {
        os.flush();
    }

    /**
     * @see java.io.OutputStream#write()
     */
    @Override
    public void write(int b) throws IOException
    {
        Object ref = null;
        Exception ee = null;

        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, b);
            ref = EventHandler.begin(specs[0].getId(), arr, this.owner.get());
            EventHandler.handleParameter(ref, 0, b);
            ((OutputStream) this.os).write(b);
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
                }
            }
        }
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
            ((OutputStream) this.os).close();
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
                }
            }
        }
    }



    /**
     * @see java.io.InputStream#read(byte[], int, int)
     */
    @Override
    public void write(byte[] b, int n, int len) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        try
        {
            Object arr = Array.newInstance(Object.class, 3);
            ArrayUtil.set(arr,  0, b);
            ArrayUtil.set(arr,  1, n);
            ArrayUtil.set(arr,  2, len);
            
            ref = EventHandler.begin(specs[3].getId(), arr, this.owner.get());
            b = (byte[])EventHandler.handleParameter(ref, 0, b);
            n = EventHandler.handleParameter(ref, 1, n);
            len = EventHandler.handleParameter(ref, 2, len);
            ((OutputStream) this.os).write(b, n, len);
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
                }
            }
        }
    }

    /**
     * @see java.io.InputStream#read(byte[])
     */
    @Override
    public void write(byte[] b) throws IOException
    {
        Object ref = null;
        Exception ee = null;        
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, b);
            ref = EventHandler.begin(specs[2].getId(), arr, this.owner.get());
            b = (byte[])EventHandler.handleParameter(ref, 0, b);
            ((OutputStream) this.os).write(b);
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
                }
            }
        }
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
