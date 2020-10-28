package es.um.asio.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.BasicApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;

import es.um.asio.service.config.properties.WikibaseProperties;

/**
 * Wikibase related configuration
 */
@Configuration
@ConditionalOnProperty(prefix = "app.wikibase", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(WikibaseProperties.class)
public class WikibaseConfig {

    /**
     * Wikibase related configuration properties.
     */
    @Autowired
    private WikibaseProperties properties;

    /**
     * Wikibase API connection.
     * 
     * @return {@link ApiConnection}.
     * @throws LoginFailedException
     *             in case of failing login.
     */
    @Bean
    public ApiConnection wikibaseApiConnection() throws LoginFailedException {
        WebResourceFetcherImpl.setUserAgent(this.getClass().getName());

        final BasicApiConnection connection = new BasicApiConnection(this.properties.getApi().getUrl());

        connection.login(this.properties.getApi().getUsername(), this.properties.getApi().getPassword());

        return connection;
    }

    /**
     * Wikibase data fetcher.
     * 
     * @param connection
     *            {@link ApiConnection}
     * @return {@link WikibaseDataFetcher}
     */
    @Bean
    public WikibaseDataFetcher wikibaseDataFetcher(final ApiConnection connection) {
        return new WikibaseDataFetcher(connection, this.properties.getSite().getUri());
    }

    /**
     * Wikibase data editor.
     * 
     * @param connection
     *            {@link ApiConnection}
     * @return {@link WikibaseDataEditor}
     */
    @Bean
    public WikibaseDataEditor wikibaseDataEditor(final ApiConnection connection) {
        return new WikibaseDataEditor(connection, this.properties.getSite().getUri());
    }
}
