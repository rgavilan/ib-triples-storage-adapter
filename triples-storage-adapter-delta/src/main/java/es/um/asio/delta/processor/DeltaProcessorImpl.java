package es.um.asio.delta.processor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import es.um.asio.delta.interpreter.RenamePropertyInterpreter;
import es.um.asio.delta.model.OperatorBuilder;

@Service
public class DeltaProcessorImpl implements DeltaProcessor {

	private final Logger logger = LoggerFactory.getLogger(DeltaProcessorImpl.class);

	@Autowired
	private OperatorBuilder operatorBuilder;

	@Autowired
	private RenamePropertyInterpreter renamePropertyInterpreter;

	@Override
	public void process(ArrayNode instructions) {
		if (instructions != null && instructions.size() > 0 && StringUtils.isNotEmpty(instructions.asText())) {
			logger.info(instructions.asText());
			instructions.forEach(this::run);
		} else {
			logger.warn("Empty instructions to run!!!");
		}
	}

	private void run(JsonNode instruction) {
		renamePropertyInterpreter.run(operatorBuilder.build(instruction));
	}

}
