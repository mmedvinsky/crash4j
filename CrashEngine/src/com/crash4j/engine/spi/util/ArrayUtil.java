/**
 * 
 */
package com.crash4j.engine.spi.util;

import java.lang.reflect.Array;

import com.crash4j.engine.spi.instrument.bcel.generic.Type;

/**
 * Collection of {@link Array} extensions that provide auto-boxing methods for all basic data types.
 */
public class ArrayUtil 
{
	/**
	 * @see Array#set(Object, int, Object)
	 */
	public static void set(Object arr, int index, Object v)
	{
		Array.set(arr, index, v);
	}


	/**
	 * @see Array#set(Object, int, int)
	 */
	public static void set(Object arr, int index, int v)
	{
		Array.set(arr, index, new Integer(v));
	}

	/**
	 * @see Array#set(Object, int, char)
	 */
	public static void set(Object arr, int index, char v)
	{
		Array.set(arr, index, new Character(v));
	}


	/**
	 * @see Array#set(Object, int, float)
	 */
	public static void set(Object arr, int index, float v)
	{
		Array.set(arr, index, new Float(v));
	}

	/**
	 * @see Array#set(Object, int, double)
	 */
	public static void set(Object arr, int index, double v)
	{
		Array.set(arr, index, new Double(v));
	}

	/**
	 * @see Array#set(Object, int, short)
	 */
	public static void set(Object arr, int index, short v)
	{
		Array.set(arr, index, new Short(v));
	}


	/**
	 * @see Array#set(Object, int, long)
	 */
	public static void set(Object arr, int index, long v)
	{
		Array.set(arr, index, new Long(v));
	}

	/**
	 * @see Array#set(Object, int, byte)
	 */
	public static void set(Object arr, int index, byte v)
	{
		Array.set(arr, index, new Byte(v));
	}
	/**
	 * @see Array#set(Object, int, boolean)
	 */
	public static void set(Object arr, int index, boolean v)
	{
		Array.set(arr, index, new Boolean(v));
	}	


    /**
     * @see Array#set(Object, int, Object)
     */
    public static Object get(Object arr, int index)
    {
        return Array.get(arr, index);
    }


    /**
     * @see Array#set(Object, int, int)
     */
    public static int getInt(Object arr, int index)
    {
        return (Integer)Array.get(arr, index);
    }

    /**
     * @see Array#getChar(Object, int, char)
     */
    public static char getChar(Object arr, int index)
    {
        return (Character)Array.get(arr, index);
    }


    /**
     * @see Array#getFloat(Object, int, float)
     */
    public static float getFloat(Object arr, int index)
    {
        return (Float)Array.get(arr, index);
    }

    /**
     * @see Array#getDouble(Object, int, double)
     */
    public static double getDouble(Object arr, int index)
    {
        return (Double)Array.get(arr, index);
    }

    /**
     * @see Array#getShort(Object, int, short)
     */
    public static short getShort(Object arr, int index)
    {
        return (Short)Array.get(arr, index);
    }


    /**
     * @see Array#getLong(Object, int, long)
     */
    public static long getLong(Object arr, int index)
    {
        return (Long)Array.get(arr, index);
    }

    /**
     * @see Array#getByte(Object, int, byte)
     */
    public static byte getByte(Object arr, int index)
    {
        return (Byte)Array.get(arr, index);
    }
    /**
     * @see Array#getBoolean(Object, int, boolean)
     */
    public static boolean getBoolean(Object arr, int index)
    {
        return (Boolean)Array.get(arr, index);
    }   


    /**
     * @see Array#set(Object, int, Object)
     */
    public static Object get(Object arr, int index, Object tt)
    {
        if (arr == null)
        {
            return null;
        }
        
        Type t = (Type)Type.getType(tt.toString());
        Object o = Array.get(arr, index);
        if (o == null)
        {
            return null;
        }
        
        if (t == Type.INT)
        {
            return ((Integer)o).intValue();
        }
        if (t == Type.FLOAT)
        {
            return ((Float)o).floatValue();
        }
        if (t == Type.BOOLEAN)
        {
            return ((Boolean)o).booleanValue();
        }
        if (t == Type.SHORT)
        {
            return ((Short)o).shortValue();
        }
        if (t == Type.CHAR)
        {
            return ((Character)o).charValue();
        }
        if (t == Type.BYTE)
        {
            return ((Byte)o).byteValue();
        }
        if (t == Type.DOUBLE)
        {
            return ((Double)o).doubleValue();
        }
        if (t == Type.LONG)
        {
            return ((Long)o).longValue();
        }
        
        return o;
        
    }

}
