package es.um.asio.service.service.impl;

import org.apache.http.HttpStatus;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Service
//TODO @ConditionalOnProperty()
public class TrellisStorageServiceImpl implements TriplesStorageService {

	/**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(TrellisStorageServiceImpl.class);
    
    @Autowired
    private TrellisUtils trellisUtils;
    
    @Value("${app.trellis-endpoint}")
    private String trellisUrlEndPoind;
	
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
	 * Save.
	 *
	 * @param message the message
	 */
	public void save(ManagementBusEvent message) {
		logger.info(message.toString());	
		
		Model model = trellisUtils.toObject(message.getModel());
		
		Response postResponse = RestAssured.given()
				.contentType(MediaTypes.TEXT_TURTLE)
				.body(model, new RdfObjectMapper())
				.post(trellisUrlEndPoind);
		
		if (postResponse.getStatusCode() != HttpStatus.SC_CREATED) {
			logger.error("Error saving the object: " + message.getModel());
			logger.error("cause: " + postResponse.getBody().asString());
			throw new RuntimeTrellisException("Error saving in Trellis the object: " + message.getModel());
		} 
	}	
}
