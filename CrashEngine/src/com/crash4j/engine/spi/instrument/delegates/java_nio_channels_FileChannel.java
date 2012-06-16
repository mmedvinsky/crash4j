/**
 * 
 */
package com.crash4j.engine.spi.instrument.delegates;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceAware;
import com.crash4j.engine.spi.util.ArrayUtil;





/**
 * @author <MM>
 *
 */
public class java_nio_channels_FileChannel extends FileChannel implements ResourceAware
{
    protected FileChannel fc = null;
    protected static Method implCloseChannelMethod = null;
    protected ResourceSpec spec = null;
    protected WeakReference<Object> owner = null;
    
    static 
    {
        try
        {
            implCloseChannelMethod = FileChannel.class.getDeclaredMethod("implCloseChannel", (Class[])null);
            implCloseChannelMethod.setAccessible(true);
        } 
        catch (Exception e) { }        
    }
    
    protected static ResourceSpec java_io_FileInputStream_java_nio_channels_FileChannel_force1 = ResourceManagerSpi.getByFullSignature("java.io.FileInputStream!java.nio.channels.FileChannel#force(Z)V");
    protected static ResourceSpec java_io_FileInputStream_java_nio_channels_FileChannel_map1 = ResourceManagerSpi.getByFullSignature("java.io.FileInputStream!java.nio.channels.FileChannel#map(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;");
    protected static ResourceSpec java_io_FileInputStream_java_nio_channels_FileChannel_read1 = ResourceManagerSpi.getByFullSignature("java.io.FileInputStream!java.nio.channels.FileChannel#read(Ljava/nio/ByteBuffer;)I");
    protected static ResourceSpec java_io_FileInputStream_java_nio_channels_FileChannel_read2 = ResourceManagerSpi.getByFullSignature("java.io.FileInputStream!java.nio.channels.FileChannel#read(Ljava/nio/ByteBuffer;J)I");
    protected static ResourceSpec java_io_FileInputStream_java_nio_channels_FileChannel_read3 = ResourceManagerSpi.getByFullSignature("java.io.FileInputStream!java.nio.channels.FileChannel#read([Ljava/nio/ByteBuffer;II)J");
    protected static ResourceSpec java_io_FileInputStream_java_nio_channels_FileChannel_truncate1 = ResourceManagerSpi.getByFullSignature("java.io.FileInputStream!java.nio.channels.FileChannel#truncate(J)Ljava/nio/channels/FileChannel;");
    protected static ResourceSpec java_io_FileInputStream_java_nio_channels_FileChannel_write1 = ResourceManagerSpi.getByFullSignature("java.io.FileInputStream!java.nio.channels.FileChannel#write(Ljava/nio/ByteBuffer;)I");
    protected static ResourceSpec java_io_FileInputStream_java_nio_channels_FileChannel_write2 = ResourceManagerSpi.getByFullSignature("java.io.FileInputStream!java.nio.channels.FileChannel#write(Ljava/nio/ByteBuffer;J)I");
    protected static ResourceSpec java_io_FileInputStream_java_nio_channels_FileChannel_write3 = ResourceManagerSpi.getByFullSignature("java.io.FileInputStream!java.nio.channels.FileChannel#write([Ljava/nio/ByteBuffer;II)J");

