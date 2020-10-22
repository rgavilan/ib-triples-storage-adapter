package es.um.asio.service.service.impl;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import es.um.asio.abstractions.constants.Constants;
import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.storage.StorageType;
import es.um.asio.service.service.TriplesStorageService;
import es.um.asio.service.trellis.TrellisLinkOperations;
import es.um.asio.service.trellis.TrellisOperations;

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
	
	
	/**
	 * Save links.
	 *
	 * @param message the message
	 */
	private void saveLinks(ManagementBusEvent message) {
		logger.info("Saving links in trellis: {}", message.getClassName());
		Model model = trellisLinkOperations.createLinksEntry(message);
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
	
}
