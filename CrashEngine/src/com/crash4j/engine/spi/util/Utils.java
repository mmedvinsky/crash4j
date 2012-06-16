/**
 * 
 */
package com.crash4j.engine.spi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.Formatter;
import java.util.Locale;

import com.crash4j.engine.spi.ResourceSpec;

/**
 * @author <MM>
 * 
 */
public class Utils
{

    static Class objectWrapper = null;
    static Constructor objectWrapper_c = null;
    static Method objectWrapper_staticHash = null;

    // TODO Make this pretty!!!!!!!
    public static String buildClassNameFromSpec(String caller, ResourceSpec spec)
    {
        String key = spec.getKey();
        String rk1 = key.replace("(", "_").replace(")", "_").replace("#", "_").replace("[", "_").replace(".", "_").replace("/", "_").replace(";", "_")
                .replace("<", "_").replace(">", "_").replace("!", "_");

        return caller.replace('.', '_') + "_" + rk1;
    }

    /**
     * Used to initilizes generated classes for utilities.
     */
    public static void initClassDiscovery()
    {
        try
        {
            objectWrapper = Class.forName("com_crash4j_engine_ObjectWrapper");
            objectWrapper_c = objectWrapper.getConstructor(Object.class);
            objectWrapper_staticHash = objectWrapper.getMethod("getBaseHashCode", Object.class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Find resource....
     * 
     * @param base
     * @param resource
     * @return
     * @throws URISyntaxException
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getResourceAsStream(Class base, String resource) throws URISyntaxException, MalformedURLException, IOException
    {
        InputStream iis = null;
        URI u = new URI(resource);
        if (u.getScheme().startsWith("http"))
        {
            URLConnection c = u.toURL().openConnection();
            iis = c.getInputStream();
        } else if (u.getScheme().startsWith("file"))
        {
            URLConnection c = u.toURL().openConnection();
            iis = c.getInputStream();
        } else if (u.getScheme().startsWith("classpath"))
        {
            String path = u.getPath();
            iis = base.getResourceAsStream(path);
            if (iis == null)
            {
                iis = ClassLoader.getSystemResourceAsStream(path);
                if (iis == null)
                {
                    iis = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                }
            }
        }
        return iis;
    }

    
    /**
     * Find resource....
     * 
     * @param base
     * @param resource
     * @return
     * @throws URISyntaxException
     * @throws MalformedURLException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static Class<?> findClass(Class<?> base, String klass) throws ClassNotFoundException
    {
        Class<?> c = Class.forName(klass);
        if (c == null)
        {
            if (base.getClassLoader() != null)
            {
                c = base.getClassLoader().loadClass(klass);
            }
            
            if (c == null)
            {
                if (ClassLoader.getSystemClassLoader() != null)
                {
                    c = ClassLoader.getSystemClassLoader().loadClass(klass);
                }
                
                if (c == null)
                {
                    c = Thread.currentThread().getContextClassLoader().loadClass(klass);
                }
            }
        }
        return c;
    }
    
    /**
     * Wrap {@link Object} into ObjectWrapper
     * 
     * @param o
     * @return
     */
    public static Object wrapObject(Object o)
    {
        try
        {
            return objectWrapper_c.newInstance(o);
        } catch (SecurityException e)
        {
            e.printStackTrace(System.err);
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace(System.err);
        } catch (InstantiationException e)
        {
            e.printStackTrace(System.err);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace(System.err);
        } catch (InvocationTargetException e)
        {
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Wrap {@link Object} into ObjectWrapper
     * 
     * @param o
     * @return
     */
    public static int getBaseHashCode(Object o)
    {
        try
        {
            Integer h = (Integer) objectWrapper_staticHash.invoke(null, o);
            return h.intValue();
        } catch (SecurityException e)
        {
            e.printStackTrace(System.err);
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace(System.err);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace(System.err);
        } catch (InvocationTargetException e)
        {
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * @param x
     * @return next power of 2 number
     */
    public static int netPW2(int x)
    {
        --x;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return ++x;
    }

    
    /**
     *  Format the double precision number for output
     */
    public static String formatDouble(double d, Locale loc)
    {
    	   StringBuilder sb = new StringBuilder();
    	   // Send all output to the Appendable object sb
    	   Formatter formatter = new Formatter(sb, loc);

    	   // Explicit argument indices may be used to re-order output.
    	   formatter.format("%.3f", d);
    	   return sb.toString();
    }
    
    /**
     * Wrap {@link Object} into ObjectWrapper id
     * 
     * @param o
     * @return
     */
    public static String wrappedObjectId(Object w, Object instance)
    {
        StringBuilder buf = new StringBuilder(instance.getClass().getName());
        buf.append("@").append(w.hashCode());
        return buf.toString();
    }

    /**
     * Reade the input stream into the {@link String}
     * 
     * @param s
     * @param iis
     * @throws IOException
     */
    public static void readStream(StringBuilder s, InputStream iis) throws IOException
    {
        char buf[] = new char[1024];
        InputStreamReader ire = new InputStreamReader(iis);
        int n = 0;
        while ((n = ire.read(buf)) != -1)
        {
            s.append(buf, 0, n);
        }
    }
}
