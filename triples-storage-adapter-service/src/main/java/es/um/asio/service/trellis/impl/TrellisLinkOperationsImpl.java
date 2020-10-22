package es.um.asio.service.trellis.impl;

import java.net.URI;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFLanguages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.restassured.response.Response;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.service.uris.UrisFactoryClient;
import es.um.asio.service.trellis.TrellisCommonOperations;
import es.um.asio.service.trellis.TrellisLinkOperations;
import es.um.asio.service.util.TrellisUtils;

@Service
public class TrellisLinkOperationsImpl implements TrellisLinkOperations {

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(TrellisLinkOperationsImpl.class);

	@Autowired
	private TrellisCommonOperations trellisCommonOperations;
	
	  /** The trellis utils. */
    @Autowired
    private TrellisUtils trellisUtils;
    
    @Autowired
    private UrisFactoryClient urisFactoryClient;

	
	/**
	 * Gets the local storage uri.
	 *
	 * @param id        the id
	 * @param className the class name
	 * @return the local storage uri
	 */
	public String getLocalStorageUri(String id, String className) {
		return urisFactoryClient.getLocalStorageUriByResource(id, className);		
	}

	/**
	 * Creates the links entry.
	 *
	 * @param message the message
	 * @return the model
	 */
	@Override
	public Model createLinksEntry(String objectId, String className) {
		Model result = null;
		
		try {
			String localStorageUri = this.getLocalStorageUri(objectId, className);
			result = getObjectFromTellis(localStorageUri);

		} catch (Exception e) {
			logger.error("Error retrieving class and id properties cause " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Gets the object from tellis.
	 *
	 * @param localStorageUri the local storage uri
	 * @return the object from tellis
	 */
	private Model getObjectFromTellis(final String localStorageUri) {
		
		Model result = null;
		try {
			Response response = trellisCommonOperations.createRequestSpecification().get(new URI(localStorageUri));
			String strModel = response.getBody().asString();			
			result = trellisUtils.toObject(strModel, RDFLanguages.TURTLE);					
		} catch (Exception e) {
			this.logger.error("Invalid URI {}", localStorageUri);
			e.printStackTrace();
		}
		
		return result;
	}

}
