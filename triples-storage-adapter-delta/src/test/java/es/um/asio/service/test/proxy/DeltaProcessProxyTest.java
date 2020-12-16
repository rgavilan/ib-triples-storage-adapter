package es.um.asio.service.test.proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.delta.processor.DeltaProcessor;
import es.um.asio.delta.proxy.DeltaProxy;
import es.um.asio.delta.proxy.impl.DeltaProxyImpl;
import es.um.asio.delta.service.ExchangeClient;

@RunWith(SpringRunner.class)
public class DeltaProcessProxyTest {

	@Autowired
	private DeltaProxy proxy;

	@MockBean
	private ExchangeClient service;

	@MockBean
	private DeltaProcessor deltaProcessor;

	@TestConfiguration
	static class ExchangeClientConfiguration {
		@Bean
		public DeltaProxy deltaProxy() {
			return new DeltaProxyImpl();
		}

	}

	@Test
	public void deltaProxyProcess() {
		this.proxy.process("001", "002");
	}
}
