package es.um.asio.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class TripleStorageUtilsTest {
	
	@Autowired
	private TriplesStorageUtils tripleStorageUtils;
	
	
	@TestConfiguration
	static class TriplesStorageUtilsTestConfig {
		@Bean
		TriplesStorageUtils tripleStorageUtils() {
			return new TriplesStorageUtils();
		}
	}
	
	@Test
	public void toStringTest() {
		Model model =  ModelFactory.createDefaultModel();
		String result = tripleStorageUtils.toString(model);
		assertNotNull(result);
	}
	

}
