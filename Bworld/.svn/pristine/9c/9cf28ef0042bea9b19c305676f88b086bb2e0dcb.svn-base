package com.bworld.misc;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import android.graphics.Bitmap;

// TODO: Auto-generated Javadoc
/**
 * The Class MemoryCache.
 */
public class MemoryCache {
    
    /** The cache. */
    private HashMap<String, SoftReference<Bitmap>> cache=new HashMap<String, SoftReference<Bitmap>>();
    
    /**
     * Gets the.
     *
     * @param id the id
     * @return the bitmap
     */
    public Bitmap get(String id){
        if(!cache.containsKey(id))
            return null;
        SoftReference<Bitmap> ref=cache.get(id);
        return ref.get();
    }
    
    /**
     * Put.
     *
     * @param id the id
     * @param bitmap the bitmap
     */
    public void put(String id, Bitmap bitmap){
        cache.put(id, new SoftReference<Bitmap>(bitmap));
    }

    /**
     * Clear.
     */
    public void clear() {
        cache.clear();
    }
}