package es.um.asio.delta.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class ExchangeClientImpl implements ExchangeClient {

	private final Logger logger = LoggerFactory.getLogger(ExchangeClientImpl.class);

	/** The file path instructions. */
	@Value("${app.delta.mockup-file-path}")
	private String filePathInstructions;

	@Override
	public ArrayNode retrieveDeltaFile(String currentVersion, String targetVersion) {
		// TODO call API-Exchange
		return mockup();
	}

	private ArrayNode mockup() {
		ArrayNode result = null;

		ObjectMapper mapper = new ObjectMapper();

		try {
			result = mapper.readValue(new File(filePathInstructions), ArrayNode.class);
		} catch (Exception e) {
			logger.error("mockup", e);
		}
		return result;
	}

}
