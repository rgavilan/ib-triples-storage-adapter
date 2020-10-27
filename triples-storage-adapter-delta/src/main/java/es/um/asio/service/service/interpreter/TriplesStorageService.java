package es.um.asio.service.service.interpreter;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.exception.TripleStoreException;

/**
 * Triples storage service.
 */
public interface TriplesStorageService {
	void process(ManagementBusEvent message) throws TripleStoreException;
}
