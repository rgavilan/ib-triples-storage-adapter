package es.um.asio.delta.interpreter;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.delta.exception.TripleStoreException;

/**
 * Triples storage service.
 */
public interface DeltaService {
	void process(ManagementBusEvent message) throws TripleStoreException;
}
