package es.um.asio.service.service.uris.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.atlas.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import es.um.asio.abstractions.constants.Constants;
import es.um.asio.abstractions.storage.StorageType;
import es.um.asio.service.service.uris.UrisFactoryClient;

@Service
public class UrisFactoryClientImpl implements UrisFactoryClient {
	
	
	private final Logger logger = LoggerFactory.getLogger(UrisFactoryClientImpl.class);
	
	
	
	/** The local resource storage uri. */
	@Value("${app.generator-uris.endpoint-local-resource-storage-uri}")
	private String localResourceStorageUri;
	
	/** The local property storage uri. */
	@Value("${app.generator-uris.endpoint-local-property-storage-uri}")
	private String localPropertyStorageUri;
	
	/** The uri property. */
	@Value("${app.generator-uris.endpoint-property}")
	private String uriProperty;
	
	/** Rest Template. */
	@Autowired
	private RestTemplate restUrisTemplate;

	@Bean
	public RestTemplate restUrisTemplate() {
		return new RestTemplate();
	}

	
	/**
	 * Gets the local storage uri by resource.
	 *
	 * @param id the id
	 * @param className the class name
	 * @return the local storage uri by resource
	 */
	public String getLocalStorageUriByResource(String id, String className) {
		return this.getUriByResource(id, className, Constants.LOCAL_URI);
	}
	
	/**
	 * Gets the canonical uri by resource.
	 *
	 * @param id the id
	 * @param className the class name
	 * @return the canonical uri by resource
	 */
	public String getCanonicalUriByResource(String id, String className) {
		return this.getUriByResource(id, className, "canonicalURILanguageStr");
	}
	
	/**
	 * Gets the uri by resource.
	 *
	 * @param id the id
	 * @param className the class name
	 * @param typeURI the type URI
	 * @return the uri by resource
	 */
	private String getUriByResource(String id, String className, String typeURI) {
		String result = StringUtils.EMPTY;
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(localResourceStorageUri)
					.queryParam(Constants.DOMAIN, Constants.DOMAIN_VALUE)
					.queryParam(Constants.SUBDOMAIN, Constants.SUBDOMAIN_VALUE)
					.queryParam(Constants.LANG, Constants.SPANISH_LANGUAGE)
					.queryParam(Constants.TYPE_CODE, Constants.TYPE_REST).queryParam("entity", className)
					.queryParam(Constants.REFERENCE, id)
					.queryParam(Constants.STORAGE_NAME, StorageType.TRELLIS.name().toLowerCase());

			Map response = restUrisTemplate.getForObject(builder.toUriString(), Map.class);
			ArrayList urisMap = (ArrayList) response.get(Constants.LOCAL_URIS);
			result = (String) ((LinkedHashMap<String, Object>) urisMap.get(0)).get(typeURI);
		} catch (RestClientException e) {
			logger.error("Error retrieving getUriByResource(id={}, class={}) ", id, className);
			e.printStackTrace();
		}

		logger.info("LocalStorageUri(id={}, className={}) = {}", id, className, result);

		return result;
	}


	/**
	 * Creates the property.
	 *
	 * @param fieldName the field name
	 * @return the string
	 */
	@Override
	public String createProperty(String fieldName) {
		
		String result = StringUtils.EMPTY;
		
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uriProperty)
					.queryParam(Constants.DOMAIN, Constants.DOMAIN_VALUE)
					.queryParam(Constants.SUBDOMAIN, Constants.SUBDOMAIN_VALUE)
					.queryParam(Constants.LANG, Constants.SPANISH_LANGUAGE);
			
			Map<String, String> obj = new HashMap<String, String>();
			obj.put("property", fieldName);	
			
			Map response = restUrisTemplate.postForObject(builder.toUriString(), obj, Map.class);			
			result = (String) response.get(Constants.CANONICAL_URI);
			
		} catch (RestClientException e) {
			logger.error("Error creating property {} cause: {} ", fieldName, e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}
}
