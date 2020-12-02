package es.um.asio.service.service.discovery.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import es.um.asio.abstractions.constants.Constants;
import es.um.asio.abstractions.domain.Operation;
import es.um.asio.service.service.discovery.DiscoveryClient;

/**
 * The Class DiscoveryClientImpl.
 */
@ConfigurationProperties(prefix = "app.discovery")
@Service
public class DiscoveryClientImpl implements DiscoveryClient {

	// Watch out!! the getters and setters they are needed
	/** The discovery nodes. */	
	private String[] nodes;

	/** Rest Template. */
	@Autowired
	private RestTemplate restDiscoveryTemplate;

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(DiscoveryClientImpl.class);

	
	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public String[] getNodes() {
		return nodes;
	}

	/**
	 * Sets the nodes.
	 *
	 * @param nodes the new nodes
	 */
	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}

	/**
	 * Rest discovery template.
	 *
	 * @return the rest template
	 */
	@Bean
	public RestTemplate restDiscoveryTemplate() {
		return new RestTemplate();
	}

	/**
	 * Event notify discovery.
	 *
	 * @param operation             the operation
	 * @param className             the class name
	 * @param localTripleStorageUri the local triple storage uri
	 * @param subDomain             the sub domain
	 * @param tripleStorage         the triple storage
	 */
	public void eventNotifyDiscovery(final Operation operation, final String className,
			final String localTripleStorageUri, final String subDomain, final String tripleStorage) {

		UriComponentsBuilder builder = null;
		
		try {
			String response = null;
			for (final String endPoint : this.nodes) {
				
				builder = UriComponentsBuilder.fromHttpUrl(endPoint)
				        .queryParam(Constants.ACTION, operation.name())
				        .queryParam(Constants.CLASS_NAME, className)
				        .queryParam(Constants.ENTITY_LOCAL_URI, localTripleStorageUri)
				        .queryParam(Constants.NODE, subDomain)
				        .queryParam(Constants.TRIPLE_STORE, tripleStorage);
				
				response = this.restDiscoveryTemplate.postForObject(builder.toUriString(), null, String.class);
				this.logger.info("Event notification from discovery library: {}", response);
			}
		} catch (final RestClientException e) {
			this.logger.error("Error sending notification to discovery library: {}, {}, {}, {}, {} cause: {}", operation,
					className, localTripleStorageUri, subDomain, tripleStorage, e.toString());
		}
	}
}
