package es.um.asio.delta.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class Operator {

	private String targetEntity;
	private DeltaAction action;
}
