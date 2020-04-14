package es.um.asio.service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.service.TriplesStorageService;

/**
 * Triples service implmentation for Trellis.
 */
@Service
//TODO @ConditionalOnProperty()
public class TrellisStorageServiceImpl implements TriplesStorageService {

	/**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(TrellisStorageServiceImpl.class);

	
	@Override
	public void save(ManagementBusEvent message) {
		logger.info(message.toString());		
	}

}
