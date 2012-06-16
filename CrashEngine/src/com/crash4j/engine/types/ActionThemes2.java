/**
 * @copyright
 */
package com.crash4j.engine.types;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * The {@link ActionThemes2} are classifications of messages that actual programs will raise during the execution.
 * The messages are method calls of classes and/ro interfaces within the JVM. For example </p>
 * {@link FileOutputStream#write(byte[])}, {@link FileOutputStream#write(byte)}, {@link FileOutputStream#write(byte[], int, int)}, {@link FileChannel#write(java.nio.ByteBuffer)}, 
 * {@link FileChannel#write(java.nio.ByteBuffer[])}, {@link FileChannel#write(java.nio.ByteBuffer[], int, int)}, performed on the same {@link File} object will result into a  
 * {@link ActionThemes2#READ} {@link ActionThemes2} related to the same <b>fsys:resource=/a/c/x/f/a.txt, mt=/dev/dev1</b> resource instance.
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
public enum ActionThemes2
{
    INIT("i", 0, 2),  
    OPEN("o", 1, 4), 
    CLOSE("c", 2, 8), 
    SHUTDOWN("s", 3, 16), 
    DESTROY("d", 4, 32), 
    NEW("n", 5, 64), 
    MODIFY("m", 6, 128), 
    READ("r", 7, 256), 
    WRITE("w", 8, 512), 
    CHECK("k", 9, 1024),
    TEST("t", 10, 2048),
    USER("u", 11, 4096),
    QUERY("q", 12, 8192),
    CALL("x", 13, 16384);
    
    /** String representation of this action */
    private String action = null;
    /** id or index of this action*/
    private int id = 0;
    /** mul^2 mask supports combinations*/
    private int mask = 0;
    
    /**
     * @param action
     */
    private ActionThemes2(String action, int id, int mask)
    {
        this.action = action;
        this.id = id;
        this.mask = mask;
    }
    
    public static ActionThemes2 fromString(String s)
    {
        return fromString(s.charAt(0));
    }
    
    public int toInt()
    {
        return id;
    }
    
    public int toMask()
    {
        return mask;
    }
    
    /**
     * Convert to {@link ActionThemes2} from string
     * @param a
     * @return
     */
    public static ActionThemes2 fromString(char a)
    {
        switch (a)
        {
        case 'i' : return INIT;
        case 'o' : return OPEN;
        case 'c' : return CLOSE;
        case 's' : return SHUTDOWN;
        case 'd' : return DESTROY;
        case 'n' : return NEW;
        case 'm' : return MODIFY;
        case 'r' : return READ;
        case 'w' : return WRITE;
        case 'k' : return CHECK;
        case 't' : return TEST;
        case 'u' : return USER;
        case 'x' : return CALL;
        default : throw new RuntimeException("Unknwon action");
        }
    }
    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return this.action;
    }
}
