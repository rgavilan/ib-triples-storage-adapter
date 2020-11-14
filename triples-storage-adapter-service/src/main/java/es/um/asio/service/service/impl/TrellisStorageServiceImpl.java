package es.um.asio.service.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import es.um.asio.abstractions.constants.Constants;
import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.perfomance.WatchDog;
import es.um.asio.service.service.TriplesStorageService;
import es.um.asio.service.service.uris.UrisFactoryClient;
import es.um.asio.service.trellis.TrellisLinkOperations;
import es.um.asio.service.trellis.TrellisOperations;
import es.um.asio.service.util.TriplesStorageUtils;

/**
 * Triples service implementation for Trellis.
 */
@ConditionalOnProperty(prefix = "app.trellis", name = "enabled", havingValue = "true", matchIfMissing = true)
@Service
public class TrellisStorageServiceImpl implements TriplesStorageService {

	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(TrellisStorageServiceImpl.class);
	
	/** The trellis operations. */
	@Autowired
	private TrellisOperations trellisOperations;
	
	/** The trellis link operations. */
	@Autowired
	private TrellisLinkOperations trellisLinkOperations;
	
	@Autowired
	private UrisFactoryClient urisFactoryClient;
	
		
	/**
	 * Process.
	 *
	 * @param message the message
	 */
	@Override
	public void process(ManagementBusEvent message) {
		switch (message.getOperation()) {
		case INSERT:
			this.save(message);
			break;
		case UPDATE:
		    this.update(message);
			break;
		case DELETE:
		    this.delete(message);
			break;
		case LINKED_INSERT:
			this.saveLinks(message);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Safety check.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	private String safetyCheck(Object obj) {
		String result = StringUtils.EMPTY;
		if (obj == null) {
			return result;
		}
		if (obj instanceof Number) {
			return ((Number) obj).toString();
		}
		if (obj instanceof String) {
			return (String) obj;
		}

		return result;
	}
	
	/**
	 * Save.
	 *
	 * @param message the message
	 */
	public void save(ManagementBusEvent message) {
		logger.info("Saving object in trellis: {} - {}", message.getClassName(), message.getIdModel());

		if(StringUtils.isNoneBlank(message.getIdModel())) {
			if(!trellisOperations.existsContainer(message)) {
			    trellisOperations.createContainer(message);
			}
			
			// we insert the entry in trellis
			trellisOperations.createEntry(message);
		}
	}
				
				
	private void update(ManagementBusEvent message) {
        logger.info("Updating object in trellis: {} - {}", message.getClassName(), message.getIdModel());

        if(StringUtils.isNoneBlank(message.getIdModel())) {            
            // we update the entry in trellis
            trellisOperations.updateEntry(message);
        }
    }    
	
	/**
	 * Delete.
	 *
	 * @param message the message
	 */
	private void delete(ManagementBusEvent message) {
	    logger.info("Deleting object in trellis: {} - {}", message.getClassName(), message.getIdModel());

        if(StringUtils.isNoneBlank(message.getIdModel())) {            
            // we delete the entry in trellis
            trellisOperations.deleteEntry(message);
        }
	}
	
	
	/**
	 * Save links.
	 *
	 * @param message the message
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void saveLinks(ManagementBusEvent message) {
		logger.info("Saving links in trellis: {}", message.getClassName());
		WatchDog saveLinksWatchDog = new WatchDog();
		
		
		try {
			Object obj = message.getLinkedModel();
			final String objectIdParent = this.safetyCheck(PropertyUtils.getProperty(obj, Constants.ID));
			final String classNameParent = (String) PropertyUtils.getProperty(obj, Constants.CLASS);
			Model model = trellisLinkOperations.createLinksEntry(objectIdParent, classNameParent);
			
			if(model != null) {
				String className = null;
				String fieldName = null;
				ArrayList<String> ids;
				LinkedHashMap<String, Object> item;
				
				LinkedHashMap<String, Object> params = (LinkedHashMap<String, Object>) message.getLinkedModel();
				ArrayList<LinkedHashMap<String, Object>> objetsToLink = (ArrayList) params.get(Constants.LINKED_TO);
				
				for (int i = 0; i < objetsToLink.size(); i++) {
					item = objetsToLink.get(i);
					
					className = (String) PropertyUtils.getProperty(item, "className");
					fieldName = (String) PropertyUtils.getProperty(item, "fieldName");
					ids = (ArrayList<String>) PropertyUtils.getProperty(item, "ids");
					
					if(ids != null && ids.size() > 0) {
						// we retrieve the canonical uri from parent
						String canonicalURIFromParent = this.urisFactoryClient.getCanonicalUriByResource(objectIdParent, classNameParent);
						Resource resource = model.getResource(canonicalURIFromParent);
						
						// we create the property in uri's factory
						String canonicalURIProperty = TriplesStorageUtils.removeLastWordFromUri(this.urisFactoryClient.createProperty(fieldName));				
						
						final Property property = model.createProperty(canonicalURIProperty, fieldName);
						
						// we add the nodes
						String canonicalURIFromSonObject;
						for(int j=0; j < ids.size(); j++) {
							// we retrieve the canonical uri from parent
							canonicalURIFromSonObject = this.urisFactoryClient.getCanonicalUriByResource(ids.get(j), className);
							if(StringUtils.isNotBlank(canonicalURIFromSonObject)) {
								RDFNode node = model.createResource(canonicalURIFromSonObject);
								resource.addProperty(property, node);
							}
						}						
					}
				} 
				
				// we save the new model
				String localUri = this.urisFactoryClient.getLocalStorageUriByResource(objectIdParent, classNameParent);
				trellisLinkOperations.updateLinksEntry(model, localUri);
			} else {
				this.logger.error("Error retrieving model from {}", message);
			}
			
		} catch (Exception e) {
			this.logger.error("Error saving links cause: {}", e.getMessage());
			this.logger.error("saveLinks", e);
		}
		
		// we print the watchdog results
		saveLinksWatchDog.takeTime("saveLinks");
		this.logger.warn("-----------------------------------------------------------------------");
		saveLinksWatchDog.printnResults(this.logger);
		this.logger.warn("-----------------------------------------------------------------------");
		
	}
}
