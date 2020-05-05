package es.um.asio.service.repository;

import com.google.gson.JsonObject;

import es.um.asio.service.exception.TripleStoreException;

public interface WikibaseStorageRepository {
	void save(final JsonObject jData) throws TripleStoreException;
}
