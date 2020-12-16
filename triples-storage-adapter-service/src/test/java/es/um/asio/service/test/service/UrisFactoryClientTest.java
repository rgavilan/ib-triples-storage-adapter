package es.um.asio.service.test.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import es.um.asio.service.service.uris.UrisFactoryClient;
import es.um.asio.service.service.uris.impl.UrisFactoryClientImpl;
import es.um.asio.service.trellis.util.TrellisCache;

@RunWith(SpringRunner.class)
public class UrisFactoryClientTest {

	@Autowired
	UrisFactoryClient uris;

	@MockBean
	RestTemplate restTemplate;

	@MockBean
	TrellisCache trellis;

	@TestConfiguration
	static class UrisFactoryClientTestConfiguration {
		@Bean
		public UrisFactoryClient urisFactoryClient() {
			return new UrisFactoryClientImpl();
		}
	}

	@Before
	public void before() {
		ReflectionTestUtils.setField(uris, "localResourceStorageUri", "uri");
		ReflectionTestUtils.setField(uris, "localPropertyStorageUri", "uri");
		ReflectionTestUtils.setField(uris, "uriProperty", "uri");
		ReflectionTestUtils.setField(uris, "uriFactoryEndpoint", "uri");
	}

	@Test
	public void test_Uris() {
		assertNotNull(uris);
	}
}
