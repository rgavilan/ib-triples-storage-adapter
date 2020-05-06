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

	void save(ManagementBusEvent message) throws TripleStoreException {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Wikidata updated");
		}

		final JsonObject jMessage = JsonParser.parseString(message.getModel()).getAsJsonObject();

		this.wikibaseStorageRepository.save(jMessage);

	}

}