    protected static ResourceSpec java_io_FileOutputStream_java_nio_channels_FileChannel_force1 = ResourceManagerSpi.getByFullSignature("java.io.FileOutputStream!java.nio.channels.FileChannel#force(Z)V");
    protected static ResourceSpec java_io_FileOutputStream_java_nio_channels_FileChannel_map1 = ResourceManagerSpi.getByFullSignature("java.io.FileOutputStream!java.nio.channels.FileChannel#map(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;");
    protected static ResourceSpec java_io_FileOutputStream_java_nio_channels_FileChannel_read1 = ResourceManagerSpi.getByFullSignature("java.io.FileOutputStream!java.nio.channels.FileChannel#read(Ljava/nio/ByteBuffer;)I");
    protected static ResourceSpec java_io_FileOutputStream_java_nio_channels_FileChannel_read2 = ResourceManagerSpi.getByFullSignature("java.io.FileOutputStream!java.nio.channels.FileChannel#read(Ljava/nio/ByteBuffer;J)I");
    protected static ResourceSpec java_io_FileOutputStream_java_nio_channels_FileChannel_read3 = ResourceManagerSpi.getByFullSignature("java.io.FileOutputStream!java.nio.channels.FileChannel#read([Ljava/nio/ByteBuffer;II)J");
    protected static ResourceSpec java_io_FileOutputStream_java_nio_channels_FileChannel_truncate1 = ResourceManagerSpi.getByFullSignature("java.io.FileOutputStream!java.nio.channels.FileChannel#truncate(J)Ljava/nio/channels/FileChannel;");
    protected static ResourceSpec java_io_FileOutputStream_java_nio_channels_FileChannel_write1 = ResourceManagerSpi.getByFullSignature("java.io.FileOutputStream!java.nio.channels.FileChannel#write(Ljava/nio/ByteBuffer;)I");
    protected static ResourceSpec java_io_FileOutputStream_java_nio_channels_FileChannel_write2 = ResourceManagerSpi.getByFullSignature("java.io.FileOutputStream!java.nio.channels.FileChannel#write(Ljava/nio/ByteBuffer;J)I");
    protected static ResourceSpec java_io_FileOutputStream_java_nio_channels_FileChannel_write3 = ResourceManagerSpi.getByFullSignature("java.io.FileOutputStream!java.nio.channels.FileChannel#write([Ljava/nio/ByteBuffer;II)J");

    protected ResourceSpi resource;

    
    public java_nio_channels_FileChannel(ResourceSpec spec, ResourceSpi res, Object arg, Object owner, Object rv)
    {
        super();
        fc = (FileChannel)rv;
        this.spec = spec;
        this.owner = new WeakReference<Object>(owner);
        this.resource = res;

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
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        return fc.equals(obj);
    }

    /**
     * @param metaData
     * @throws IOException
     * @see java.nio.channels.FileChannel#force(boolean)
     */
    public void force(boolean metaData) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        
        Object ow = this.owner.get();
        if (ow == null)
        {
            fc.force(metaData);
        }
        
