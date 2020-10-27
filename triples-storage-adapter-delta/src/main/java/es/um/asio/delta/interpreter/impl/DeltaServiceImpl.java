package es.um.asio.delta.interpreter.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.delta.interpreter.DeltaService;

/**
 * Triples service implementation for Trellis.
 */
@ConditionalOnProperty(prefix = "app.trellis", name = "enabled", havingValue = "true", matchIfMissing = true)
@Service
public class DeltaServiceImpl implements DeltaService {

	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(DeltaServiceImpl.class);
	

		
		
	/**
	 * Process.
	 *
	 * @param message the message
	 */
	@Override
	public void process(ManagementBusEvent message) {
		switch (message.getOperation()) {
		case LINKED_INSERT:
			this.saveLinks(message);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Safety check.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	private String safetyCheck(Object obj) {
		String result = StringUtils.EMPTY;
		if (obj == null) {
			return result;
		}
		if (obj instanceof Number) {
			return ((Number) obj).toString();
		}
		if (obj instanceof String) {
			return (String) obj;
		}

		return result;
	}
	

	
	
	/**
	 * Save links.
	 *
	 * @param message the message
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void saveLinks(ManagementBusEvent message) {
		logger.info("Saving links in trellis: {}", message.getClassName());
			
	}
}
