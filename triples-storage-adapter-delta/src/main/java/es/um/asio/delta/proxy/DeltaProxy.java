package es.um.asio.delta.proxy;

/**
 * Proxy service for triples. Performs DTO conversion and permission checks.
 */
public interface DeltaProxy {

	void process(final String currentVersion, final String targetVersion);
}
