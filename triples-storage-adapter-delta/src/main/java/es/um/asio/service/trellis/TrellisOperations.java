package es.um.asio.service.trellis;

import org.apache.jena.rdf.model.Model;

import es.um.asio.abstractions.domain.ManagementBusEvent;

/**
 * The Interface TrellisOperations.
 */
public interface TrellisOperations {    

    
    /**
    * Exists container.
    *
    * @param message the message
    * @return true, if successful
    */
    boolean existsContainer(ManagementBusEvent message);
    
    /**
     * Creates the container.
     *
     * @param message the message
     */
    void createContainer(ManagementBusEvent message);
    
    /**
     * Creates the entry.
     *
     * @param message the message
     */
    void createEntry(ManagementBusEvent message);
    
    /**
     * Update entry.
     *
     * @param message the message
     */
    void updateEntry(ManagementBusEvent message);
    
    /**
     * Delete entry.
     *
     * @param message the message
     */
    void deleteEntry(ManagementBusEvent message);
}
