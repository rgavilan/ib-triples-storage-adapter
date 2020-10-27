package es.um.asio.back.controller.delta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.um.asio.delta.proxy.DeltaProxy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping(DeltaController.Mappings.BASE)
public class DeltaController {

	
	private final Logger logger = LoggerFactory.getLogger(DeltaController.class);
	
	@Autowired
	private DeltaProxy deltaProxy;
	
	@GetMapping(value = "/{currentVersion}/{targetVersion}")
	public void run(@PathVariable("currentVersion") final String currentVersion,@PathVariable("targetVersion") final String targetVersion) {
		logger.info("Running DeltaController [currentVersion={} , targetVersion={}]", currentVersion, targetVersion);
	}
	
	/**
     * Mappgins.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Mappings {
        /**
         * Controller request mapping.
         */
        protected static final String BASE = "/delta";

    }
}
