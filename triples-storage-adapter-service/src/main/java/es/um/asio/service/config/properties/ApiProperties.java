package es.um.asio.service.config.properties;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * Wikibase API related configuration properties.
 */
@Getter
@Setter
@Validated
public class ApiProperties {

    /**
     * Wikibase API URL
     */
    @NotNull
    private String url;

    /**
     * Wikibase username
     */
    @NotNull
    private String username;

    /**
     * Wikibase password
     */
    @NotNull
    private String password;
}
