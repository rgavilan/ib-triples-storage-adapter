package es.um.asio.service.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.domain.Operation;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.service.TriplesStorageService;
import es.um.asio.service.service.impl.TrellisStorageServiceImpl;
import es.um.asio.service.util.TrellisUtils;


@RunWith(SpringRunner.class)
public class TrellisStorageServiceTest {

    @TestConfiguration
    static class TrellisStorageServiceTestConfiguration {
        @Bean
        public TriplesStorageService triplesStorageService() {
            return new TrellisStorageServiceImpl();
        }
        @Bean
        public TrellisUtils trellisUtils()
        {
            return new TrellisUtils();
        }
    }
    
    @Autowired
    private TriplesStorageService trellisStorageService;

    
    @Test
    public void whenProcessEvent_thenNoException() throws TripleStoreException
    {
        ManagementBusEvent managementBusEvent = new ManagementBusEvent();
        managementBusEvent.setOperation(Operation.DELETE);
        trellisStorageService.process(managementBusEvent);
    }
}
