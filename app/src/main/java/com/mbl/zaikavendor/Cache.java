package com.mbl.zaikavendor;

/**
 * Created by sachin on 5/11/2017.
 */
import android.support.v4.util.LruCache;

public class Cache {

    private static Cache instance;
    private LruCache<Object, Object> lru;

    private Cache() {

        lru = new LruCache<Object, Object>(1024);

    }

    public static Cache getInstance() {

        if (instance == null) {

            instance = new Cache();
        }

        return instance;

    }

    public LruCache<Object, Object> getLru() {
        return lru;
    }
}
