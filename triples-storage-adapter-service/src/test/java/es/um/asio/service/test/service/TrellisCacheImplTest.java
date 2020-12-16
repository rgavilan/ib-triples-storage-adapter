package es.um.asio.service.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.service.trellis.util.TrellisCache;
import es.um.asio.service.trellis.util.TrellisCacheImpl;

@RunWith(SpringRunner.class)
public class TrellisCacheImplTest {

	@Autowired
	TrellisCache cache;

	@TestConfiguration
	static class TrellisCacheTestConfiguration {
		@Bean
		public TrellisCache trellisCache() {
			return new TrellisCacheImpl();
		}
	}

	@Test
	public void test_cacheFindTrellis() {
		this.cache.find("test", "testCache");

	}

	@Test
	public void test_cacheSaveTrellis() {

		this.cache.saveInCache("test", "1", "testCache");

	}
}
