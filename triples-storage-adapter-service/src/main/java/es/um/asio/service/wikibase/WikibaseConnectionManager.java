package es.um.asio.service.wikibase;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;

import lombok.Getter;

/**
 * Wikibase connection manager.
 */
@ConditionalOnProperty(prefix = "app.wikibase", name = "enabled", havingValue = "true", matchIfMissing = false)
@Component
public class WikibaseConnectionManager {
    
    /**
     * Wikibase API connection
     */
    @Autowired
    private ApiConnection connection;

    /**
     * Wikibase data fetcher.
     */
    @Getter
    @Autowired
    private WikibaseDataFetcher dataFetcher;

    /**
     * Wikibase data editor.
     */
    @Getter
    @Autowired
    private WikibaseDataEditor dataEditor;

    /**
     * Gets {@link WikibaseDataFetcher} for site URI.
     *
     * @param siteUri
     *            Site URI.
     * @return {@link WikibaseDataFetcher}.
     */
    public WikibaseDataFetcher getDataFetcher(final String siteUri) {
        WikibaseDataFetcher intDataFetcher;

        if (StringUtils.isBlank(siteUri)) {
            intDataFetcher = this.getDataFetcher();
        } else {
            intDataFetcher = new WikibaseDataFetcher(this.connection, siteUri);
        }
        return intDataFetcher;
    }

    /**
     * Gets {@link WikibaseDataEditor} for site URI.
     *
     * @param siteUri
     *            Site URI.
     * @return {@link WikibaseDataEditor}.
     */
    public WikibaseDataEditor getDataEditor(final String siteUri) {
        WikibaseDataEditor intDataEditor;

        if (StringUtils.isBlank(siteUri)) {
            intDataEditor = this.getDataEditor();
        } else {
            intDataEditor = new WikibaseDataEditor(this.connection, siteUri);
        }
        return intDataEditor;
    }
}
