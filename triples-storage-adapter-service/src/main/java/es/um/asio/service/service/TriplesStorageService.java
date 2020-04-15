package es.um.asio.service.service;

import es.um.asio.abstractions.domain.ManagementBusEvent;

/**
 * Triples storage service.
 */
public interface TriplesStorageService {
	void process(ManagementBusEvent message);
}
