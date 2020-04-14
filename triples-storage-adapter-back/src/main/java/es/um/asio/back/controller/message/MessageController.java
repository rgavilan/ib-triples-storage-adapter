package es.um.asio.back.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.proxy.TriplesStorageProxy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Message controller.
 */
@RestController
@RequestMapping(MessageController.Mappings.BASE)
public class MessageController {

    /**
     * Proxy service to handle message entity related operations
     */
    @Autowired
    private TriplesStorageProxy proxy;

    
    // @Secured(Role.ADMINISTRATOR_ROLE)       throws TripleStoreException
    @PostMapping
    public void save(@RequestBody @Validated(ManagementBusEvent.class) final ManagementBusEvent message) {
        System.out.println("Yessss");
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
