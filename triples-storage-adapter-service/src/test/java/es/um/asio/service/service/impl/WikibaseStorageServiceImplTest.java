package es.um.asio.service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.domain.Operation;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.service.uris.UrisFactoryClient;
import es.um.asio.service.util.TriplesStorageUtils;
import es.um.asio.service.util.WikibaseUtils;
import es.um.asio.service.wikibase.WikibaseOperations;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class WikibaseStorageServiceImplTest {
	
	@Autowired
	private WikibaseStorageServiceImpl wikibaseStorageServiceImpl;
	
	@MockBean
	private TriplesStorageUtils triplesStorageUtils;
    
    /** 
     * The wikibase utils. 
    */
	@MockBean
    private WikibaseUtils wikibaseUtils;
    
    /**
     * Wikibase template
     */
	@MockBean
    private WikibaseOperations template;
    
    /** The uris factory client. */
	@MockBean
    private UrisFactoryClient urisFactoryClient;
	
	
	@TestConfiguration
	static class WikibaseStorageServiceImplTestConfig {
		@Bean
		WikibaseStorageServiceImpl wikibaseStorageServiceImpl() {
			return new WikibaseStorageServiceImpl();
		}
	}
	
	@Test
	public void processDELETE() throws TripleStoreException {
		ManagementBusEvent managementBusEvent = new ManagementBusEvent();
		managementBusEvent.setClassName("Universidad");
		managementBusEvent.setOperation(Operation.DELETE);
		managementBusEvent.setModel("");
		wikibaseStorageServiceImpl.process(managementBusEvent);
	}

}