        ResourceSpec sp = java_io_FileOutputStream_java_nio_channels_FileChannel_force1;
        if (ow instanceof InputStream)
        {
            sp = java_io_FileInputStream_java_nio_channels_FileChannel_force1;
        }
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, metaData);            
            ref = EventHandler.begin(sp.getId(), arr, ow);
            metaData = EventHandler.handleParameter(ref, 0, metaData);
            fc.force(metaData);
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
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return fc.hashCode();
    }

    /**
     * @param position
     * @param size
     * @param shared
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#lock(long, long, boolean)
     */
    public FileLock lock(long position, long size, boolean shared) throws IOException
    {
        return fc.lock(position, size, shared);
    }

    /**
     * @param mode
     * @param position
     * @param size
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#map(java.nio.channels.FileChannel.MapMode, long, long)
     */
    @SuppressWarnings("finally")
    public MappedByteBuffer map(MapMode mode, long position, long size) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        MappedByteBuffer rv = null;
        
        Object ow = this.owner.get();
        if (ow == null)
        {
            return fc.map(mode, position, size);
        }
        
        ResourceSpec sp = java_io_FileOutputStream_java_nio_channels_FileChannel_map1;
        if (ow instanceof InputStream)
        {
            sp = java_io_FileInputStream_java_nio_channels_FileChannel_map1;
        }
         
        try
        {
            Object arr = Array.newInstance(Object.class, 3);
            ArrayUtil.set(arr,  0, mode);            
            ArrayUtil.set(arr,  1, position);            
            ArrayUtil.set(arr,  2, size);            
            ref = EventHandler.begin(sp.getId(), arr, ow);
            mode = (MapMode)EventHandler.handleParameter(ref, 0, mode);
            position = EventHandler.handleParameter(ref, 1, position);
            size = EventHandler.handleParameter(ref, 2, size);
            fc.map(mode, position, size);
        }
        catch (IOException e)
        {
            ee = e;
            throw e;
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
            return rv;
        }
    }

    /**
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#position()
     */
    public long position() throws IOException
    {
        return fc.position();
    }

    /**
     * @param newPosition
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#position(long)
     */
    public java.nio.channels.FileChannel position(long newPosition) throws IOException
    {
        return fc.position(newPosition);
    }

    /**
     * @param dst
     * @param position
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#read(java.nio.ByteBuffer, long)
     */
    @SuppressWarnings("finally")
    public int read(ByteBuffer dst, long position) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        int rv = 0;
        
        Object ow = this.owner.get();
        if (ow == null)
        {
            return fc.read(dst, position);
        }
        
        ResourceSpec sp = java_io_FileOutputStream_java_nio_channels_FileChannel_read2;
        if (ow instanceof InputStream)
        {
            sp = java_io_FileInputStream_java_nio_channels_FileChannel_read2;
        }
         
        try
        {
            Object arr = Array.newInstance(Object.class, 2);
            ArrayUtil.set(arr,  0, dst);            
            ArrayUtil.set(arr,  1, position);            
            ref = EventHandler.begin(sp.getId(), arr, ow);
            dst = (ByteBuffer)EventHandler.handleParameter(ref, 0, dst);
            position = EventHandler.handleParameter(ref, 1, position);
            rv = fc.read(dst, position);
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
                }
            }
        }
        return rv;
    }

    /**
     * @param dst
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#read(java.nio.ByteBuffer)
     */
    public int read(ByteBuffer dst) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        int rv = 0;
        
        Object ow = this.owner.get();
        if (ow == null)
        {
            return fc.read(dst);
        }
        
        ResourceSpec sp = java_io_FileOutputStream_java_nio_channels_FileChannel_read1;
        if (ow instanceof InputStream)
        {
            sp = java_io_FileInputStream_java_nio_channels_FileChannel_read1;
        }
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, dst);            
            ref = EventHandler.begin(sp.getId(), arr, ow);
            dst = (ByteBuffer)EventHandler.handleParameter(ref, 0, dst);
            rv = fc.read(dst);
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
                }
            }
        }
        return rv;
    }

    /**
     * @param dsts
     * @param offset
     * @param length
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#read(java.nio.ByteBuffer[], int, int)
     */
    @SuppressWarnings("finally")
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        long rv = 0;
        
        Object ow = this.owner.get();
        if (ow == null)
        {
            return fc.read(dsts, offset, length);
        }
        
        ResourceSpec sp = java_io_FileOutputStream_java_nio_channels_FileChannel_read3;
        if (ow instanceof InputStream)
        {
            sp = java_io_FileInputStream_java_nio_channels_FileChannel_read3;
        }
         
        try
        {
            Object arr = Array.newInstance(Object.class, 3);
            ArrayUtil.set(arr,  0, dsts);            
            ArrayUtil.set(arr,  1, offset);            
            ArrayUtil.set(arr,  2, length);            
            ref = EventHandler.begin(sp.getId(), arr, ow);
            dsts = (ByteBuffer[])EventHandler.handleParameter(ref, 0, dsts);
            offset = EventHandler.handleParameter(ref, 1, offset);
            length= EventHandler.handleParameter(ref, 2, length);
            rv = fc.read(dsts, offset, length);
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
                }
            }
        }
        return rv;
    }

    /**
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#size()
     */
    public long size() throws IOException
    {
        return fc.size();
    }

    /**
     * @return
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return fc.toString();
    }

    /**
     * @param src
     * @param position
     * @param count
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#transferFrom(java.nio.channels.ReadableByteChannel, long, long)
     */
    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException
    {
        return fc.transferFrom(src, position, count);
    }

    /**
     * @param position
     * @param count
     * @param target
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#transferTo(long, long, java.nio.channels.WritableByteChannel)
     */
    public long transferTo(long position, long count, WritableByteChannel target) throws IOException
    {
        return fc.transferTo(position, count, target);
    }

    /**
     * @param size
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#truncate(long)
     */
    @SuppressWarnings("finally")
    public java.nio.channels.FileChannel truncate(long size) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        FileChannel rv = null;
        
        Object ow = this.owner.get();
        if (ow == null)
        {
            return fc.truncate(size);
        }
        
        ResourceSpec sp = java_io_FileOutputStream_java_nio_channels_FileChannel_truncate1;
        if (ow instanceof InputStream)
        {
            sp = java_io_FileInputStream_java_nio_channels_FileChannel_truncate1;
        }
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, size);                       
            ref = EventHandler.begin(sp.getId(), arr, ow);
            size = EventHandler.handleParameter(ref, 0, size);
            rv = fc.truncate(size);
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
                rv = (FileChannel)EventHandler.end(ref, ee, rv);
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
        return rv;
    }

    /**
     * @param position
     * @param size
     * @param shared
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#tryLock(long, long, boolean)
     */
    public FileLock tryLock(long position, long size, boolean shared) throws IOException
    {
        return fc.tryLock(position, size, shared);
    }

    /**
     * @param src
     * @param position
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#write(java.nio.ByteBuffer, long)
     */
    @SuppressWarnings("finally")
    public int write(ByteBuffer src, long position) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        int rv = 0;
        
        Object ow = this.owner.get();
        if (ow == null)
        {
            return fc.write(src, position);
        }
        
        ResourceSpec sp = java_io_FileOutputStream_java_nio_channels_FileChannel_write2;
        if (ow instanceof InputStream)
        {
            sp = java_io_FileInputStream_java_nio_channels_FileChannel_write2;
        }
         
        try
        {
            Object arr = Array.newInstance(Object.class, 2);
            ArrayUtil.set(arr,  0, src);                       
            ArrayUtil.set(arr,  1, position);                       
            ref = EventHandler.begin(sp.getId(), arr, ow);
            src = (ByteBuffer)EventHandler.handleParameter(ref, 0, src);
            position = EventHandler.handleParameter(ref, 1, position);
            rv = fc.write(src, position);
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
                }
            }
        }
        return rv;
    }

    /**
     * @param src
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#write(java.nio.ByteBuffer)
     */
    @SuppressWarnings("finally")
    public int write(ByteBuffer src) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        int rv = 0;
        
        Object ow = this.owner.get();
        if (ow == null)
        {
            return fc.write(src);
        }
        
        ResourceSpec sp = java_io_FileOutputStream_java_nio_channels_FileChannel_write1;
        if (ow instanceof InputStream)
        {
            sp = java_io_FileInputStream_java_nio_channels_FileChannel_write1;
        }
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, src);                       
            ref = EventHandler.begin(sp.getId(), arr, ow);
            src = (ByteBuffer)EventHandler.handleParameter(ref, 0, src);
            rv = fc.write(src);
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
                }
            }
        }
        return rv;
    }

    /**
     * @param srcs
     * @param offset
     * @param length
     * @return
     * @throws IOException
     * @see java.nio.channels.FileChannel#write(java.nio.ByteBuffer[], int, int)
     */
    @SuppressWarnings("finally")
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        long rv = 0;
        
        Object ow = this.owner.get();
        if (ow == null)
        {
            return fc.write(srcs, offset, length);
        }
        
        ResourceSpec sp = java_io_FileOutputStream_java_nio_channels_FileChannel_write3;
        if (ow instanceof InputStream)
        {
            sp = java_io_FileInputStream_java_nio_channels_FileChannel_write3;
        }
         
        try
        {
            Object arr = Array.newInstance(Object.class, 3);
            ArrayUtil.set(arr,  0, srcs);                       
            ArrayUtil.set(arr,  0, offset);                       
            ArrayUtil.set(arr,  0, length);                       
            ref = EventHandler.begin(sp.getId(), arr, ow);
            srcs = (ByteBuffer[])EventHandler.handleParameter(ref, 0, srcs);
            offset = EventHandler.handleParameter(ref, 1, offset);
            length = EventHandler.handleParameter(ref, 2, length);
            rv = fc.write(srcs, offset, length);
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
                }
            }
        }
        return rv;
    }

    
    @Override
    protected void implCloseChannel() throws IOException
    {
        try
        {
            if (implCloseChannelMethod != null)
            {
                implCloseChannelMethod.invoke(fc, (Object[])null);
            }
        } 
        catch (Exception e)
        {
        }
    }
}
