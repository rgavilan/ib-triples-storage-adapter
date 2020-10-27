package es.um.asio.delta.proxy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;

import es.um.asio.delta.processor.DeltaProcessor;
import es.um.asio.delta.proxy.DeltaProxy;
import es.um.asio.delta.service.ExchangeClient;

@Service
public class DeltaProxyImpl implements DeltaProxy {
   
    @Autowired
    private ExchangeClient exchangeCliente;
    
    @Autowired
    private DeltaProcessor deltaProcessor;

	@Override
	public void process(String currentVersion, String targetVersion) {
		
		// we retrieve the delta file
		ArrayNode instructions = exchangeCliente.retrieveDeltaFile(currentVersion, targetVersion);
		
		// we process the delta file
		deltaProcessor.process(instructions);
	}

}
