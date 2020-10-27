package es.um.asio.delta.interpreter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.um.asio.delta.interpreter.AddEntityInterpreter;
import es.um.asio.delta.interpreter.AddPropertyInterpreter;
import es.um.asio.delta.model.Operator;
import es.um.asio.delta.model.operator.AddPropertyOperator;

@Service
public class AddPropertyInterpreterImpl implements AddPropertyInterpreter {
	
	
	private final Logger logger = LoggerFactory.getLogger(AddPropertyInterpreterImpl.class);
	
	@Autowired
	private AddEntityInterpreter addEntityInterpreter;
	
	@Override
	public void run(Operator operator) {
		if(!(operator instanceof AddPropertyOperator)) {
			addEntityInterpreter.run(operator);
		} else {
			logger.info("Executing: {} ", operator);
		}
	}

}
