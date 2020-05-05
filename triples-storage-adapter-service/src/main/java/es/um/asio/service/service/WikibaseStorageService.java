package es.um.asio.service.service;

import es.um.asio.service.exception.TripleStoreException;

public interface WikibaseStorageService {
	/**
     * Save a message into the system
     *
     * @param message
     *            The message
     * @throws TripleStoreException
     *             in case of error.
     */
    void save(final String message) throws TripleStoreException;
}
