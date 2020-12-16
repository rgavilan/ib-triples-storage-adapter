package es.um.asio.service.test.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.delta.service.interpreter.RenamePropertyServiceInterpreter;
import es.um.asio.delta.service.interpreter.impl.RenamePropertyServiceInterpreterImpl;

@RunWith(SpringRunner.class)
public class RenamePropertyServiceInterpreterTest {

	@Autowired
	private RenamePropertyServiceInterpreter service;

	@TestConfiguration
	static class RenamePropertyServiceInterpreterConfiguration {
		@Bean
		public RenamePropertyServiceInterpreter renamePropertyServiceInterpreter() {
			return new RenamePropertyServiceInterpreterImpl();
		}
	}

	@Test
	public void test_structure() {
		assertNotNull(service);
	}

}
