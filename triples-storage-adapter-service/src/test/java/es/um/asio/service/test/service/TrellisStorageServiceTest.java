package es.um.asio.service.test.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.domain.Operation;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.service.TriplesStorageService;
import es.um.asio.service.service.impl.TrellisStorageServiceImpl;
import es.um.asio.service.trellis.TrellisOperations;
import es.um.asio.service.util.TriplesStorageUtils;


@RunWith(SpringRunner.class)
public class TrellisStorageServiceTest {

    @TestConfiguration
    static class TrellisStorageServiceTestConfiguration {
        @Bean
        public TriplesStorageService triplesStorageService() {
            return new TrellisStorageServiceImpl();
        }
        @Bean
        public TriplesStorageUtils triplesStorageUtils()
        {
            return new TriplesStorageUtils();
        }
    }
    
    @Autowired
    private TriplesStorageService trellisStorageService;
    
    @MockBean(reset = MockReset.BEFORE)
    private TrellisOperations trellisOperations;

  
    @Test
    public void whenProcessInsertEvent_thenCreateEntry() throws TripleStoreException {
        ManagementBusEvent insertEvent = this.givenAManagementBusEvent(Operation.INSERT);
        
        trellisStorageService.process(insertEvent);
        
        verify(trellisOperations).createEntry(insertEvent);
    }
    
    @Test
    public void whenProcessInsertEvent_thenNoCreateEntryIfModelIdIsEmpty() throws TripleStoreException {
        ManagementBusEvent insertEvent = this.givenAManagementBusEvent(Operation.INSERT);
        insertEvent.setIdModel(StringUtils.EMPTY);
        
        trellisStorageService.process(insertEvent);
        
        verify(trellisOperations, never()).createEntry(insertEvent);
    }
    
    @Test
    public void whenProcessInsertEvent_thenCreateContainerIfNotExist() throws TripleStoreException {
        ManagementBusEvent insertEvent = this.givenAManagementBusEvent(Operation.INSERT);
        Mockito.when(trellisOperations.existsContainer(insertEvent)).thenReturn(false);
        
        trellisStorageService.process(insertEvent);
        
        verify(trellisOperations).createContainer(insertEvent);
    }
    
    @Test
    public void whenProcessInsertEvent_thenNoCreateContainerIfAlreadyExist() throws TripleStoreException {
        ManagementBusEvent insertEvent = this.givenAManagementBusEvent(Operation.INSERT);
        Mockito.when(trellisOperations.existsContainer(insertEvent)).thenReturn(true);
        
        trellisStorageService.process(insertEvent);
        
        verify(trellisOperations, never()).createContainer(insertEvent);
    }
    
        
    @Test
    public void whenProcessUpdateEvent_thenUpdateEntry() throws TripleStoreException {
        ManagementBusEvent udpateEvent = this.givenAManagementBusEvent(Operation.UPDATE);
        
        trellisStorageService.process(udpateEvent);
        
        verify(trellisOperations).updateEntry(udpateEvent);
    }
    
    @Test
    public void whenProcessUpdateEvent_thenNoUpdateEntryIfModelIdIsEmpty() throws TripleStoreException {
        ManagementBusEvent udpateEvent = this.givenAManagementBusEvent(Operation.UPDATE);
        udpateEvent.setIdModel(StringUtils.EMPTY);
        
        trellisStorageService.process(udpateEvent);
        
        verify(trellisOperations, never()).updateEntry(udpateEvent);
    }
    
    @Test
    public void whenProcessDeleteEvent_thenDeleteEntry() throws TripleStoreException {
        ManagementBusEvent deleteEvent = this.givenAManagementBusEvent(Operation.DELETE);
        
        trellisStorageService.process(deleteEvent);
        
        verify(trellisOperations).deleteEntry(deleteEvent);
    }
    
    @Test
    public void whenProcessDeleteEvent_thenNoDeleteEntryIfModelIdIsEmpty() throws TripleStoreException {
        ManagementBusEvent deleteEvent = this.givenAManagementBusEvent(Operation.DELETE);
        deleteEvent.setIdModel(StringUtils.EMPTY);
        
        trellisStorageService.process(deleteEvent);
        
        verify(trellisOperations, never()).deleteEntry(deleteEvent);
    }
 
    
    private ManagementBusEvent givenAManagementBusEvent(Operation operation) {
        ManagementBusEvent managementBusEvent = new ManagementBusEvent();
        managementBusEvent.setOperation(operation);
        managementBusEvent.setIdModel("dummyId");
        return managementBusEvent;
    }
}
