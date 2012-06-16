/**
 * @copyright
 */
package com.crash4j.engine.spi.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.crash4j.engine.spi.context.ThreadContext;

/**
 * {@link StatsJSONFileCollector} will collect the stats with specific frequency and will deposit them into a file specified by the user.
 * 
 * @author team
 *
 */
public class StatsJSONFileCollector extends JSONBufferedStatsCollector
{
    protected String output = null;
    protected String outputDir = null;
    protected Writer writer = null;
    int i = 0;
    
    /**
     * 
     */
    public StatsJSONFileCollector()
    {
    }
    
    
    
    
    /**
     * @see com.crash4j.engine.spi.json.JSONStatsCollector#start()
     */
    @Override
    public void start()
    {
        super.start();
        if (output == null)
        {
            output = "stats.json";
        }
        
        if (this.outputDir == null)
        {
            return;
        }
        
        File odir = new File(this.outputDir);
        if (!odir.exists())
        {
            odir.mkdir();
        }
        try
        {
            writer = new BufferedWriter(new FileWriter(new File(odir, this.output)));
            writer.write("[");
        }
        catch (Exception e)
        {
            log.logError("", e);            
        }
    }

    /**
     * @see com.crash4j.engine.spi.json.JSONStatsCollector#stop()
     */
    @Override
    public void stop()
    {
        super.stop();
        try
        {
            this.writer.write("{}]");
            this.writer.flush();
            this.writer.close();
        }
        catch (Exception e)
        {
            log.logError("", e);
        }
    }




    @Override
    protected void sendMessages(JSONArray arr) throws IOException
    {
        ThreadContext.beginIgnore();
        try
        {
            for (int i = 0; i < arr.length(); i++)
            {
                Object rec = arr.opt(i);
                writer.write(rec.toString());
                writer.write(",\n");
                writer.flush();
            }

        }
        finally
        {
            ThreadContext.endIgnore();
        }
    }

    /**
     * @return the output
     */
    public String getOutput()
    {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(String output)
    {
        this.output = output;
    }

    /**
     * @return the outputDir
     */
    public String getOutputDir()
    {
        return outputDir;
    }

    /**
     * @param outputDir the outputDir to set
     */
    public void setOutputDir(String outputDir)
    {
        this.outputDir = outputDir;
    }
}
