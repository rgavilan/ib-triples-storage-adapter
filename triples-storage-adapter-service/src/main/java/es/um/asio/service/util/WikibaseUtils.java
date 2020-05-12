package es.um.asio.service.util;

import org.apache.commons.lang3.StringUtils;
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
    
    
    /**
     * Sanitize property value.
     *
     * @param value the value
     * @return the string
     */
    public String sanitizePropertyValue(String value) {
        String propertyValue = value;
        propertyValue = propertyValue.replaceAll("(\\r|\\n)", "");
        propertyValue = StringUtils.left(propertyValue, 400); //Property must be 400 characters maximum
        propertyValue = propertyValue.trim();
        if(StringUtils.isEmpty(propertyValue)) {
            propertyValue = "."; //Property must contain at least one character
        }
        return propertyValue;
    }
}
