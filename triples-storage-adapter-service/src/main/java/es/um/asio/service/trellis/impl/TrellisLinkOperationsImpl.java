package es.um.asio.service.trellis.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
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
import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.abstractions.storage.StorageType;
import es.um.asio.service.trellis.TrellisLinkOperations;

@Service
public class TrellisLinkOperationsImpl implements TrellisLinkOperations {
	
	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(TrellisLinkOperationsImpl.class);
	
	/** The local storage uri. */
	@Value("${app.generator-uris.endpoint-local-storage-uri}")
	private String localStorageUri;
	
	/** Rest Template. */
    @Autowired
    private RestTemplate restLinkTemplate;

    @Bean
    public RestTemplate restLinkTemplate() {
        return new RestTemplate();
    }
  
	/**
	 * Safety check.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	private String safetyCheck(Object obj) {
		String result = StringUtils.EMPTY;
		if( obj == null) {
			return result;
		}
		if (obj instanceof Number) {
			return ((Number) obj).toString();
		}
		if(obj instanceof String) {
			return (String) obj;
		}
		
		return result;
	}
	
	/**
	 * Gets the local storage uri.
	 *
	 * @param id the id
	 * @param className the class name
	 * @return the local storage uri
	 */
	public String getLocalStorageUri(String id, String className) {
		
		String result = StringUtils.EMPTY;
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(localStorageUri)
			        .queryParam(Constants.DOMAIN, Constants.DOMAIN_VALUE)
			        .queryParam(Constants.SUBDOMAIN, Constants.SUBDOMAIN_VALUE)
			        .queryParam(Constants.LANG, Constants.SPANISH_LANGUAGE)
			        .queryParam(Constants.TYPE_CODE, Constants.TYPE_REST)
			        .queryParam("entity", className)
			        .queryParam(Constants.REFERENCE, id)
			        .queryParam(Constants.STORAGE_NAME, StorageType.TRELLIS.name().toLowerCase());
			
			Map response = restLinkTemplate.getForObject(builder.toUriString(), Map.class);		
			ArrayList urisMap =  (ArrayList) response.get(Constants.LOCAL_URIS);
			result = (String) ((LinkedHashMap<String,Object>)urisMap.get(0)).get(Constants.LOCAL_URI);
		} catch (RestClientException e) {
			logger.error("Error retrieving getLocalStorageUri(id={}, class={}) ",id, className);
			e.printStackTrace();
		}
		
		logger.info("LocalStorageUri(id={}, className={}) = {}",id,className,result);
		
		return result;
	}
	
	
	@Override
	public Model createLinksEntry(ManagementBusEvent message) {
		
		Object obj = message.getLinkedModel();
		try {
			final String className = (String) PropertyUtils.getProperty(obj, "@class");
			final String objectId = this.safetyCheck(PropertyUtils.getProperty(obj, "id"));
			
			String localStorageUri = this.getLocalStorageUri(objectId, className);
			
		} catch (Exception e) {
			logger.error("Error retrieving class and id properties cause " + e.getMessage());
			e.printStackTrace();
		} 
		
		
		return null;
	}

	
}
