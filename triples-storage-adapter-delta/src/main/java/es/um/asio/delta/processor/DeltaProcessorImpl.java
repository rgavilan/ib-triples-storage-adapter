package es.um.asio.delta.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import es.um.asio.delta.interpreter.AddPropertyInterpreter;
import es.um.asio.delta.model.OperatorBuilder;

@Service
public class DeltaProcessorImpl implements DeltaProcessor {

	private final Logger logger = LoggerFactory.getLogger(DeltaProcessorImpl.class);

	@Autowired
	private OperatorBuilder operatorBuilder;

	@Autowired
	private AddPropertyInterpreter addPropertyInterpreter;

	@Override
	public void process(ArrayNode instructions) {
		logger.info(instructions.asText());

		if (instructions != null && instructions.size() > 0) {
			instructions.forEach(instruction -> run(instruction));
		}
	}

	private void run(JsonNode instruction) {
		addPropertyInterpreter.run(operatorBuilder.build(instruction));
	}

}
