/**
 * 
 */
package com.crash4j.engine.spi;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.crash4j.engine.Action;
import com.crash4j.engine.spi.actions.ActionImpl;
import com.crash4j.engine.spi.json.JSONArray;
import com.crash4j.engine.spi.json.JSONObject;
import com.crash4j.engine.spi.json.JSONTokener;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.traits.LifecycleHandler;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.ResourceTypes;


/**
 * Core Class List loads and parses the core.inst file that contains a list
 * of all core classes that are to be transformed and loaded by the processor
 * TODO rewrite this class
 */
class ResourceSpecLoader
{
    private ResourceSpec[] ordSpecs = null;
    private HashMap<String, ResourceSpec> fullIndex = new HashMap<String, ResourceSpec>(); 
    private HashMap<String, ArrayList<ResourceSpec>> classIndex = new HashMap<String, ArrayList<ResourceSpec>>();
    private HashMap<Action, Action> actions = new HashMap<Action, Action>();
    private ArrayList<Action> ordActions = new ArrayList<Action>();
    
    /**
     * 
     * @author team
     *
     */
    class _spec implements ResourceSpec
    {
        String key = null;
        int id = 0;
        boolean inf = false;
        private String className = null;
        private String signature = null;
        private String method = null;
        private Action action = null;
        private boolean isContructor = false;
        private boolean instrumentSubclasses = false;
        private String resfac = null;
        private LifecycleHandler handler = null;
        private ResourceTypes rType = null;
        private HashMap<String, String> attrs = null;
        private boolean mnative = false;
        private Set<Class<?>> exceptions = new HashSet<Class<?>>();
        protected Log log = LogFactory.getLog(ResourceSpec.class);
        protected int accepts = 0;
        
        /**
         * Create spec
         */
        _spec(String clz, String method, String full, String sig,  boolean nat, 
        		LifecycleHandler handler, Action defact, ResourceTypes rType, HashMap<String, String> attrs, 
                Set<Class<?>> exceptions, boolean isub, int accepts)
        {
            this.key = full;
            this.id = -1;
            this.resfac = null;
            this.className = clz;
            this.accepts = accepts;
            this.signature = sig;
            this.method = method;
            this.instrumentSubclasses = isub;
            this.action = defact;
            this.isContructor = this.method.equalsIgnoreCase("<init>");
            this.rType = rType;
            this.mnative = nat;
            this.handler = handler;
            this.attrs = attrs;
            this.exceptions.addAll(exceptions);
       }
        
        
        /**
         * Create spec
         */
        _spec(int id, String clz, String method, String full, String sig,  boolean nat, 
                String resfac, String rType, HashMap<String, String> attrs, 
                Set<String> errors, boolean isub, int accepts)
        {
            this.key = full;
            this.id = id;
            this.resfac = null;
            this.className = clz;
            this.accepts = accepts;
            this.signature = sig;
            this.method = method;
            this.instrumentSubclasses = isub;
            this.action = action;
            this.isContructor = this.method.equalsIgnoreCase("<init>");
            this.resfac = resfac;
            this.rType = ResourceTypes.fromString(rType);
            this.mnative = nat;
            try
            {
                this.handler = (LifecycleHandler)Class.forName(this.resfac).newInstance();
            } 
            catch (Exception e)
            {
                log.logError("Unable to instantiate "+this.resfac, e);
            }
            this.attrs = attrs;
            //pre-comp error classes
            for (String ee : errors)
            {
                try
                {
                    Class c = Class.forName(ee);
                    exceptions.add(c);
                } 
                catch (ClassNotFoundException e)
                {
                    log.logError("Unable to resolve: "+ee, e);
                }
            }
        }
        
        /**
		 * @return the actions
		 */
        @Override
		public Action getDefaultAction() {
			return action;
		}

		/**
		 * @param actions the actions to set
		 */
		public void setDefaultAction(Action action) {
			this.action = action;
		}

		/**
         * @return the exceptions
         */
        public Set<Class<?>> getExceptions()
        {
            return exceptions;
        }

        /**
         * @return the method
         */
         @Override
        public String getMethod()
        {
            return method;
        }

        /**
         * @see com.crash4j.engine.spi.ResourceSpec#getEntityName()
         */
        @Override
        public String getEntityName()
        {
            return this.className;
        }


