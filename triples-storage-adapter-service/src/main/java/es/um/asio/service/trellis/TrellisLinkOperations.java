package es.um.asio.service.trellis;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import org.apache.jena.rdf.model.Model;

public interface TrellisLinkOperations {

	/**
     * Creates the links entry.
     *
     * @param message the message
     */
    Model createLinksEntry(ManagementBusEvent message);
}
