package es.um.asio.service.proxy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.proxy.TriplesStorageProxy;
import es.um.asio.service.service.TriplesStorageService;

/**
 * Proxy service implementation for triples. Performs DTO conversion and permission checks.
 */
@Service
public class TriplesStorageProxyImpl implements TriplesStorageProxy {

    /**
     * Service Layer.
     */
    @Autowired
    private TriplesStorageService service;

	@Override
	public void process(ManagementBusEvent message) throws TripleStoreException {
		this.service.process(message);		
	}

}
