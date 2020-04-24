package es.um.asio.service.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.trellisldp.api.RuntimeTrellisException;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.service.TriplesStorageService;
import es.um.asio.service.util.MediaTypes;
import es.um.asio.service.util.RdfObjectMapper;
import es.um.asio.service.util.TrellisUtils;

/**
 * Triples service implementation for Trellis.
 */
@ConditionalOnProperty(prefix = "app.trellis", name = "enabled", havingValue = "true", matchIfMissing = true)
@Service
public class TrellisStorageServiceImpl implements TriplesStorageService {

	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(TrellisStorageServiceImpl.class);

	@Autowired
	private TrellisUtils trellisUtils;

	@Value("${app.trellis.endpoint}")
	private String trellisUrlEndPoint;

	/**
	 * Process.
	 *
	 * @param message the message
	 */
	@Override
	public void process(ManagementBusEvent message) {
		switch (message.getOperation()) {
		case INSERT:
			this.save(message);
			break;
		case UPDATE:
			break;
		case DELETE:
			break;
		default:
			break;
		}
	}

	
	/**
	 * Gets the Model from resourceUri.
	 *
	 * @param resourceUri the resource uri
	 * @return the model
	 */
	public Model get(String resourceUri) {
		Model model = RestAssured.given()
				.header("Accept", MediaTypes.TEXT_TURTLE)
				.expect().statusCode(HttpStatus.SC_OK)
				.when().get(resourceUri)
				.as(Model.class, new RdfObjectMapper(resourceUri));

		return model;

	}
	
	/**
	 * Save.
	 *
	 * @param message the message
	 */
	public void save(ManagementBusEvent message) {
		logger.info("Saving object in trellis : " + message.toString());

		if(StringUtils.isNoneBlank(message.getIdModel())) {
			if(!existsContainer(message)) {
				createContainer(message);
			}
			
			// we insert the entry in trellis
			createEntry(message);
		}
	}
	
	
			
	/**
	 * Exists container.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean existsContainer(ManagementBusEvent message) {
		boolean result = false;
		
		String urlContainer =  trellisUrlEndPoint + message.getClassName();
		Model model;
		try {
			model = RestAssured.given()
					.header("Accept", MediaTypes.TEXT_TURTLE)
					.expect()
					.when().get(urlContainer)
					.as(Model.class, new RdfObjectMapper(urlContainer));
					result = model.size() > 0 ? true : false;
		} catch (Exception e) {
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Creates the container.
	 *
	 * @param message the message
	 */
	public void createContainer(ManagementBusEvent message) {
		logger.info("Creating a container");
												
		Model model = ModelFactory.createDefaultModel();
		model.createProperty("http://hercules.org");
		Resource resourceProperties = model.createResource();
		Property a = model.createProperty("http://www.w3.org/ns/ldp#", "a");
		Property dcterms = model.createProperty("http://purl.org/dc/terms/", "title");
		
		resourceProperties.addProperty(a, "Container");
		resourceProperties.addProperty(a, "BasicContainer");
		resourceProperties.addProperty(dcterms, message.getClassName() + " Container");
		
		Response postResponse;
		try {
			postResponse = RestAssured.given()
					.contentType(MediaTypes.TEXT_TURTLE)
					.header("slug", message.getClassName())					
					.header("link", "<http://www.w3.org/ns/ldp#BasicContainer>; rel=\"type\"")
					.body(model, new RdfObjectMapper())
					.post(trellisUrlEndPoint);
			
			if (postResponse.getStatusCode() != HttpStatus.SC_CREATED) {
				logger.warn("The container already exists: " + postResponse.getStatusCode());
			} 
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the entry.
	 *
	 * @param message the message
	 */
	public void createEntry(ManagementBusEvent message) {
		Model model = trellisUtils.toObject(message.getModel());
		String urlContainer =  trellisUrlEndPoint + "/" + message.getClassName();
		
		Response postResponse = RestAssured.given()
				.contentType(MediaTypes.TEXT_TURTLE)
				.header("slug", message.getIdModel())
				.body(model, new RdfObjectMapper()).post(urlContainer);
		
		if (postResponse.getStatusCode() != HttpStatus.SC_CREATED) {
			logger.error("Error saving the object: " + message.getModel());
			logger.error("cause: " + postResponse.getBody().asString());
			throw new RuntimeTrellisException("Error saving in Trellis the object: " + message.getModel());
		}
	}
}
