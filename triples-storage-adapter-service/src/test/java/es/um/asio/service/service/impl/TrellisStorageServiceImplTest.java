package es.um.asio.service.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.domain.Operation;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.service.uris.UrisFactoryClient;
import es.um.asio.service.trellis.TrellisLinkOperations;
import es.um.asio.service.trellis.TrellisOperations;
import es.um.asio.service.util.TriplesStorageUtils;
import es.um.asio.service.util.WikibaseUtils;
import es.um.asio.service.wikibase.WikibaseOperations;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class TrellisStorageServiceImplTest {
	
	@Autowired
	private TrellisStorageServiceImpl trellisStorageServiceImpl;
	
	@MockBean
	private TrellisOperations trellisOperations;
	
	/** The trellis link operations. */
	@MockBean
	private TrellisLinkOperations trellisLinkOperations;
	
	@MockBean
	private UrisFactoryClient urisFactoryClient;
	
	
	@TestConfiguration
	static class TrellisStorageServiceImplTestConfig {
		@Bean
		TrellisStorageServiceImpl trellisStorageServiceImpl() {
			return new TrellisStorageServiceImpl();
		}
	}
	
	@Test
	public void processDELETE() throws TripleStoreException {
		ManagementBusEvent managementBusEvent = new ManagementBusEvent();
		managementBusEvent.setClassName("Universidad");
		managementBusEvent.setOperation(Operation.DELETE);

		managementBusEvent.setModel("");
		trellisStorageServiceImpl.process(managementBusEvent);
	}

}
