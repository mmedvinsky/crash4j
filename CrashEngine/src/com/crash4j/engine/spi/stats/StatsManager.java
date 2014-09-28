/**
 * @copyright
 */
package com.crash4j.engine.spi.stats;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import com.crash4j.engine.Action;
import com.crash4j.engine.StatsCollector;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.JDBCResourceSpiImpl;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.types.ResourceTypes;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;

/**
 * {@link StatsManager} manages reported statistics for all resources.
 */
public class StatsManager implements Runnable
{
	protected int queueSize = 100;
	protected volatile boolean stop = false;
	protected int maxThreads = 3;
	protected volatile int spawnedThread = 0;
	protected static final Log log = LogFactory.getLog(StatsManager.class);
	
	protected boolean statrted = false;
	
	/** Monitors */
	protected ConcurrentHashMap<_resource_key, ConcurrentHashMap<Monitor, _stat_item>> monitors = 
			new ConcurrentHashMap<_resource_key, ConcurrentHashMap<Monitor, _stat_item>>(); 
	
	private LinkedBlockingDeque<_stat_record> squeue = new LinkedBlockingDeque<_stat_record>(queueSize);
	private LinkedBlockingDeque<_stat_record> recycled = new LinkedBlockingDeque<_stat_record>(queueSize);
		
	/**
	 * The record that is used to submit stats
	 */
	class _stat_record
	{
		ResourceSpi spi = null;
		StatTypes stat = null;
		UnitTypes unit = null;
		Action action = null;
		long value = 0;
		long bytes = 0;
	}
	
