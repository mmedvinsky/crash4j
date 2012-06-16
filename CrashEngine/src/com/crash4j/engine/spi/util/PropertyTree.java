/**
 * @copyright
 */
package com.crash4j.engine.spi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crash4j.engine.spi.instrument.bcel.generic.GOTO;


/**
 * {@link PropertyTree} indexes vector of key/value pairs into a tree for faster lookup.
 * The tree supports regular expression and a wild card * for anything.
 * 
 * @author team
 *
 */
public class PropertyTree<T>
{
    protected final static int property_name  = 2;
    protected final static int property_value = 4;
    protected final static int property_regex = 8;
    protected final static int property_any   = 16;
    protected static final Pattern regexPattern = Pattern.compile("[{]([^}]+)[}]");
    protected _node<T> root = new _node<T>();
    /**
     * @author team
     */
    class _node<Y>
    {
        _node(String v, int t)
        {
            this.type = t;
            this.value = v;
            Matcher matcher = regexPattern.matcher(this.value);
            if (matcher.matches())
            {
                String rx = matcher.group(1);
                this.type = property_regex;
                this.pattern = Pattern.compile(rx);
            }
            else if (this.value.equals("*"))
            {
                this.type = property_regex;
                this.pattern = Pattern.compile(".*");
            }
        }
        
        _node()
        {
        }
        
        protected String value = null;
        protected Pattern pattern = null;
        protected int type = -1;
        protected ArrayList<_node<Y>> children = new ArrayList<_node<Y>>();
        protected T association = null;
    }
    
    /**
     * Match the {@link PropertyOwner} against the tree and return the selected match
     * 
     * @param owner
     */
    public T match(PropertyOwner owner)
    {
        return _match(this.root, owner);
    }
    
    /**
     * Match the {@link PropertyOwner} against the tree and return the selected match
     * 
     * @param owner
     */
    protected T _match(_node<T> n, PropertyOwner owner)
    {
        while (!n.children.isEmpty())
        {
            boolean matched = false;
            for (_node<T> cn : n.children)
            {
                //Check if we have this property
                if (owner.hasProperty(cn.value))
                {
                    for (_node<T> cnv : cn.children)
                    {
                        if ((cnv.type & property_regex) == property_regex)
                        {
                            matched = owner.match(cn.value, cnv.pattern);
                        }
                        else
                        {
                            matched = owner.match(cn.value, cnv.value);
                        }
                        
                        if (matched)
                        {
                            n = cnv;
                            break;
                        }
                    }
                }
                if (!matched)
                {
                    return null;
                }
                break;
            }
        }
        return n.association;
    }
    
    
    /**
     * @param vector of key value pairs.  
     */
    protected void _train(_node<T> cn, List<String[]> vector, int i, T assoc)
    {
       _node<T> nx = null;
       
       if (i >= vector.size())
       {
           cn.association = assoc;
           return;
       }
       
       String[]kvp = vector.get(i);
       
       if (cn.children.isEmpty())
       {
           _node<T> k = new _node<T>(kvp[0], property_name);
           _node<T> v = new _node<T>(kvp[1], property_value);
           k.children.add(v);
           cn.children.add(k);
           _train(v, vector, ++i, assoc);
           return;
       }
       
       for (_node<T> cs : cn.children)
       {
           if (cs.value.equalsIgnoreCase(kvp[0]))
           {
               for (_node<T> vn : cs.children)
               {
                   if (vn.value.equals(kvp[1]))
                   {
                       _train(vn, vector, ++i, assoc);
                       return;
                   }
               }
               _node<T> nn = new _node<T>(kvp[1], property_value);
               cs.children.add(nn);
               _train(nn, vector, ++i, assoc);
               return;
           }
           
           _node<T> k = new _node<T>(kvp[0], property_name);
           _node<T> v = new _node<T>(kvp[1], property_value);
           k.children.add(v);
           cn.children.add(k);
           _train(v, vector, ++i, assoc);
           return;
       }
       _train(nx, vector, ++i, assoc);
    }

    /**
     * @param vector of key value pairs.  
     */
    public void addProperties(List<String[]> vector, T assoc)
    {
        _train(this.root, vector, 0, assoc);
    }
    
}
