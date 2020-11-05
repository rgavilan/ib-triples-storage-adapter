package es.um.asio.service.test.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.wikidata.wdtk.datamodel.implementation.MonolingualTextValueImpl;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.domain.Operation;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.service.TriplesStorageService;
import es.um.asio.service.service.impl.WikibaseStorageServiceImpl;
import es.um.asio.service.util.TriplesStorageUtils;
import es.um.asio.service.util.WikibaseUtils;
import es.um.asio.service.wikibase.WikibaseOperations;

@RunWith(SpringRunner.class)
public class WikibaseStorageServiceTest {

    @TestConfiguration
    static class WikibaseStorageServiceTestConfiguration {
        @Bean
        public TriplesStorageService triplesStorageService() {
            return new WikibaseStorageServiceImpl();
        }
        @Bean
        public TriplesStorageUtils triplesStorageUtils()
        {
            return new TriplesStorageUtils();
        }
        
    }
    
    @MockBean
    private WikibaseUtils wikibaseUtils;   
   
    
    @MockBean(reset = MockReset.BEFORE)
    private WikibaseOperations wikibaseOperations;
    
    @Autowired
    private TriplesStorageService wikibaseStorageService; 
    
    
    @Test
    public void whenOperationIsUpdate_thenNotExecuteAnyOperation() throws TripleStoreException {
        ManagementBusEvent updateEvent = givenAManagementBusEvent(Operation.UPDATE);
        
        wikibaseStorageService.process(updateEvent);
        
        verifyNoMoreInteractions(wikibaseOperations);
    }
    
    @Test
    public void whenOperationIsDelete_thenNotExecuteAnyOperation() throws TripleStoreException {
        ManagementBusEvent deleteEvent = givenAManagementBusEvent(Operation.DELETE);
        
        wikibaseStorageService.process(deleteEvent);
        
        verifyNoMoreInteractions(wikibaseOperations);
    }
    
    
    @Test
    public void whenOperationIsInsert_AndIsNotAllowedType_thenNotExecuteAnyOperation() throws TripleStoreException {
        ManagementBusEvent insertCvnEvent = givenAManagementBusEvent(Operation.INSERT);        
        insertCvnEvent.setClassName("Not allowed type");
        
        wikibaseStorageService.process(insertCvnEvent);
        
        verifyNoMoreInteractions(wikibaseOperations);
    }
    
    @Test
    public void whenOperationIsInsert_thenInsertOnWikibase() throws TripleStoreException {
        ManagementBusEvent insertEvent = givenAManagementBusEvent(Operation.INSERT);        
        insertEvent.setModel(this.givenAProyectXmlRdf());
        Mockito.when(wikibaseUtils.createMonolingualTextValue(Mockito.anyString())).thenReturn(new MonolingualTextValueImpl("dummy label", "es"));
        
        wikibaseStorageService.process(insertEvent);
        
        verify(wikibaseOperations).insert(Mockito.any());
    }
    
    
    private ManagementBusEvent givenAManagementBusEvent(Operation operation) {
        ManagementBusEvent managementBusEvent = new ManagementBusEvent();
        managementBusEvent.setOperation(operation);
        managementBusEvent.setClassName("Universidad");
        return managementBusEvent;
    }
    
    private String givenAProyectXmlRdf() {
        return "<rdf:RDF    xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"    xmlns:j.0=\"http://hercules.org/um/es-ES/rec/Proyecto/\">  <rdf:Description rdf:about=\"http://hercules.org/um/es-ES/rec/Proyecto/00001\">    <rdf:type rdf:resource=\"http://hercules.org/um/es-ES/rec/Proyecto/\"/>    <j.0:grupoInvestigacion rdf:resource=\"http://hercules.org/um/es-ES/rec/GrupoInvestigacion/0001\"/>    <j.0:nombre>PROJECT 1</j.0:nombre>    <j.0:id>00001</j.0:id>  </rdf:Description></rdf:RDF>";
    }
    
}
