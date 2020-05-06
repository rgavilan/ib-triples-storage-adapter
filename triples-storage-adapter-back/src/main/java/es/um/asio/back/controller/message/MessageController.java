package es.um.asio.back.controller.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.proxy.TriplesStorageProxy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Message controller.
 */
@RestController
@RequestMapping(MessageController.Mappings.BASE)
public class MessageController {

private final Logger logger = LoggerFactory.getLogger(MessageController.class);
    /**
     * Proxy service to handle message entity related operations
     */
    @Autowired
    private TriplesStorageProxy proxy;
    
    @PostMapping
    public void processMessage(@RequestBody @Validated(ManagementBusEvent.class) final ManagementBusEvent message) {
        try {
			this.proxy.process(message);
		} catch (Exception e) {
			logger.error("Error processing message: " + message.toString() + " cause: " + e.getMessage());
			e.printStackTrace();
		}        
    }

    /**
     * Mappgins.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Mappings {
        /**
         * Controller request mapping.
         */
        protected static final String BASE = "/message";

    }
}
