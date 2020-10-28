package es.um.asio.delta.model;

public final class DeltaActions {

	private DeltaActions() {
		throw new IllegalStateException("Utility class");
	}

	public static final String RENAME_PROPERTY = "RENAME_PROPERTY";
	public static final String UPDATE_PROPERTY = "UPDATE_PROPERTY";

	public static final String DELETE_PROPERTY = "DELETE_PROPERTY";

	public static final String RENAME_ENTITY = "RENAME_ENTITY";

	public static final String DELETE_ENTITY = "DELETE_ENTITY";

	public static final String UNKNOWN = "UNKNOWN";
}
