package es.um.asio.service.trellis.util;

import java.util.Map;

import org.springframework.stereotype.Service;

import es.um.asio.abstractions.constants.Constants;

@Service
public class TrellisCacheImpl implements TrellisCache {

	private Map<String, Object> cacheTrellisContainers;
	
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
			default:
				return null;
			}
	 	}

}
