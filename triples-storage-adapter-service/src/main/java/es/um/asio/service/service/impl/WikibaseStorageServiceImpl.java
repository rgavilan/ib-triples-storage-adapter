package es.um.asio.service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.repository.WikibaseStorageRepository;
import es.um.asio.service.service.WikibaseStorageService;

public class WikibaseStorageServiceImpl implements WikibaseStorageService {
	
	private final Logger logger = LoggerFactory.getLogger(WikibaseStorageServiceImpl.class);
	
	@Autowired
	private WikibaseStorageRepository wikibaseStorageRepository;
	
	@Override
	public void save(String message) throws TripleStoreException {
		if (this.logger.isDebugEnabled()) {
            this.logger.debug("Insert new message: {}", message);
        }

        final JsonObject jMessage = JsonParser.parseString(message).getAsJsonObject();

        final String idMessage = jMessage.get("id").getAsString();
        final String type = jMessage.get("type").getAsString();
        final JsonObject jData = jMessage.get("data").getAsJsonObject();

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("recived TYPE: [{}]\tID: [{}]\tCONTENT: {}", type, idMessage, jData.toString());
            this.logger.debug("Ready to update wikidata");
        }

        this.wikibaseStorageRepository.save(jData);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Wikidata updated");
        }
	}

}
