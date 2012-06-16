/**
 * 
 */
package com.crash4j.engine.client.json;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Binding JSON to POJO and POJO to JSON using JAXB annotations 
 * as binding specifications.  
 */
public class JSONBinding<T> 
{
	private static final int t_jattribute = 2;
	private static final int t_jobject    = 4;
	private static final int t_jarray     = 8;
	private static final int t_jvalue     = 16;
	
	protected _binding_struct sroot = new _binding_struct(t_jobject);
	protected Class<T> rootClass = null;
	class _binding_struct
	{
		int mask = 0;
		String name = null;
		Class<?> bindingType = null;
		XmlAdapter<?, ?> adapter = null;
		PropertyDescriptor pdesc = null;
		BeanInfo beanInfo = null;
		ArrayList<_binding_struct> childern = new ArrayList<_binding_struct>();
		/**
		 * @param mask
		 */
		public _binding_struct(int mask) 
		{
			this.mask = mask;
		}
		/**
		 * @param mask
		 * @param name
		 */
		public _binding_struct(int mask, String name, PropertyDescriptor pd) 
		{
			this.mask = mask;
			this.pdesc = pd;
			this.name = name;
		}
		
		
	}
	
	/**
	 * Constructor
	 * @param c
	 * @throws IntrospectionException
	 */
	public JSONBinding(Class<T> c)
	{		
		try 
		{
			rootClass = c;
			createIndex(c, sroot);
		} 
		catch (IntrospectionException e) 
		{
		}
	}
	
	protected boolean isValue(Class<?> c)
	{
		return c.isAssignableFrom(String.class) || 
		c.isAssignableFrom(Integer.class) || 
		c.isAssignableFrom(Boolean.class) || 
		c.isAssignableFrom(Short.class) || 
		c.isAssignableFrom(Long.class) || 
		c.isAssignableFrom(Integer.class) || 
		c.isAssignableFrom(Byte.class) || 
		c.isAssignableFrom(Double.class) || 
		c.isAssignableFrom(double.class) || 
		c.isAssignableFrom(float.class) || 
		c.isAssignableFrom(long.class) || 
		c.isAssignableFrom(short.class) || 
		c.isAssignableFrom(int.class) || 
		c.isAssignableFrom(char.class) || 
		c.isAssignableFrom(byte.class);
	}
	
