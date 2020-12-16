package es.um.asio.service.test.trellis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.domain.Operation;
import es.um.asio.service.service.discovery.DiscoveryClient;
import es.um.asio.service.service.uris.UrisFactoryClient;
import es.um.asio.service.trellis.TrellisCommonOperations;
import es.um.asio.service.trellis.TrellisLinkOperations;
import es.um.asio.service.trellis.impl.TrellisCommonOperationsImpl;
import es.um.asio.service.trellis.impl.TrellisLinkOperationsImpl;
import es.um.asio.service.trellis.util.TrellisCache;
import es.um.asio.service.trellis.util.TrellisCacheImpl;
import es.um.asio.service.util.TriplesStorageUtils;

@RunWith(SpringRunner.class)
public class TrellisLinkOperationsTest {
    
    /**
     * Trellis operations
     */
    @Autowired
    private TrellisLinkOperations trellisLinkOperations;
    
    @MockBean
    private UrisFactoryClient urisFactoryClient;
    
    @MockBean
    private DiscoveryClient discoveryClient;
    
    @TestConfiguration
    static class TrellisStorageServiceTestConfiguration {
        @Bean
        public TrellisLinkOperations trellisLinkOperations() {
            return new TrellisLinkOperationsImpl();
        }
        
        @Bean
        public TriplesStorageUtils triplesStorageUtils() {
            return new TriplesStorageUtils();
        }
        
        @Bean
        public TrellisCommonOperations trellisCommonOperations() {
            return new TrellisCommonOperationsImpl();
        }
        
        @Bean
        public TrellisCache trellisCache() {
            return new TrellisCacheImpl();
        }
    }
    
    @Before
    public void setUp() {
        Mockito.when(this.urisFactoryClient.getLocalStorageUriByEntityId(any(), any())).thenAnswer(invocation -> {
           return "http://mockuri/" + invocation.getArgument(0); 
        });
    }
    
    @Test
    public void whenRetrieveObjectFromTellis_thenReturnNull() {
        Model model = this.trellisLinkOperations.retrieveObjectFromTellis("1", "foo.bar");
        assertThat(model).isNull();
    }
}
