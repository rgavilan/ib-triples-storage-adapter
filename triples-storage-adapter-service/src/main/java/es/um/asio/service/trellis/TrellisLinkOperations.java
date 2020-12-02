package es.um.asio.service.trellis;

import org.apache.jena.rdf.model.Model;

public interface TrellisLinkOperations {
	
    /**
     * Retrieve object from tellis.
     *
     * @param id the id
     * @param className the class name
     * @return the model
     */
    Model retrieveObjectFromTellis(String id, String className);
    
    /**
     * Update links entry.
     *
     * @param model the model
     * @param localUri the local uri
     * @param className the class name
     */
    void updateLinksEntry(Model model, String localUri, String className);
}
