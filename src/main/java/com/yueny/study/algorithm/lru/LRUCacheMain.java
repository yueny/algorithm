package com.yueny.study.algorithm.lru;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LRUCacheMain {
    public static void main(String[] args) {
        LRUCache<String, Integer> lruCache = new LRUCache(5);

        for (int i=0; i< 100; i++) {
            lruCache.put("" + i, i);
        }

        System.out.printf("values: "+ lruCache.values());
    }
}