	/**
	 * Create an index for aster binding
	 * @param beanInfo
	 * @param bsh
	 * @throws IntrospectionException 
	 */
	protected void createIndex(Class<?> clazz, _binding_struct bsh) throws IntrospectionException
	{
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		bsh.beanInfo = beanInfo;
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor pd : props) 
		{
			Method m = pd.getReadMethod();
			
			//If it transient then skip it.....
			XmlTransient trans = m.getAnnotation(XmlTransient.class);
			if (trans != null)
			{
				continue;
			}
			
			//If this is a value then add a single child to <code>bsh</code>
			//that will contain the method pointer to the value
			XmlValue value = m.getAnnotation(XmlValue.class);
			if (value != null)
			{
				_binding_struct ob = new _binding_struct(t_jattribute, "value", pd);
				bsh.childern.add(ob);
				continue;
			}
			//If this is an attribute then add attribute node child to the <code>bsh</code>
			XmlAttribute att = m.getAnnotation(XmlAttribute.class);
			if (att != null)
			{
				_binding_struct ob = new _binding_struct(t_jattribute, att.name(), pd);
				bsh.childern.add(ob);
				continue;
			}
			
			XmlElement elem = m.getAnnotation(XmlElement.class);			
			if (elem != null)
			{
				_binding_struct ob = null;
				String name = elem.name();
				if (isValue(m.getReturnType()))
				{
					ob = new _binding_struct(t_jattribute, name, pd);
					bsh.childern.add(ob);
				}
				else if (Collection.class.isAssignableFrom(m.getReturnType()))
				{										
					Class<?> compType = elem.type();
					ob = new _binding_struct(t_jarray, name, pd);
					if (compType != null)
					{
						if (!isValue(compType))
						{
							_binding_struct aty = new _binding_struct(t_jobject, null, pd);
							createIndex(compType, aty);						
							ob.childern.add(aty);
						}
					}					
					bsh.childern.add(ob);
				}
				else
				{				
					ob = new _binding_struct(t_jobject, name, pd);
					bsh.childern.add(ob);
					ob.bindingType = elem.type();
					createIndex(m.getReturnType(), ob);
				}
			}
		}
	}
	
	public JSONObject fromObject(Object o) 
			throws JSONException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		JSONObject root = new JSONObject();
		fromObject(root, o, sroot);
		return root;
	}
	
	protected void fromObject(JSONObject parent, Object o, _binding_struct bsh) 
			throws JSONException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		if ((bsh.mask & t_jobject) == t_jobject)
		{
			for (_binding_struct bh : bsh.childern)
			{
				if ((bh.mask & t_jattribute) == t_jattribute)
				{
					Object result = bh.pdesc.getReadMethod().invoke(o);
					parent.put(bh.name, result);
				}
				else if ((bh.mask & t_jarray) == t_jarray)
				{
					JSONArray arr = new JSONArray();
					parent.put(bh.name, arr);
					Collection<?> result = (Collection<?>)bh.pdesc.getReadMethod().invoke(o);
					for (Object cb : result) 
					{
						if (bh.childern.isEmpty())
						{
							arr.put(cb.toString());
						}
						else
						{
							_binding_struct ab = bh.childern.get(0);
							JSONObject jo = new JSONObject();
							fromObject(jo, cb, ab); 
							arr.put(jo);
						}
					}
				}
				else if ((bh.mask & t_jobject) == t_jobject)
				{
					Object result = bh.pdesc.getReadMethod().invoke(o);
					JSONObject jo = new JSONObject();
					fromObject(jo, result, bh); 
				}
			}
		}
	}

	/**
	 * Populate object from json
	 * @param parent
	 * @param o
	 * @param bsh
	 * @throws JSONException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	protected void toObject(JSONObject parent, Object o, _binding_struct bsh) 
			throws JSONException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		if ((bsh.mask & t_jobject) == t_jobject)
		{
			for (_binding_struct bh : bsh.childern)
			{
				if ((bh.mask & t_jattribute) == t_jattribute)
				{
					Object v = parent.opt(bh.name);
					if (v != null)
					{
						bh.pdesc.getWriteMethod().invoke(o, v);
					}
				}
				else if ((bh.mask & t_jarray) == t_jarray)
				{
					JSONArray arr = parent.getJSONArray(bh.name);
					Collection coll = (Collection)bh.pdesc.getReadMethod().invoke(o);
					if (coll == null)
					{
						continue;
					}
					
					for (int i = 0; i < arr.length(); i++)
					{
						Object co = arr.get(i);
						if (bh.childern.isEmpty())
						{
							coll.add(co.toString());
						}
						else
						{
							_binding_struct ab = bh.childern.get(0);
							Class<?> clazz = ab.beanInfo.getBeanDescriptor().getBeanClass();
							Object oo = clazz.newInstance();
							toObject((JSONObject)co, oo, ab); 
							coll.add(oo.toString());
						}					
					}
				}
				else if ((bh.mask & t_jobject) == t_jobject)
				{
					JSONObject jo = parent.optJSONObject(bh.name);
					if (jo != null)
					{
						Class<?> clazz = bh.beanInfo.getBeanDescriptor().getBeanClass();
						Object oo = clazz.newInstance();
						toObject(jo, oo, bh); 
						bh.pdesc.getWriteMethod().invoke(o, oo);
					}
				}
			}
		}
	}
	
	
	public JSONArray fromCollection(Collection<?> c) 
			throws JSONException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		JSONArray arr = new JSONArray();
		for (Object object : c) 
		{
			arr.put(fromObject(object));
		}
		return arr;
	}

	public Object toObject(JSONObject o) throws JSONException, IllegalArgumentException, IllegalAccessException, 
							InvocationTargetException, InstantiationException
	{
		Class<?> clazz = this.sroot.beanInfo.getBeanDescriptor().getBeanClass();
		Object obj = clazz.newInstance();
		toObject(o, obj, this.sroot);
		return obj;
	}
	
	public Collection<?> toCollection(JSONArray arr) throws JSONException, IllegalArgumentException, IllegalAccessException, 
			InvocationTargetException, InstantiationException
	{
		LinkedList<T> list = new LinkedList<T>();
		for (int i = 0; i < arr.length(); i++)
		{
			Class<?> clazz = this.sroot.beanInfo.getBeanDescriptor().getBeanClass();
			Object obj = clazz.newInstance();
			toObject(arr.optJSONObject(i), obj, this.sroot);
		}
		return list;
	}
}
