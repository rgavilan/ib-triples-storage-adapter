package es.um.asio.service.proxy;

import es.um.asio.abstractions.domain.ManagementBusEvent;

/**
 * Proxy service for triples. Performs DTO conversion and permission checks.
 */
public interface TriplesStorageProxy {

	void save(ManagementBusEvent message);
}
