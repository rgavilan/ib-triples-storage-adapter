package es.um.asio.delta.processor;

import com.fasterxml.jackson.databind.node.ArrayNode;

public interface DeltaProcessor {

	void process(ArrayNode instructions);
}
