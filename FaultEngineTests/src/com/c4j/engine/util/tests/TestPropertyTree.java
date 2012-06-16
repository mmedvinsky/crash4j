package com.c4j.engine.util.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.crash4j.engine.spi.util.PropertyOwner;
import com.crash4j.engine.spi.util.PropertyTree;
import com.crash4j.junit.CrashRunner;

public class TestPropertyTree
{
    class _props implements PropertyOwner
    {
        protected HashMap<String, String> props  = null;
        
        public _props(HashMap<String, String> props)
        {
            this.props = props;
        }
        
        @Override
        public boolean hasProperty(String name)
        {
            return props.containsKey(name);
            //return true;
        }

        @Override
        public boolean match(String name, String value)
        {
            String v = props.get(name);
            return v.equals(value);
            //return true;
        }

        @Override
        public boolean match(String name, Pattern value)
        {
            String v = props.get(name);
            return value.matcher(v).matches();
            //return true;
        }
        
        
    };
    
    protected static void addkvp(ArrayList<String[]> kvps, String k, String v)
    {
        String[] kvp = new String[2];        
        kvp[0] = new String(k);
        kvp[1] = new String(v);
        kvps.add(kvp);
    }
    
    @Test
    public void testPropertyTree()
    {
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("type", "yyy");
        props.put("prop2", "Test2");
        props.put("prop3", "www.google.com");
        props.put("4prop", "8080");
        props.put("5prop1", "hello");
        props.put("prop6", "t");
        props.put("7prop1", "xxx");
        props.put("8prop1", "Test12");
        props.put("9prop1", "Test13");
        props.put("1prop10", "Test14");
        _props o1 = new _props(props);
        
        
        PropertyTree<String> tree = new PropertyTree<String>();
        
        ArrayList<String[]> kvps = new ArrayList<String[]>();
        
        addkvp(kvps, "type", "xxx");
        addkvp(kvps, "prop3", "www.google.com");
        addkvp(kvps, "8prop1", "Test12");
        addkvp(kvps, "7prop1", "xxx");
        addkvp(kvps, "1prop10", "Test14");
        
        ArrayList<String[]> kvps2 = new ArrayList<String[]>();
        addkvp(kvps2, "type", "yyy");
        addkvp(kvps2, "prop3", "www.google.com");
        addkvp(kvps2, "8prop1", "Test12");
        addkvp(kvps2, "7prop1", "{[x]+}");
        addkvp(kvps2, "1prop10", "Test14");
        
        ArrayList<String[]> kvps3 = new ArrayList<String[]>();
        addkvp(kvps3, "type", "yyy");
        addkvp(kvps3, "prop3", "www.google.com");
        addkvp(kvps3, "8prop3", "Test12");
        addkvp(kvps3, "7prop0", "xxx");
        addkvp(kvps3, "1prop10", "Test14");
        
        ArrayList<String[]> kvps4 = new ArrayList<String[]>();
        addkvp(kvps4, "type", "yyy");
        addkvp(kvps4, "prop3", "www.google.com");
        addkvp(kvps4, "8prop3", "Test12");
        addkvp(kvps4, "7prop0", "xxx");
        addkvp(kvps4, "1prop11", "Test122");
        
        
        tree.addProperties(kvps, "Answer");
        tree.addProperties(kvps2, "Answer2");
        tree.addProperties(kvps3, "Answer3");
        tree.addProperties(kvps4, "Answer4");
        
        System.out.println(tree.match(o1));
        
        long nt = System.nanoTime();
        for (int i = 0; i < 1000000; i++)
        {
            tree.match(o1);
        }
        long n2 = System.nanoTime() - nt;
        System.out.println(n2/1000000);
    }

}
