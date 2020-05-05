package es.um.asio.service.wikibase;

import java.util.List;
import java.util.Map;

import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.Value;

import es.um.asio.service.exception.TripleStoreException;

public interface WikibaseOperations {
    /**
     * Gets a document by Id.
     *
     * @param id
     *            Document identifier
     * @return {@link EntityDocument}
     * @throws TripleStoreException
     *             in case of error
     */
    EntityDocument getById(String id) throws TripleStoreException;

    /**
     * Search documents containing the search string. Default language is used.
     *
     * @param searchString
     *            Search string.
     * @return List of documents
     * @throws TripleStoreException
     *             in case of error.
     */
    List<EntityDocument> search(String searchString) throws TripleStoreException;

    /**
     * Search documents containing the search string.
     *
     * @param searchString
     *            Search string.
     * @param language
     *            Language. If {@code null}, default language is used.
     * @return List of documents
     * @throws TripleStoreException
     *             in case of error.
     */
    List<EntityDocument> search(String searchString, String language) throws TripleStoreException;

    /**
     * Search the first document containing the search string. Default language is used.
     *
     * @param searchString
     *            Search string.
     * @return First document
     * @throws TripleStoreException
     *             in case of error.
     */
    EntityDocument searchFirst(String searchString) throws TripleStoreException;

    /**
     * Search the first document containing the search string.
     *
     * @param searchString
     *            Search string.
     * @param language
     *            Language. If {@code null}, default language is used.
     * @return First document
     * @throws TripleStoreException
     *             in case of error.
     */
    EntityDocument searchFirst(String searchString, String language) throws TripleStoreException;

    /**
     * Performs an upsert operation for the imput data.
     *
     * @param item
     *            Item to save
     * @param labels
     *            Map of labels
     * @param descriptions
     *            Map of descriptions
     * @param aliases
     *            Map of aliases
     * @return {@link ItemDocument}
     * @throws TripleStoreException
     *             in case of error.
     */
    ItemDocument save(ItemDocument item, Map<String, String> labels, Map<String, String> descriptions,
            Map<String, List<String>> aliases) throws TripleStoreException;

    /**
     * Performs an upsert operation for the imput data. Default language is used.
     *
     * @param searchString
     *            Search string
     * @param labels
     *            Map of labels
     * @param descriptions
     *            Map of descriptions
     * @param aliases
     *            Map of aliases
     * @return {@link ItemDocument}
     * @throws TripleStoreException
     *             in case of error.
     */
    ItemDocument save(String searchString, Map<String, String> labels, Map<String, String> descriptions,
            Map<String, List<String>> aliases) throws TripleStoreException;

    /**
     * Performs an upsert operation for the imput data.
     *
     * @param searchString
     *            Search string
     * @param language
     *            Language. If {@code null}, default language is used.
     * @param labels
     *            Map of labels
     * @param descriptions
     *            Map of descriptions
     * @param aliases
     *            Map of aliases
     * @return {@link ItemDocument}
     * @throws TripleStoreException
     *             in case of error.
     */
    ItemDocument save(String searchString, String language, Map<String, String> labels,
            Map<String, String> descriptions, Map<String, List<String>> aliases) throws TripleStoreException;

    /**
     * Performs an upsert operation for the imput data.
     *
     * @param property
     *            Property to save
     * @param labels
     *            Map of labels
     * @param descriptions
     *            Map of descriptions
     * @param aliases
     *            Map of aliases
     * @param dataType
     *            Data type.
     * @return {@link PropertyDocument}
     * @throws TripleStoreException
     *             in case of error.
     */
    PropertyDocument saveProperty(PropertyDocument property, Map<String, String> labels,
            Map<String, String> descriptions, Map<String, List<String>> aliases, String dataType)
            throws TripleStoreException;

    /**
     * Performs an upsert operation for the imput data. Default language is used.
     *
     * @param searchString
     *            Search string
     * @param labels
     *            Map of labels
     * @param descriptions
     *            Map of descriptions
     * @param aliases
     *            Map of aliases
     * @param dataType
     *            Data type.
     * @return {@link PropertyDocument}
     * @throws TripleStoreException
     *             in case of error.
     */
    PropertyDocument saveProperty(String searchString, Map<String, String> labels, Map<String, String> descriptions,
            Map<String, List<String>> aliases, String dataType) throws TripleStoreException;

    /**
     * Performs an upsert operation for the imput data.
     *
     * @param searchString
     *            Search string
     * @param language
     *            Language. If {@code null}, default language is used.
     * @param labels
     *            Map of labels
     * @param descriptions
     *            Map of descriptions
     * @param aliases
     *            Map of aliases
     * @param dataType
     *            Data type.
     * @return {@link PropertyDocument}
     * @throws TripleStoreException
     *             in case of error.
     */
    PropertyDocument saveProperty(String searchString, String language, Map<String, String> labels,
            Map<String, String> descriptions, Map<String, List<String>> aliases, String dataType)
            throws TripleStoreException;

    /**
     * Update a statement in an item.
     *
     * @param item
     *            Item
     * @param statement
     *            Statement
     * @return {@link ItemDocument}.
     * @throws TripleStoreException
     *             in case of error.
     */
    ItemDocument updateStatement(ItemDocument item, Statement statement) throws TripleStoreException;

    /**
     * Generate a statement.
     *
     * @param item
     *            Item
     * @param property
     *            Property
     * @param value
     *            Value
     * @return Statement
     * @throws TripleStoreException
     *             in case of error
     */
    Statement generateStatement(ItemDocument item, PropertyDocument property, Value value) throws TripleStoreException;

    /**
     * Generate a statement.
     * 
     * @param itemId
     *            Item ID
     * @param propId
     *            Property ID
     * @param value
     *            Value
     * @return Statement
     * @throws TripleStoreException
     *             in case of error
     */
    Statement generateStatement(ItemIdValue itemId, PropertyIdValue propId, Value value) throws TripleStoreException;
}
