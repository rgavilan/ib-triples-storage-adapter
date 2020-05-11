package es.um.asio.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wikidata.wdtk.datamodel.implementation.MonolingualTextValueImpl;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;

/**
 * The Class WikibaseUtils.
 */
@Component
public class WikibaseUtils {

    /**
     *  The default language. 
    */
    @Value("${app.wikibase.query.default-language}")
    private String defaultLanguage;
    
    /**
     * Creates the monolingual text value.
     *
     * @param text the text
     * @return the monolingual text value
     */
    public MonolingualTextValue createMonolingualTextValue(String text) {
        return new MonolingualTextValueImpl(text, defaultLanguage);
    }
}
