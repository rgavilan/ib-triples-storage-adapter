package es.um.asio.service.trellis;

import org.apache.jena.rdf.model.Model;

public interface TrellisLinkOperations {

	/**
     * Creates the links entry.
     *
     * @param message the message
     */
    Model createLinksEntry(String id, String className);
}
