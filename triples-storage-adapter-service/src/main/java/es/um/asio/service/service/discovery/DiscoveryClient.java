package es.um.asio.service.service.discovery;

import es.um.asio.abstractions.domain.Operation;

public interface DiscoveryClient {
	void eventNotifyDiscovery(Operation operation, String className, String localTripleStorageUri, String subDomain,
			String tripleStorage);

}
