package es.um.asio.back.test.controller.message;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.um.asio.back.controller.message.MessageController;
import es.um.asio.service.proxy.TriplesStorageProxy;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    /**
     * MVC test support
     */
    @Autowired
    private MockMvc mvc;

    /**
     * Proxy service for triples. Performs DTO conversion and permission checks.
     */
    @MockBean
    private TriplesStorageProxy proxy;

    
    @TestConfiguration
    static class UserProxyTestConfiguration {
        @Bean
        public MessageController userController() {
            return new MessageController();
        }
    }
    
    @Test
    public void test_Infraestructure() {
        assertNotNull(mvc);
    }
    
    @Test
    public void givenGreetURIWithPost_whenMockMVC_thenStatusIsOk() throws Exception {
        this.mvc.perform(post("/message")
                .content("{\"id\":8,\"type\":\"researcher\",\"data\":{\"Label_es\":\"Investigador 9\",\"Description_es\":\"Descripción Investigador 9\",\"Label_en\":\"Researcher 9\",\"Description_en\":\"Description Researcher 9\",\"Instance of:P9:wikibase-item\":\"I:Q17\",\"Instance of:P13:string\":\"01-12-1988\",\"Dirección:P10:string\":\"Direccion 9\",\"Instance of:P14:wikibase-item\":\"I:Q29\",\"Titled in:P11:wikibase-item\":\"es:Titlulo 2\",\"Random:P15:string\":98}}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))            
                .andExpect(status().isOk());
    }
    
    @Test
    public void whenProcessMessage_thenCallTripleStoreProcess() throws Exception {
        this.mvc.perform(post("/message")
                .content("{\"id\":8,\"type\":\"researcher\",\"data\":{\"Label_es\":\"Investigador 9\",\"Description_es\":\"Descripción Investigador 9\",\"Label_en\":\"Researcher 9\",\"Description_en\":\"Description Researcher 9\",\"Instance of:P9:wikibase-item\":\"I:Q17\",\"Instance of:P13:string\":\"01-12-1988\",\"Dirección:P10:string\":\"Direccion 9\",\"Instance of:P14:wikibase-item\":\"I:Q29\",\"Titled in:P11:wikibase-item\":\"es:Titlulo 2\",\"Random:P15:string\":98}}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))            
                .andExpect(status().isOk());
        
        verify(proxy).process(Mockito.any());
    }

}
