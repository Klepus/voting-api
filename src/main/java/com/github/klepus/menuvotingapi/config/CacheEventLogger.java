package com.github.klepus.menuvotingapi.config;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEventLogger implements CacheEventListener<Object, Object> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        log.info("Event '{}' fired for key '{}' with value {}", cacheEvent.getType(), cacheEvent.getKey(), cacheEvent.getNewValue());
    }
}