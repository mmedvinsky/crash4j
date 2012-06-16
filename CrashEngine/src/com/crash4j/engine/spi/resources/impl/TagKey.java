/**
 * @copyright
 */
package com.crash4j.engine.spi.resources.impl;

/**
 * Index into main has of resources.
 * @author team
 *
 */
public class TagKey
{
    protected String type = null;
    protected int coreHash = 0;
    protected int hash = 0;
    public TagKey(String type, int coreHash, int hash)
    {
        this.type = type;
        this.coreHash = coreHash;
        this.hash = hash;
    }
    
    public TagKey(String tag)
    {
        String[] parts = tag.split("[@]");
        this.type = parts[0];
        this.coreHash = (int)Long.parseLong(parts[1], 16);
        this.hash = (int)Long.parseLong(parts[2], 16);
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        TagKey k = null;
        if (obj instanceof TagKey)
        {
            k = (TagKey)obj;
            return k.type.equals(this.type) && k.coreHash == this.coreHash;
        }
        
        if (obj instanceof String)
        {
            k = new TagKey(obj.toString());
            return this.equals(k);
        }
        if (obj instanceof ResourceSpiImpl)
        {
            return obj.equals(this);
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return hash;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder(this.type);
        b.append("@").append(Integer.toHexString(this.coreHash)).append("@").append(Integer.toHexString(this.hash));
        return b.toString();
    }
}
