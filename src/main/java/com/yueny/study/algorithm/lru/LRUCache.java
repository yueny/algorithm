package com.yueny.study.algorithm.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于JavaLinkedHashMap实现的 LRU 算法
 */
public class LRUCache<K,V> extends LinkedHashMap<K,V> {
    private final int CACHE_SIZE;

    /**
     *
     * @param cacheSize 保存传递进来的最大数据量
     */
    public LRUCache(int cacheSize){
        //设置hashmap的初始大小，同时最后一个true指的是让linkedhashmap按照访问顺序来进行排序，
        //最近访问的放在头，最老访问的放在尾
        super((int)Math.ceil(cacheSize/0.75)+1,0.75f,true);
        CACHE_SIZE = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest){
        //当map中的数据量大于指定的缓存个数的时候，就自动删除最老的数据。
        return size() > CACHE_SIZE;
    }
}
