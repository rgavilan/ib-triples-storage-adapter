package es.um.asio.service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.repository.WikibaseStorageRepository;
import es.um.asio.service.service.TriplesStorageService;

@ConditionalOnProperty(prefix = "app.wikibase", name = "enabled", havingValue = "true", matchIfMissing = true)
@Service
public class WikibaseStorageServiceImpl implements TriplesStorageService {
	
	private final Logger logger = LoggerFactory.getLogger(WikibaseStorageServiceImpl.class);
	
	@Autowired
	private WikibaseStorageRepository wikibaseStorageRepository;
	
	@Override
	public void process(ManagementBusEvent message) throws TripleStoreException {
		if (this.logger.isDebugEnabled()) {
            this.logger.debug("Insert new message: {}", message);
        }
/*
        final JsonObject jMessage = JsonParser.parseString(message).getAsJsonObject();

        final String idMessage = jMessage.get("id").getAsString();
        final String type = jMessage.get("type").getAsString();
        final JsonObject jData = jMessage.get("data").getAsJsonObject();

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("recived TYPE: [{}]\tID: [{}]\tCONTENT: {}", type, idMessage, jData.toString());
            this.logger.debug("Ready to update wikidata");
        }

        this.wikibaseStorageRepository.save(jData);
*/
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Wikidata updated");
        }
	}

}
