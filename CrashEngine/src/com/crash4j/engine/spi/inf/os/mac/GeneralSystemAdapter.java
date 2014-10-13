/**
 * 
 */
package com.crash4j.engine.spi.inf.os.mac;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.crash4j.engine.spi.inf.Filesystem;
import com.crash4j.engine.spi.inf.SystemAdapter;

/**
 * @author <MM>
 *
 */
public class GeneralSystemAdapter implements SystemAdapter
{
	/**
	 * Get root directories
	 * @param fs
	 * @return
	 * @throws IOException
	 */
	protected String getRootPath(FileStore fs) throws IOException 
	{
        IOException ex = null;
        for (Path p : FileSystems.getDefault().getRootDirectories()) 
        {
            try 
            {
                if (Files.getFileStore(p).equals(fs)) 
                {
                    return p.toString();
                }
            } 
            catch (IOException e) 
            {
                ex = e;
            }
        }
        
        if (ex != null) 
        {
            throw ex;
        }
        return null;
	}
	
	
	/**
     * @see com.crash4j.engine.spi.spi.inf.SystemAdapter#getFilesystems()
     */
    @Override
    public Filesystem[] getFilesystems() throws IOException
    {
    	ArrayList<Filesystem> fsys = new ArrayList<Filesystem>();
    	FileSystem fss = FileSystems.getDefault();
    	for (FileStore store : fss.getFileStores()) 
    	{
    		String root = getRootPath(store);
    		if (root != null)
    		{
    			fsys.add(new Filesystem(store.name(), 
    					store.getUsableSpace(), 
    					store.getUnallocatedSpace(), 
    					root));
    		}
		}
    	return fsys.toArray(new Filesystem[0]);
    }

    @Override
    public InetAddress[] traceroute(NetworkInterface inf, InetAddress to) throws IOException
    {
    	return null;
    }

    @Override
    public double ping(InetAddress e) throws IOException
    {
    	return 0;
    }
}
