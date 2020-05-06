package es.um.asio.service.proxy;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.exception.TripleStoreException;

/**
 * Proxy service for triples. Performs DTO conversion and permission checks.
 */
public interface TriplesStorageProxy {

	void process(ManagementBusEvent message) throws TripleStoreException;
}
