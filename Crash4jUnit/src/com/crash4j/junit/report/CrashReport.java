/**
 * 
 */
package com.crash4j.junit.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.crash4j.Simulation;
import com.crash4j.engine.client.json.JSONArray;
import com.crash4j.engine.client.json.JSONBinding;
import com.crash4j.engine.client.json.JSONException;
import com.crash4j.engine.client.json.JSONObject;

/**
 * Crash report builder will organize the data into a report structure.
 */
@XmlRootElement(name="CrashReport")
public class CrashReport 
{
    protected ArrayList<Simulation> sims = new ArrayList<Simulation>();
    protected int iterations = 0;
    protected int concrrency = 0;
    protected long timeout = 0;
    protected String outputDir;
    protected String outputFile;
    protected JSONObject reportData = null;

    protected static JSONBinding<CrashReport> bindSimRule = new JSONBinding<CrashReport>(CrashReport.class);
    
    
    protected static String []UIResources =  {
    	"/com/crash4j/junit/report/ui/js/jquery.jqplot.css",
    	"/com/crash4j/junit/report/ui/js/jquery.jqplot.min.js",    	
    	"/com/crash4j/junit/report/ui/js/jquery-ui.css",
    	"/com/crash4j/junit/report/ui/js/jquery-ui.min.js",    	
    	"/com/crash4j/junit/report/ui/js/jquery.min.js",
    	"/com/crash4j/junit/report/ui/js/jqplot.pieRenderer.min.js"
    };
    
    protected static String reportTemplate = null;
    
