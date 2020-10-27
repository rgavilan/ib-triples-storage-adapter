package es.um.asio.delta.proxy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.delta.exception.TripleStoreException;
import es.um.asio.delta.proxy.DeltaProxy;
import es.um.asio.delta.service.interpreter.DeltaService;

/**
 * Proxy service implementation for triples. Performs DTO conversion and permission checks.
 */
@Service
public class DeltaProxyImpl implements DeltaProxy {

    /**
     * Service Layer.
     */
    @Autowired
    private DeltaService service;

	@Override
	public void process(String currentVersion, String targetVersion) {
		// TODO Auto-generated method stub
	}

}
