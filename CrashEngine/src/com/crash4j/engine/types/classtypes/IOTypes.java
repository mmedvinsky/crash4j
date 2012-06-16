/**
 * @copyright
 */
package com.crash4j.engine.types.classtypes;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * The {@link IOTypes} are classifications of messages that actual programs will raise during the execution.
 * The messages are method calls of classes and/ro interfaces within the JVM. For example </p>
 * {@link FileOutputStream#write(byte[])}, {@link FileOutputStream#write(byte)}, {@link FileOutputStream#write(byte[], int, int)}, {@link FileChannel#write(java.nio.ByteBuffer)}, 
 * {@link FileChannel#write(java.nio.ByteBuffer[])}, {@link FileChannel#write(java.nio.ByteBuffer[], int, int)}, performed on the same {@link File} object will result into a  
 * {@link IOTypes#READ} {@link IOTypes} related to the same <b>fsys:resource=/a/c/x/f/a.txt, mt=/dev/dev1</b> resource instance.
 * <p/>
 * The following actions are currently supported by the system:</br>
 * 
 * <pre>
 * (i) initialization action. i.e. java.io.File constructor.
 * (o) open connection to the resource, i.e. Socket.connect, FileInputStream() constructor, ...
 * (c) close connection to the resource  
 * (s) shutdown the resource operation if it is running
 * (d) destroy the resource, i.e. file delete.
 * (n) create new resource, i.e. File.createTempFile 
 * (m) move/rename/modify resource
 * (r) read from the resource
 * (w) write to the resource
 * (k) get resource information
 * (t) end-point is performing a test 
 * (u) user-defined action, to be specified with attributes or other means
 * (x) call procedure
 * </pre>
 * 
 * <table>
 * <tr>
 * </tr>
 * </table>
 * 
 * @author <crash4j team>
 * 
 */
public enum IOTypes
{
    INIT,  
    BIND,  
    OPEN, 
    CLOSE, 
    DESTROY, 
    READ, 
    WRITE, 
    CHECK;
    
    /** mul^2 mask supports combinations*/
    private int mask = 0;
    
    /**
     * @param action
     */
    private IOTypes()
    {
        this.mask = (int)Math.pow(2, (this.ordinal()+1));
    }
    public static DBTypes fromString(String s)
    {
    	return DBTypes.valueOf(s.toUpperCase());
    }
   
    public int toInt()
    {
        return this.ordinal();
    }
    
    public int toMask()
    {
        return mask;
    }
}
