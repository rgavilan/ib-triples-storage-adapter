package es.um.asio.delta.service;

import com.fasterxml.jackson.databind.node.ArrayNode;

public interface ExchangeClient {
	ArrayNode retrieveDeltaFile(String currentVersion, String targetVersion);
}
