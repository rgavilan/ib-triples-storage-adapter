package es.um.asio.delta.model.operator;

import es.um.asio.delta.model.Operator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)

public class RenamePropertyOperator extends Operator {
	private String oldName;
	private String newName;
}
