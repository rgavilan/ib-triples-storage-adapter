package es.um.asio.delta.config.properties;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * Wikibase Site related configuration properties.
 */
@Getter
@Setter
@Validated
public class SiteProperties {

    /**
     * Wikibase site URI
     */
    @NotNull
    private String uri;
}
