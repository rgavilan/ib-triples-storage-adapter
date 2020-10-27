package es.um.asio.service.config.properties;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * Wikibase query related configuration properties.
 */
@Getter
@Setter
@Validated
public class QueryProperties {

    /**
     * Default query language
     */
    @NotNull
    private String defaultLanguage = "es";
}
