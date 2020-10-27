package es.um.asio.delta.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.um.asio.delta.model.operator.AddPropertyOperator;

@Component
public class OperatorBuilder {
	
	private final Logger logger = LoggerFactory.getLogger(OperatorBuilder.class);
	
	private ObjectMapper mapper;
	
	public OperatorBuilder() {
		super();
		mapper = new ObjectMapper();
		// if not you have "No content to map due to end-of-input"
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
	}

	public Operator build(JsonNode instruction) {
		Operator result = null;
		
		try {
			switch (instruction.get("action").asText(DeltaActions.UNKNOWN)) {
			case DeltaActions.ADD_PROPERTY:
				result = mapper.readValue(instruction.toString(), AddPropertyOperator .class);				
				break;
			// this should be in the last position
			case DeltaActions.UNKNOWN:
			default:
				break;
			}
	    } catch (Exception e) {
	    	logger.error("Error reading instruction: {} cause: {}", instruction, e.getMessage());
	        e.printStackTrace();
	    }   
		
		return result;
	}

}
