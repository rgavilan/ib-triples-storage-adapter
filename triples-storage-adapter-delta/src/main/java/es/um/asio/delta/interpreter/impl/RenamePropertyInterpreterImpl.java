package es.um.asio.delta.interpreter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.um.asio.delta.interpreter.UpdatePropertyInterpreter;
import es.um.asio.delta.interpreter.RenamePropertyInterpreter;
import es.um.asio.delta.model.Operator;
import es.um.asio.delta.model.operator.RenamePropertyOperator;

@Service
public class RenamePropertyInterpreterImpl implements RenamePropertyInterpreter {
	
	private final Logger logger = LoggerFactory.getLogger(RenamePropertyInterpreterImpl.class);
	
	@Autowired
	private UpdatePropertyInterpreter updatePropertyInterpreter;
	
	@Override
	public void run(Operator operator) {
		if(!(operator instanceof RenamePropertyOperator)) {
			updatePropertyInterpreter.run(operator);
		} else {
			logger.info("Executing: {} ", operator);
			updatePropertyInterpreter.run(operator);
		}
	}

}