    static {
    	try
    	{
    		StringWriter sw = new StringWriter();
	    	InputStream iis = CrashReport.class.getResourceAsStream("/com/crash4j/junit/report/ui/reportTemplate.html");
	    	if (iis != null)
	    	{
	    		InputStreamReader isr = new InputStreamReader(iis);
	    		char buf[] = new char[1024];
	    		int c = 0;
	    		while ((c = isr.read(buf)) != -1)
	    		{
	    			sw.write(buf, 0, c);
	    		}
	    		reportTemplate = sw.toString();
	    	}
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    
    /**
     * Default constructor.
     */
    public CrashReport()
    {
    	
    }

    
	/**
	 * Generating constructor using fields.
	 * @param sims
	 * @param iterations
	 * @param concrrency
	 * @param timeout
	 * @param outputDir
	 * @param outputFile
	 */
	public CrashReport(Simulation[] sims, int iterations, int concrrency,
			long timeout, String outputDir, String outputFile) 
	{
		for (Simulation simulation : sims) 
		{
			this.sims.add(simulation);
		}
		this.iterations = iterations;
		this.concrrency = concrrency;
		this.timeout = timeout;
		this.outputDir = outputDir;
		this.outputFile = outputFile;
		
	}
	
	/**
	 * @return the sims
	 */
	@XmlElement(name="sims", type=Simulation.class)
	public ArrayList<Simulation> getSims() {
		return sims;
	}


	/**
	 * @param sims the sims to set
	 */
	public void setSims(ArrayList<Simulation> sims) {
		this.sims = sims;
	}


	/**
	 * @return the iterations
	 */
	@XmlAttribute(name="iterations")
	public int getIterations() {
		return iterations;
	}


	/**
	 * @param iterations the iterations to set
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}


	/**
	 * @return the concrrency
	 */
	@XmlAttribute(name="concurrency")
	public int getConcrrency() {
		return concrrency;
	}


	/**
	 * @param concrrency the concrrency to set
	 */
	public void setConcrrency(int concrrency) {
		this.concrrency = concrrency;
	}


	/**
	 * @return the timeout
	 */
	@XmlAttribute(name="timeout")
	public long getTimeout() {
		return timeout;
	}


	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}


	/**
	 * @return the outputDir
	 */
	@XmlAttribute(name="outputDir")
	public String getOutputDir() {
		return outputDir;
	}


	/**
	 * @param outputDir the outputDir to set
	 */
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * Copy local jar resource to the destination directory
	 * @param resource
	 * @param loc
	 * @throws IOException
	 */
	protected void copyResourceToLocation(String resource, File loc) throws IOException
	{
		InputStream iis = this.getClass().getResourceAsStream(resource);
		if (iis == null)
		{
			throw new IOException();
		}
		try
		{
			copyResourceToLocation(new InputStreamReader(iis), resource, loc);
		}
		finally
		{
			iis.close();
		}
	}
	
	/**
	 * Copy local jar resource to the destination directory
	 * @param resource
	 * @param loc
	 * @throws IOException
	 */
	protected void copyResourceToLocation(Reader iis, String resource, File loc) throws IOException
	{
		File end = new File(resource);		
		File newf = new File(loc, end.getName());
		FileWriter fios = new FileWriter(newf);
		char buf[] = new char[1024];
		int c = 0;
		while ((c = iis.read(buf)) != -1)
		{
			fios.write(buf, 0, c);
		}
		fios.flush();
		fios.close();
	}
	

	/**
	 * Prepare the report results.
	 * @param outputDir
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void prepare() throws InterruptedException, ExecutionException
	{
		try 
		{
			reportData = bindSimRule.fromObject(this);
			JSONObject rindex = new JSONObject();
			JSONObject aggByActionClass = new JSONObject();
			
			JSONObject rtypes = new JSONObject();
			reportData.put("rindex", rindex);
			reportData.put("rtypes", rtypes);
			
			File resources = new File(this.outputDir, this.outputFile);
			if (resources.exists())
			{
				char []buf = new char[1024];
				FileReader fis = new FileReader(resources);
				int n = 0;
				StringBuilder builder = new StringBuilder();
				while ((n = fis.read(buf)) != -1)
				{
					builder.append(buf, 0, n);
				}
				JSONArray arr = null;
				try
				{
					arr = new JSONArray(builder.toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return;
				}
				
			    ExecutorService statsService = Executors.newCachedThreadPool();
			    ChartAggregate ca = new ChartAggregate(arr);
			    Future<JSONObject> chartAgg = statsService.submit(ca);
			    
			    for (int i = 0; i < arr.length(); i++)
			    {
					JSONObject co = arr.optJSONObject(i);
					String etag = co.optString("id");
					if (etag.trim().isEmpty())
					{
						continue;
					}
					String name = co.optString("name");
					String rtype = name.split("[:]")[0];
					if (!rtypes.has(rtype))
					{
						rtypes.put(rtype, "");
					}
					
					JSONObject info = rindex.optJSONObject(etag);
					if (info == null)
					{
						info = new JSONObject();
						info.put("name", name);
						rindex.put(etag, info);
					}
					
					//replace stat
					if (co.optJSONObject("stat") != null)
					{
						info.remove("stat");							
						info.put("stat", new JSONObject(co.getJSONObject("stat").toString()));
					}
			    }
			    
			    //Create aggregated stat data for general reporting.
			    
			    JSONArray aggi = rindex.names();
			    for (int i  = 0; i < aggi.length(); i++)
			    {
			    	JSONObject info = rindex.optJSONObject(aggi.optString(i));
			    	JSONObject aggStat = info.optJSONObject("stat");
			    	JSONArray acts = aggStat.names();
			    	for (int j = 0; j < acts.length(); j++)
			    	{
			    		String name = acts.getString(j);
			    		JSONObject ste = aggStat.optJSONObject(name);
			    		String []parts = name.split("[:]");
			    		//Check if we have an action class already and if not aggregated it
			    		JSONObject typeAgg = aggByActionClass.optJSONObject(parts[0]);
			    		if (typeAgg == null)
			    		{
			    			typeAgg = new JSONObject();
			    			aggByActionClass.put(parts[0], typeAgg);
			    		}
			    		
			    		JSONObject atime = ste.optJSONObject("actualtime");
			    		
			    		if (atime == null)
			    		{
			    			continue;
			    		}
			    		
			    		long ttime = atime.optLong("totaltime");
			    		double tmax = atime.optDouble("max");
			    		double tmin = atime.optDouble("min");
			    		long tcount = atime.optLong("count");
			    		
			    		long xttime = typeAgg.optLong("totaltime", 0) + ttime;
			    		double xtmax = typeAgg.optDouble("max", 0) > tmax ? typeAgg.optDouble("max", 0) : tmax;
			    		double xtmin = typeAgg.optDouble("min", 0) < tmin ? typeAgg.optDouble("min", 0) : tmin;
			    		long xtcount = typeAgg.optLong("count", 0) + tcount;
			    		
			    		typeAgg.put("totaltime", xttime);
			    		typeAgg.put("max", xtmax);
			    		typeAgg.put("min", xtmin);
			    		typeAgg.put("count", xtcount);
			    	}
			    }
			    
			    //create chart data for aggregation.
			    JSONArray actionAggs = aggByActionClass.names();
			    JSONArray dta = new JSONArray();
			    aggByActionClass.put("totalTimeChartData", dta);
			    for (int i = 0; i < actionAggs.length(); i++)
			    {
			    	JSONObject rec = aggByActionClass.optJSONObject(actionAggs.optString(i));
			    	JSONArray item = new JSONArray();
			    	item.put(actionAggs.optString(i)+" time");
			    	item.put(rec.optLong("totaltime"));
			    	dta.put(item);
			    }
			    
			    dta = new JSONArray();
			    aggByActionClass.put("totalCountChartData", dta);
			    for (int i = 0; i < actionAggs.length(); i++)
			    {
			    	JSONObject rec = aggByActionClass.optJSONObject(actionAggs.optString(i));
			    	JSONArray item = new JSONArray();
			    	item.put(actionAggs.optString(i)+" counts");
			    	item.put(rec.optLong("count"));
			    	dta.put(item);
			    }
			    
			    
			    reportData.put("aggregatedChartData", aggByActionClass);
			    
			    JSONObject chardData = chartAgg.get();
			    if (chardData != null)
			    {
			    	reportData.put("chartData", chardData);
			    }				
			}
			
			
			//Output report data
			File routput = new File(this.outputDir, this.outputFile+"Report.js");
			FileWriter fw = new FileWriter(routput);
			fw.write("var reportData = "+reportData.toString()+";");
			fw.flush();
			
			File uiloc = new File(this.outputDir, "js");
			if (!uiloc.exists())
			{
				uiloc.mkdir();
				for (String uielem : UIResources) 
				{
					copyResourceToLocation(uielem, uiloc);					
				}
			}
			String rep = new String(reportTemplate);
			rep = rep.replace("${reportData}", routput.getName());
			copyResourceToLocation(new StringReader(rep), this.outputFile+"Report.html", new File(this.outputDir));
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		} 
		catch (InvocationTargetException e) 
		{
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
}
