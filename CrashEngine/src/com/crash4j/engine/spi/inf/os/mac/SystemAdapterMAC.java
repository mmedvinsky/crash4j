/**
 * 
 */
package com.crash4j.engine.spi.inf.os.mac;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crash4j.engine.spi.inf.Filesystem;
import com.crash4j.engine.spi.inf.SystemAdapter;
import com.crash4j.engine.spi.util.Utils;

/**
 * @author <MM>
 *
 */
public class SystemAdapterMAC implements SystemAdapter
{
    //
    protected Pattern dfp = Pattern.compile("([^    ]+)[ ]+([^ ]+)[ ]+([^ ]+)[ ]+([^ ]+)[ ]+([^ ]+)[ ]+([/].*)");
    protected Pattern trp = Pattern.compile("[(]([^)]+)[)]");
    protected Pattern pp = Pattern.compile("[ ]*([0-9]+).*?time[=]([0-9]+[.][0-9]*).*");
    
    /**
     * @see com.crash4j.engine.spi.spi.inf.SystemAdapter#getFilesystems()
     */
    @Override
    public Filesystem[] getFilesystems() throws IOException
    {
        ArrayList<Filesystem> fss = new ArrayList<Filesystem>();
        
        Process p = Runtime.getRuntime().exec("df -ml");
        InputStream iss = p.getInputStream();
        char buf[] = new char[1024];
        StringBuilder bl = new StringBuilder();
        Utils.readStream(bl, iss);
        
        int i = 0;
        String lines[] = bl.toString().split("\n");
        for (String l : lines)
        {
            if (i == 0)
            {
                //skip the title pages
                i++;
                continue;
            }
            //Create a matcher
            Matcher mch = dfp.matcher(l);
            if (mch.matches())
            {
                Filesystem fs = new Filesystem(mch.group(1), 
                        new Long(mch.group(3)), 
                        new Long(mch.group(4)), 
                        mch.group(6));
                fss.add(fs);
            }            
        }
        
        return fss.toArray(new Filesystem[0]);
    }

    @Override
    public InetAddress[] traceroute(NetworkInterface inf, InetAddress to) throws IOException
    {
        StringBuilder tr = new StringBuilder("traceroute");
        if (inf != null)
        {
            tr.append(" -i").append(inf.getDisplayName());
        }
        tr.append(" ").append(to.getHostName());
        
        Process p = Runtime.getRuntime().exec(tr.toString());
        InputStream iss = p.getInputStream();
        StringBuilder bl = new StringBuilder();
        Utils.readStream(bl, iss);
        
        ArrayList<InetAddress> ends = new ArrayList<InetAddress>();
        Matcher mch = trp.matcher(bl.toString());
        int c = mch.groupCount();
        while (mch.find())
        {
            InetAddress addr = InetAddress.getByName(mch.group(1));
            ends.add(addr);
        }            
        
        return ends.toArray(new InetAddress[0]);
    }

    @Override
    public double ping(InetAddress e) throws IOException
    {
        StringBuilder tr = new StringBuilder("ping -c 5");
        tr.append(" ").append(e.getHostName());
        
        Process p = Runtime.getRuntime().exec(tr.toString());
        InputStream iss = p.getInputStream();
        StringBuilder bl = new StringBuilder();
        Utils.readStream(bl, iss);
        
        ArrayList<InetAddress> ends = new ArrayList<InetAddress>();
        int i = 0;
        double av = 0;
        String lines[] = bl.toString().split("\n");
        for (String l : lines)
        {
            if (i == 0)
            {
                //skip the title pages
                i++;
                continue;
            }
            
            i++;;
            //Create a matcher
            Matcher mch = pp.matcher(l);
            if (mch.matches())
            {
                
                int bytes = new Integer(mch.group(1));
                double time = new Double(mch.group(2));
                av += bytes/time;
            }            
        }
        return av/i+1;
    }
}
