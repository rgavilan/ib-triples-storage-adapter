package es.um.asio.service.test.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.domain.Operation;
import es.um.asio.service.service.TriplesStorageService;
import es.um.asio.service.service.impl.TrellisStorageServiceImpl;
import es.um.asio.service.util.TrellisUtils;

@RunWith(SpringRunner.class)
public class TriplesStorageServiceTest {
    /**
     * Triples storage service.
     */
    @Autowired
    private TriplesStorageService service;
    
    @TestConfiguration
    static class UserServiceTestConfiguration {
        @Bean
        public TriplesStorageService userService() {
            return new TrellisStorageServiceImpl();
        }
        
        @Bean
        public TrellisUtils trellisUtils() {
        	return new TrellisUtils();
        }
    }
    
    @Test
    public void test_Infraestructure() {
        assertNotNull(service);
        
        ManagementBusEvent message = new ManagementBusEvent();
        
        String model = "<rdf:RDF\r\n" + "    xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\r\n"
				+ "    xmlns:j.0=\"http://example.org/\"\r\n"
				+ "    xmlns:j.1=\"http://www.w3.org/2001/asio-rdf/3.0#\">\r\n"
				+ "  <j.0:ConceptoGrupoDummy rdf:about=\"http://example.org/E0A6-01/5/DESCRIPTORES/LENGUAJES+PLASTICOS\">\r\n"
				+ "    <j.1:texto>LENGUAJES PLASTICOS</j.1:texto>\r\n"
				+ "    <j.1:codTipoConcepto>DESCRIPTORES</j.1:codTipoConcepto>\r\n"
				+ "    <j.1:numero>5</j.1:numero>\r\n"
				+ "    <j.1:idGrupoInvestigacion>E0A6-01</j.1:idGrupoInvestigacion>\r\n"
				+ "  </j.0:ConceptoGrupoDummy>\r\n" + "</rdf:RDF>";
        
        message.setModel(model);
        message.setClassName("ConceptoGrupoDummy");
        // message.setIdModel("07dcf82c-43ec-40b5-aa21-e41801d3b582");
        message.setIdModel("10886753");
        message.setOperation(Operation.INSERT);
        service.process(message);
    }
}
