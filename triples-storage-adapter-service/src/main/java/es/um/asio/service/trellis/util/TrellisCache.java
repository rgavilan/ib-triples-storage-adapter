package es.um.asio.service.trellis.util;

public interface TrellisCache {
	Object find(String key, String cacheName);

	void saveInCache(String key, Object value, String cacheName);
}
