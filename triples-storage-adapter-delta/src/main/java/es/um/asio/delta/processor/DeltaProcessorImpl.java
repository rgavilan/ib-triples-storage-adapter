package es.um.asio.delta.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class DeltaProcessorImpl implements DeltaProcessor {

	
	private final Logger logger = LoggerFactory.getLogger(DeltaProcessorImpl.class);
	
	@Override
	public void process(ArrayNode instructions) {
		logger.info(instructions.asText());
		
	}

}
