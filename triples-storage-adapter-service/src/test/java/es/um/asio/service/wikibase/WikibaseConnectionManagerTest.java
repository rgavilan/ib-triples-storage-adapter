package es.um.asio.service.wikibase;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;


@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class WikibaseConnectionManagerTest {
	
	@Autowired
	private WikibaseConnectionManager wikibaseConnectionManager;
	
	@MockBean
	private ApiConnection connection;

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
	static class WikibaseConnectionManagerTestConfig {
		@Bean
		WikibaseConnectionManager wikibaseConnectionManager() {
			return new WikibaseConnectionManager();
		}
	}

	
	@Test
	public void getDataFetcherBlank() {
		WikibaseDataFetcher result = wikibaseConnectionManager.getDataFetcher("");
		assertNotNull(result);
	}
	
	@Test
	public void getDataFetcherNotBlank() {
		String siteUri = "http://siteUri";
		WikibaseDataFetcher result = wikibaseConnectionManager.getDataFetcher(siteUri);
		assertNotNull(result);
	}
	
	@Test
	public void getDataEditorBlank() {
		WikibaseDataFetcher result = wikibaseConnectionManager.getDataFetcher("");
		assertNotNull(result);
	}
	
	@Test
	public void getDataEditorNotBlank() {
		String siteUri = "http://siteUri";
		WikibaseDataFetcher result = wikibaseConnectionManager.getDataFetcher(siteUri);
		assertNotNull(result);
	}

}
