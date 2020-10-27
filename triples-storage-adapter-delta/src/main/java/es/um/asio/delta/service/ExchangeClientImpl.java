package es.um.asio.delta.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class ExchangeClientImpl implements ExchangeClient {

	@Override
	public ArrayNode retrieveDeltaFile(String currentVersion, String targetVersion) {
		// TODO call API-Exchange
		return mockup();
	}
	
	private ArrayNode mockup() {
		ArrayNode  result = null;

		ObjectMapper mapper = new ObjectMapper();
		
	    try {
	        result = mapper.readValue(new File("C:\\tmp\\input.json"), ArrayNode .class);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }   
	    return result;
	}

}
