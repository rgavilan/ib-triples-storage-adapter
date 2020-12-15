package es.um.asio.back.test.runners.stepdefs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.domain.Operation;
import es.um.asio.back.controller.message.MessageController;
import es.um.asio.delta.proxy.DeltaProxy;
import es.um.asio.delta.proxy.impl.DeltaProxyImpl;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.proxy.TriplesStorageProxy;
import es.um.asio.service.proxy.impl.TriplesStorageProxyImpl;
import es.um.asio.service.service.TriplesStorageService;
import es.um.asio.service.service.impl.TrellisStorageServiceImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = MessageController.class)
public class TripleStorageStepDefinitionsTest {

	/**
	 * Proxy service for triples. Performs DTO conversion and permission checks.
	 */
	@MockBean
	@Autowired
	private TriplesStorageProxy proxy;

	@MockBean
	@Autowired
	private DeltaProxy deltaProxy;

	@MockBean
	private TriplesStorageService service;

	ManagementBusEvent message;

	String currentVersion;

	String targetVersion;

	@TestConfiguration
	static class TripleStorageStepDefinitionsConfiguration {
		@Bean
		public TriplesStorageProxy triplesStorageProxy() {
			return new TriplesStorageProxyImpl();
		}

		@Bean
		public TriplesStorageService triplesStorageService() {
			return new TrellisStorageServiceImpl();
		}

		@Bean
		public DeltaProxy deltaProxy() {
			return new DeltaProxyImpl();
		}
	}

	@Given("^message controller recives a message to insert$")
	public void message_controller_recives_a_messages_to_insert() throws Exception {

		message = new ManagementBusEvent("111", "researcher",
				"data\\\":{\\\"Label_es\\\":\\\"Investigador 9\\\",\\\"Description_es\\\":\\\"Descripción Investigador 9\\\",\\\"Label_en\\\":\\\"Researcher 9\\\",\\\"Description_en\\\":\\\"Description Researcher 9\\\",\\\"Instance of:P9:wikibase-item\\\":\\\"I:Q17\\\",\\\"Instance of:P13:string\\\":\\\"01-12-1988\\\",\\\"Dirección:P10:string\\\":\\\"Direccion 9\\\",\\\"Instance of:P14:wikibase-item\\\":\\\"I:Q29\\\",\\\"Titled in:P11:wikibase-item\\\":\\\"es:Titlulo 2\\\",\\\"Random:P15:string\\\":98}}\"",
				"Researcher", Operation.INSERT);
	}

	@Then("^service process message and insert on trellis$")
	public void service_process_message_and_insert_on_trellis() throws TripleStoreException {
		this.proxy.process(message);
	}

	@Given("^delta controller recives versions to process$")
	public void delta_controller_recives_versions_to_process() throws Exception {

		currentVersion = "001";
		targetVersion = "001";
	}

	@Then("^delta service recive versions and process file data$")
	public void delta_service_recive_versions_and_process_file_data() throws Exception {
		this.deltaProxy.process(currentVersion, targetVersion);
	}

}
