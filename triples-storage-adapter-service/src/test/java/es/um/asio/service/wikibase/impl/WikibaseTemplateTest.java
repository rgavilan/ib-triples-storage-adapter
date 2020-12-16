package es.um.asio.service.wikibase.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;


import es.um.asio.service.wikibase.WikibaseConnectionManager;
import es.um.asio.service.wikibase.WikibaseOperations;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {WikibaseOperations.class, WikibaseTemplateTest.WikibaseTemplateTestConfig.class})
@ActiveProfiles("unit-test")
public class WikibaseTemplateTest {

	@Autowired
	private WikibaseOperations wikibaseTemplate;

	@MockBean
	private WikibaseConnectionManager connectionManager;

	/**
	 * Wikibase data fetcher.
	 */
	@MockBean
	private WikibaseDataFetcher dataFetcher;

	/**
	 * Wikibase data editor.
	 */
	@MockBean
	private WikibaseDataEditor dataEditor;

	@TestConfiguration
	static class WikibaseTemplateTestConfig {
		@Bean
		WikibaseOperations wikibaseTemplate() {
			return new WikibaseTemplate();
		}
	}

	@Test
	public void getByIdEmpty() {
		assertThrows(IllegalArgumentException.class, () -> {
			wikibaseTemplate.getById(null);
		});

	}
	
}
