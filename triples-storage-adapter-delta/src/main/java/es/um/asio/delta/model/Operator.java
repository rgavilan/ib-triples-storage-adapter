package es.um.asio.delta.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class Operator {

	private String targetEntity;
	private String action;
}
