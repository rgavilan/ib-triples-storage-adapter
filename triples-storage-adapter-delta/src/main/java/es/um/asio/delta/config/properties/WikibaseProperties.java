package es.um.asio.delta.config.properties;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * Wikibase related configuration properties.
 */
@ConfigurationProperties("app.wikibase")
@Validated
@Getter
@Setter
public class WikibaseProperties {

    /**
     * API related configuration properties.
     */
    @NotNull
    @NestedConfigurationProperty
    private ApiProperties api;

    /**
     * API related configuration properties.
     */
    @NotNull
    @NestedConfigurationProperty
    private SiteProperties site;
    
    /**
     * Wikibase query related configuration properties.
     */
    @NotNull
    @NestedConfigurationProperty
    private QueryProperties query;

}
