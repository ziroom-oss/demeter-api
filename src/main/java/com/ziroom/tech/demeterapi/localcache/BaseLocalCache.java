package com.ziroom.tech.demeterapi.localcache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

 /*
 * 本地缓存基类
 *
 * @author xuzeyu
 */

public abstract class BaseLocalCache<P,T> {

    private final static Logger LOGGER_INFO = LoggerFactory.getLogger(BaseLocalCache.class);
    private final static Logger LOGGER_ERROR = LoggerFactory.getLogger(BaseLocalCache.class);

    public LoadingCache<P, T> loadingCache;

    private static final long CACHE_MAX_SIZX = 2000L;

    private static final long CACHE_TIME = 5;

    public LoadingCache<P, T> loadCache(CacheLoader<P, T> cacheLoader){
        return CacheBuilder.newBuilder()
                .maximumSize(CACHE_MAX_SIZX)
                .expireAfterWrite(CACHE_TIME, TimeUnit.MINUTES)
                .build(cacheLoader);
    }

    public T get(P param) {
        if (Objects.isNull(param)){
            return null;
        }
        try {
            return loadingCache.get(param);
        } catch (ExecutionException e) {
            LOGGER_INFO.info("BaseLocalCache.get({}) ERROR {}!", param, e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
