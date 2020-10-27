package es.um.asio.delta.model.operator;

import es.um.asio.delta.model.DeltaType;
import es.um.asio.delta.model.Operator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)

public class AddPropertyOperator extends Operator {
	private String property;
	private DeltaType type;
}
