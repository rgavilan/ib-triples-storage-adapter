package es.um.asio.service.trellis.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import es.um.asio.abstractions.constants.Constants;

@Service
public class TrellisCacheImpl implements TrellisCache {

	private Map<String, Object> cacheTrellisContainers;
	
	private Map<String, Object> cacheProperties;
	
	
	public TrellisCacheImpl() {
		this.cacheTrellisContainers = new HashMap<>();
		this.cacheProperties = new HashMap<>();
	}

	@Override
	public Object find(String key, String cacheName) {		
		return this.retrieveCache(cacheName).get(key);
	}

	@Override
	public void saveInCache(String key, Object value, String cacheName) {
		this.retrieveCache(cacheName).put(key, value);
	}
	
	 private Map<String, Object> retrieveCache(String cacheName) {
	 		switch (cacheName) {
			case Constants.CACHE_TRELLIS_CONTAINER:
				return cacheTrellisContainers;
			case Constants.CACHE_PROPERTIES:
				return cacheProperties;
			default:
				return null;
			}
	 	}

}