        @Override
        public String getSignature() 
        {
            return this.signature;
        }

        @Override
        public int getId()
        {
            return this.id;
        }

        @Override
        public String getKey()
        {
            return this.key;
        }

        @Override
        public boolean isConstructor()
        {
            return this.isContructor;
        }

        @Override
        public String getResourceFactoryImpl()
        {
            return resfac;
        }

        @Override
        public LifecycleHandler getHandler()
        {
             return this.handler;
        }

        @Override
        public ResourceTypes getResourceType()
        {
            return this.rType;
        }

        @Override
        public String getAttribute(String name)
        {
            return this.attrs.get(name);
        }

        @Override
        public boolean isNative()
        {
            // TODO Auto-generated method stub
            return this.mnative;
        }

		@Override
		public boolean instrumentSubclasses() 
		{
			return instrumentSubclasses;
		}

		@Override
		public int getAccepts() 
		{
			return this.accepts;
		}

		@Override
		public ActionClasses getActionClass() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enum<?> getActionClassTypes() {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    public void load(String uri) throws Exception
    {

        URL url = new URL(uri);
        URLConnection cc = url.openConnection();
        InputStream is = cc.getInputStream();           
        this.load(is);
        is.close();
    }
    
    private static String getJSONValue(JSONObject o, String name)
    {
        try
        {
            return o.getString(name);
        }
        catch (Exception e)
        {
            
        }
        return null;
    }
    
    /**
     * Load the spec from the {@link URL}
     * @param source
     * @throws Exception
     */
    public void load(InputStream source) throws Exception
    {
    	//Read the stream.
        //StringBuilder sspec = new StringBuilder();
        //Utils.readStream(sspec, source);
        
        
        ArrayList<ResourceSpec> ospecs = new ArrayList<ResourceSpec>(); 
        ///Read the stream
        JSONObject spec = new JSONObject(new JSONTokener(source));
        //Get the list of entities
        JSONObject obj = (JSONObject)spec.get("entities");        
        JSONArray entities = obj.names();
        
        int index = this.ordSpecs == null ? 0 : this.ordSpecs.length;
        
        for (int i = 0; i < entities.length(); i++)
        {
            String ename = entities.getString(i);
            //Create class index
            ArrayList<ResourceSpec> classMembers = this.classIndex.get(ename);
            if (classMembers == null)
            {
                classMembers = new ArrayList<ResourceSpec>();
                this.classIndex.put(ename, classMembers);
            }
            
            //Go over classes and methods
            
            JSONObject especs = (JSONObject)obj.get(ename);
            //If set this directive will tell byte code processor to instrument subclasses of this entity
            //For every signature included in the spec.
            boolean instrumentSubclasses = especs.optBoolean("instrumentSubclasses");
            //Declare resource type for this entity
            String rtype = getJSONValue(especs, "rtype");
                        
            //Describe who will be accepted by the EventHandler
            JSONArray accepts = especs.optJSONArray("accept");            
            int accepstMask = 0;
            if (accepts != null)
            {
            	for (int y = 0; y < accepts.length(); y++)
            	{
            		accepstMask |= ResourceTypes.fromString(accepts.getString(y)).toMask();
            	}
            }
            
            //Iterate of names of to get detailed entry point specifications.
            JSONArray mspecs = especs.names();
            for (int j = 0; j < mspecs.length(); j++)
            {
                String mspecName = mspecs.getString(j);
                
                boolean is = instrumentSubclasses;
                if (mspecName.equalsIgnoreCase("instrumentSubclasses"))
                {
                	is = especs.optBoolean("instrumentSubclasses");
                	continue;
                }
                
                int lAccepstMask = accepstMask;
                if (mspecName.equalsIgnoreCase("accept"))
                {
                    JSONArray la = especs.optJSONArray("accept");
                    
                    if (la != null)
                    {
                    	for (int y = 0; y < la.length(); y++)
                    	{
                    		lAccepstMask |= ResourceTypes.fromString(la.getString(y)).toMask();
                    	}
                    }
                	continue;
                }
                
                if (mspecName.equalsIgnoreCase("rtype"))
                {
                	rtype = especs.optString("rtype");
                	continue;
                }
                
                if (mspecName.equalsIgnoreCase("aclasstype"))
                {
                	int a = 0;
                	a++;
                }
                JSONObject mspec = especs.getJSONObject(mspecName);                
                
                String method = getJSONValue(mspec, "method");
            	String aclass = mspec.optString("aclass");
            	String aclasstype = mspec.optString("aclasstype");
                
                ActionImpl action = null;
            	if (aclass == null)
            	{
            		throw new Exception("Undefined ActionClass");
            	}
            	ActionClasses actc = ActionClasses.valueOf(aclass);
            	action = new ActionImpl(method, actc, actc.getClassActionTypeFromString(aclasstype));
                
                HashSet<String> exs = new HashSet<String>();
                JSONArray errors = null;
                try { errors = mspec.getJSONArray("errors"); } catch (Exception e) {}
                if (errors != null)
                {
                    for (int l = 0; l < errors.length(); l++)
                    {
                        exs.add(errors.getString(l));
                    }
                }
                
                String fullsig = ename+"#"+mspec.getString("signature");
                HashMap<String, String> attrs=null;
                try
                {
                    JSONObject jattrs= mspec.getJSONObject("attrs");
                    attrs = new HashMap<String, String>();
                    JSONArray attnames = jattrs.names();
                    for (int k = 0; k < attnames.length(); k++)
                    {
                        String aname = attnames.getString(k);
                        String aval = jattrs.getString(aname);
                        attrs.put(aname, aval);
                    }
                }
                catch (Exception e)
                {
                    //just carry on
                }
                
                if (getJSONValue(mspec, "rtype") != null)
                {
                	rtype = getJSONValue(mspec, "rtype");
                }
                
                _spec s = new _spec(index, ename, method, fullsig, 
                        getJSONValue(mspec, "signature"), 
                        new Boolean(getJSONValue(mspec,"native")),
                        getJSONValue(mspec, "handler"), 
                        rtype, attrs, exs, is, lAccepstMask);
                
                s.setDefaultAction(action);
                
                classMembers.add(s);
                //System.out.println(fullsig);
                this.fullIndex.put(fullsig, s);
                ospecs.add(s);
                index++;
            }        
        }    
        
        if (this.ordSpecs == null)
        {
            this.ordSpecs = ospecs.toArray(new ResourceSpec[0]);
        }
        else
        {
            ResourceSpec[] newSpecs = new ResourceSpec[this.ordSpecs.length + ospecs.size()];
            System.arraycopy(this.ordSpecs, 0, newSpecs, 0, this.ordSpecs.length);
            System.arraycopy(ospecs.toArray(new ResourceSpec[0]), 0, newSpecs, this.ordSpecs.length, ospecs.size());
            this.ordSpecs = newSpecs;
        }
    }
    
    /**
     * Default constructor
     */
    public ResourceSpecLoader()
    {
    }
    
    /**
     * Create new spec
     * @return
     */
    public ResourceSpec createSpec(String clz, String method, String full, String sig,  boolean nat, 
    		LifecycleHandler handler, Action defact, ResourceTypes rType, HashMap<String, String> attrs, 
            Set<Class<?>> exceptions, boolean isub, int accepts)
    {
    	return new _spec(clz, method, full, sig, nat, 
    		handler, defact, rType, attrs, exceptions, isub, accepts);
    }
    /**
     * @param id
     * @return {@link ResourceSpec} at id
     */
    public ResourceSpec getSpecById(int id)
    {
        return this.ordSpecs[id];
    }
    
    /**
     * @return the classIndex
     */
    public List<ResourceSpec> getByClassName(String cl) 
    {
        return classIndex.get(cl);
    }

    /**
     * @return the classIndex
     */
    public HashMap<String, ArrayList<ResourceSpec>> getAllByClass() 
    {
        return classIndex;
    }


    /**
     * @return the ordSpecs
     */
    public ResourceSpec[] getSpecs()
    {
        return ordSpecs;
    }

    public Action[] getActions()
    {
    	return this.ordActions.toArray(new Action[0]);
    }
    
    /**
     * @return the classIndex
     */
    public ResourceSpec getByFullSignature(String sig) 
    {
        return this.fullIndex.get(sig);
    }
}