	/**
	 * ResourceKey used to index the resource monitors
	 */
	class _resource_key
	{
		ResourceSpi spi = null;
		String tag = null;
		/**
		 * 
		 * @param spi
		 */
		_resource_key(ResourceSpi spi)
		{
			this.spi = spi;
			this.tag = spi.getETag();
		}
		
		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) 
		{
			if (obj instanceof _resource_key)
			{
				return ((_resource_key)obj).tag.equals(this.tag);
			}
			return this.tag.equals(obj);
		}
		
		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() 
		{
			return this.tag.hashCode();
		}
		
		
	}
	class _stat_item
	{
		Monitor monitor = null;
		/**
		 * @param monitor
		 * @param spi
		 */
		public _stat_item(Monitor monitor) 
		{
			this.monitor = monitor;
		}
	}
	
	/**
	 * @return the queueSize
	 */
	public int getQueueSize() 
	{
		return queueSize;
	}

	/**
	 * @param queueSize the queueSize to set
	 */
	public void setQueueSize(int queueSize) 
	{
		this.queueSize = queueSize;
	}
	
	/**
	 * Start the stats processing
	 */
	public void start()
	{
		spawnedThread++;
		Thread th = new Thread(this);
		th.start();		
		this.statrted = true;
	}
	
	public void stop()
	{
		stop = true;
		this.statrted = false;
	}
	
	/**
	 * Executes stats consumptions here
	 */
	public void run()
	{
		LinkedList<_stat_record> recs = new LinkedList<_stat_record>();
		while (!stop)
		{
			synchronized (this)
			{
				double qstate = (this.squeue.size()/this.queueSize);
				if (qstate > 0.8)
				{
					spawnedThread++;
					Thread th = new Thread(this);
					th.start();
				}
				else if (qstate < 0.5 && spawnedThread > 1)
				{					
					spawnedThread--;
					return;
				}
			}
			
			while (this.squeue.drainTo(recs) > 0)
			{
				for (_stat_record sr : recs) 
				{
				    _resource_key rid = new _resource_key(sr.spi);
					ConcurrentHashMap<Monitor, _stat_item> mons = monitors.get(rid);
					if (mons == null)
					{
						 mons = new ConcurrentHashMap<Monitor, _stat_item>();
						 ConcurrentHashMap<Monitor, _stat_item> mt = monitors.putIfAbsent(rid, mons);
						if (mt != null)
						{
							mons = mt;
						}
					}
					
					//System.out.println(sr.spi+" "+sr.action+" "+sr.stat+" "+sr.unit);
					_stat_item m = new _stat_item(new Monitor(sr.action, sr.stat, sr.unit));
					_stat_item cMon = mons.putIfAbsent(m.monitor, m);
					if (cMon != null)
					{
						m = cMon;
					}
					m.monitor.addSample(sr.value, sr.bytes);
					recycled.offerFirst(sr);
				}
				recs.clear();
			}
			
			try 
			{
				TimeUnit.NANOSECONDS.sleep(1);
			} 
			catch (InterruptedException e) 
			{
			}
		}
	}
	
	/**
	 * Get a {@link Monitor} instance the can be used to submit stats against
	 * @param stat is a {@link StatTypes} selection of this monitor
	 * @param unit a {@link UnitTypes} for this monitor
	 * @param action a submitted action for this monitor
	 * @param time spent for this operation
	 */
	public void submit(ResourceSpi spi, StatTypes stat, UnitTypes unit, Action action, long val)
	{
		submit(spi, stat, unit, action, val, 0);
	}
	/**
	 * Get a {@link Monitor} instance the can be used to submit stats against
	 * @param stat is a {@link StatTypes} selection of this monitor
	 * @param unit a {@link UnitTypes} for this monitor
	 * @param action a submitted action for this monitor
	 * @param time spent for this operation
	 */
	public void submit(ResourceSpi spi, StatTypes stat, UnitTypes unit, Action action, long val, long bytes)
	{
		if (!this.statrted)
		{
			return;
		}
		
		if (action.getActionClassTypes() == null)
		{
			return;
		}
		
		
		//MM
		/*
		if (spi.getResourceType().equals(ResourceTypes.DB))
		{
			JDBCResourceSpiImpl ni = (JDBCResourceSpiImpl)spi;
			System.out.println(ni);
		}
		*/
		_stat_record st = this.recycled.pollFirst();
		if (st == null)
		{	
			st = new _stat_record();
		}

		st.action = action;
		st.spi = spi;
		st.stat = stat;
		st.value = val;
		st.unit = unit;
		st.bytes = bytes;
		
		
		//if (spi.getResourceType().equals(ResourceTypes.NET))
		//{
		//	if (spi.getVector().toString().indexOf("6553") != -1)
		//	{
		//		System.out.println("");
		//	}
		//}
		
		//log.logTrace(st.action, st.spi.getETag(), st.value);
		
		this.squeue.offerLast(st);
	}

	public void clearStats()
	{
		if (!this.statrted)
		{
			return;
		}
		
		this.monitors.clear();
		/*
		Collection<ConcurrentHashMap<Monitor, _stat_item>> values = this.monitors.values();
		for (ConcurrentHashMap<Monitor, _stat_item> m : values) 
		{
			for (_stat_item sti : m.values()) 
			{
				sti.monitor.clear();
			}
		}
		*/
	}
	
	/**
	 * Collect all stats
	 * @param collector
	 */
	public void collectAllStats(StatsCollector collector, long after)
	{
		if (!this.statrted)
		{
			return;
		}
		
		collector.begin();
		try
		{
			Set<Map.Entry<_resource_key, ConcurrentHashMap<Monitor, _stat_item>>> entries = this.monitors.entrySet();
			
			for (Map.Entry<_resource_key, ConcurrentHashMap<Monitor, _stat_item>> en : entries) 
			{
				_resource_key rk = en.getKey();
				
				if (en.getValue().isEmpty())
				{
					continue;
				}
				if (collector.getLastAccessTime() <= rk.spi.getLastAccess())
				{
					/*
		    		if (rk.spi.getResourceType().equals(ResourceTypes.DB))
		    		{
		    			System.out.println(rk.spi);
		    		}
		    		*/
					if (rk.spi.isComplete())
					{
						collector.enterResource(rk.spi);
						try
						{
							for (_stat_item sti : en.getValue().values()) 
							{
								sti.monitor.collect(rk.spi, collector);
							}
						}
						finally
						{
							collector.exitResource(rk.spi);
						}
					}
				}
			}
		}
		finally
		{
			collector.end();
		}
	}
	
	/**
	 * @return <code>true</code> if the {@link StatsManager} is started and false otherwise.
	 */
	public boolean isStarted()
	{
		return this.statrted;
	}
	
	/**
	 * Collect stats for a specific resource
	 * @param collector
	 * @param spi
	 */
    public void collectStats(StatsCollector collector, ResourceSpi spi, long after)
    {
		if (!this.statrted)
		{
			return;
		}
		
    	ConcurrentHashMap<Monitor, _stat_item> mons = monitors.get(spi.getETag());
    	Collection<_stat_item> values = mons.values();
    	for (_stat_item sti : values) 
    	{
			sti.monitor.collect(spi, collector);
		}
    }
}
