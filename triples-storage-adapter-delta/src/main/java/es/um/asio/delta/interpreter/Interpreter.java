package es.um.asio.delta.interpreter;

import es.um.asio.delta.model.Operator;

public interface Interpreter {

	void run(Operator operator);
}
