package es.um.asio.service.wikibase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.PropertyDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.implementation.MonolingualTextValueImpl;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementGroup;
import org.wikidata.wdtk.datamodel.interfaces.TermedDocument;
import org.wikidata.wdtk.datamodel.interfaces.Value;
import org.wikidata.wdtk.datamodel.interfaces.ValueSnak;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import es.um.asio.service.config.properties.WikibaseProperties;
import es.um.asio.service.exception.TripleStoreException;

/**
 * Wikibase template
 */
@Component
public class WikibaseTemplate implements WikibaseOperations {

    /**
     * Wikibase connection manager.
     */
    @Autowired
    private WikibaseConnectionManager connectionManager;

    /**
     * Wikibase data fetcher.
     */
    @Autowired
    private WikibaseDataFetcher dataFetcher;

    /**
     * Wikibase data editor.
     */
    @Autowired
    private WikibaseDataEditor dataEditor;

    /**
     * Wikibase related configuration properties.
     */
    @Autowired
    private WikibaseProperties properties;

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDocument getById(final String id) throws TripleStoreException {
        return this.getById(id, null);
    }

    public EntityDocument getById(final String id, final String siteUri) throws TripleStoreException {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("id must not be null or blank!");
        }

        EntityDocument document = null;

        try {
            document = this.connectionManager.getDataFetcher(siteUri).getEntityDocument(id);
        } catch (MediaWikiApiErrorException | IOException e) {
            throw new TripleStoreException("Error getting document", e);
        }

        return document;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EntityDocument> search(final String searchString) throws TripleStoreException {
        return this.search(searchString, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EntityDocument> search(final String searchString, final String language) throws TripleStoreException {
        if (StringUtils.isBlank(searchString)) {
            throw new IllegalArgumentException("searchString must not be null or blank!");
        }

        return this.search(searchString, language, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDocument searchFirst(final String searchString) throws TripleStoreException {
        return this.searchFirst(searchString, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDocument searchFirst(final String searchString, final String language) throws TripleStoreException {
        if (StringUtils.isBlank(searchString)) {
            throw new IllegalArgumentException("searchString must not be null or blank!");
        }

        final List<EntityDocument> documents = this.search(searchString, language, 1);
        return documents.isEmpty() ? null : documents.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemDocument save(final ItemDocument item, final Map<String, String> labels,
            final Map<String, String> descriptions, final Map<String, List<String>> aliases)
            throws TripleStoreException {

        ItemDocument newItem;

        if (this.isNew(item)) {
            // do insert
            newItem = this.create(labels, descriptions, aliases);
        } else {
            // do update
            newItem = this.update(item, labels, descriptions, aliases, null);
        }

        return newItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemDocument save(final String searchString, final Map<String, String> labels,
            final Map<String, String> descriptions, final Map<String, List<String>> aliases)
            throws TripleStoreException {
        return this.save(searchString, null, labels, descriptions, aliases);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemDocument save(final String searchString, final String language, final Map<String, String> labels,
            final Map<String, String> descriptions, final Map<String, List<String>> aliases)
            throws TripleStoreException {
        return this.save(this.getItemDocument(searchString, language), labels, descriptions, aliases);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyDocument saveProperty(final PropertyDocument property, final Map<String, String> labels,
            final Map<String, String> descriptions, final Map<String, List<String>> aliases, final String dataType)
            throws TripleStoreException {

        PropertyDocument newProperty;

        if (this.isNew(property)) {
            // do insert
            newProperty = this.createProperty(labels, descriptions, aliases, dataType);
        } else {
            // do update
            newProperty = this.updateProperty(property, labels, descriptions, aliases);
        }

        return newProperty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyDocument saveProperty(final String searchString, final Map<String, String> labels,
            final Map<String, String> descriptions, final Map<String, List<String>> aliases, final String dataType)
            throws TripleStoreException {
        return this.saveProperty(searchString, null, labels, descriptions, aliases, dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyDocument saveProperty(final String searchString, final String language,
            final Map<String, String> labels, final Map<String, String> descriptions,
            final Map<String, List<String>> aliases, final String dataType) throws TripleStoreException {
        return this.saveProperty((PropertyDocument) this.getItemDocument(searchString, language), labels, descriptions,
                aliases, dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemDocument updateStatement(final ItemDocument item, final Statement statement)
            throws TripleStoreException {
        return this.update(item, null, null, null, statement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement generateStatement(final ItemDocument item, final PropertyDocument property, final Value value)
            throws TripleStoreException {
        return this.generateStatement(item.getEntityId(), property.getEntityId(), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement generateStatement(final ItemIdValue itemId, final PropertyIdValue propId, final Value value)
            throws TripleStoreException {
        final ItemDocument item = (ItemDocument) this.getById(itemId.getId(), itemId.getSiteIri());

        Statement statement = null;
        boolean isPresent = false;
        boolean propertyExist = false;

        for (final StatementGroup sg : item.getStatementGroups()) {
            if (sg.getProperty().equals(propId)) {
                propertyExist = true;
                for (final Statement s : sg.getStatements()) {
                    statement = s;
                    final Iterator<Snak> iterator = s.getAllQualifiers();

                    while (iterator.hasNext() && !isPresent) {
                        final ValueSnak qua = (ValueSnak) iterator.next();
                        final Value innerValue = qua.getValue();
                        isPresent = value.equals(innerValue);
                    }
                }
            }
        }

        final StatementBuilder builder = StatementBuilder.forSubjectAndProperty(itemId, propId)
                .withQualifierValue(propId, value);

        if (propertyExist) {
            builder.withId(statement.getStatementId());
        }

        return builder.build();
    }

    /**
     * Search documents containing the search string.
     *
     * @param searchString
     *            Search string.
     * @param language
     *            Language. If {@code null}, default language is used.
     * @param maxElements
     *            Max number of elements. If lower or equal than 0, returns all elements.
     * @return List of documents
     * @throws TripleStoreException
     *             in case of error.
     */
    private List<EntityDocument> search(final String searchString, final String language, final int maxElements)
            throws TripleStoreException {
        final List<EntityDocument> documents = new ArrayList<>();
        int element = 0;

        try {
            final List<WbSearchEntitiesResult> results = this.dataFetcher.searchEntities(searchString,
                    StringUtils.isNotBlank(language) ? language : this.properties.getQuery().getDefaultLanguage());
            for (final WbSearchEntitiesResult result : results) {

                documents.add(this.getById(result.getEntityId()));

                if ((maxElements > 0) && (++element >= maxElements)) {
                    break;
                }
            }
        } catch (final MediaWikiApiErrorException e) {
            throw new TripleStoreException("Error searching documents", e);
        }

        return documents;
    }

    /**
     * Create a new document.
     *
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
    private ItemDocument create(final Map<String, String> labels, final Map<String, String> descriptions,
            final Map<String, List<String>> aliases) throws TripleStoreException {
        ItemDocument item = ItemDocumentBuilder.forItemId(ItemIdValue.NULL).build();

        // Add labels
        item = this.addLabels(item, labels);

        // Add descriptions
        item = this.addDescriptions(item, descriptions);

        // Add aliases
        item = this.addAliases(item, aliases);

        ItemDocument newItem;
        try {
            newItem = this.dataEditor.createItemDocument(item, "Create new item", Collections.emptyList());
        } catch (IOException | MediaWikiApiErrorException e) {
            throw new TripleStoreException("Error creating document", e);
        }

        return newItem;
    }

    /**
     * Update an existing document.
     *
     * @param item
     *            {@link ItemDocument}
     * @param labels
     *            Map of labels
     * @param descriptions
     *            Map of descriptions
     * @param aliases
     *            Map of aliases
     * @param statement
     *            {@link Statement}
     * @return {@link ItemDocument}
     * @throws TripleStoreException
     *             in case of error.
     */
    private ItemDocument update(final ItemDocument item, final Map<String, String> labels,
            final Map<String, String> descriptions, final Map<String, List<String>> aliases, final Statement statement)
            throws TripleStoreException {
        ItemDocument itemDocument = item;

        // Add labels
        itemDocument = this.addLabels(itemDocument, labels);

        // Add descriptions
        itemDocument = this.addDescriptions(itemDocument, descriptions);

        // Add aliases
        itemDocument = this.addAliases(itemDocument, aliases);

        if (statement != null) {
            itemDocument = itemDocument.withStatement(statement);
        }

        ItemDocument updatedItem;
        try {
            updatedItem = this.dataEditor.editItemDocument(itemDocument, true, "Update item", Collections.emptyList());
        } catch (IOException | MediaWikiApiErrorException e) {
            throw new TripleStoreException("Error updating document", e);
        }

        return updatedItem;
    }

    /**
     * Create a new property.
     *
     * @param labels
     *            Map of labels
     * @param descriptions
     *            Map of descriptions
     * @param aliases
     *            Map of aliases
     * @param dataType
     *            Data type
     * @return {@link PropertyDocument}.
     * @throws TripleStoreException
     *             in case of error.
     */
    private PropertyDocument createProperty(final Map<String, String> labels, final Map<String, String> descriptions,
            final Map<String, List<String>> aliases, final String dataType) throws TripleStoreException {
        PropertyDocument prop = PropertyDocumentBuilder.forPropertyIdAndDatatype(PropertyIdValue.NULL, dataType)
                .build();

        // Add labels
        prop = this.addLabels(prop, labels);

        // Add descriptions
        prop = this.addDescriptions(prop, descriptions);

        // Add aliases
        prop = this.addAliases(prop, aliases);

        PropertyDocument newProperty;
        try {
            newProperty = this.dataEditor.createPropertyDocument(prop, "Create new property", Collections.emptyList());
        } catch (IOException | MediaWikiApiErrorException e) {
            throw new TripleStoreException("Error creating property", e);
        }

        return newProperty;
    }

    /**
     * Update an existing property.
     *
     * @param property
     *            {@link PropertyDocument}.
     * @param labels
     *            Map of labels
     * @param descriptions
     *            Map of descriptions
     * @param aliases
     *            Map of aliases
     * @return {@link PropertyDocument}.
     * @throws TripleStoreException
     *             in case of error.
     */
    private PropertyDocument updateProperty(final PropertyDocument property, final Map<String, String> labels,
            final Map<String, String> descriptions, final Map<String, List<String>> aliases)
            throws TripleStoreException {
        PropertyDocument prop = property;

        // Add labels
        prop = this.addLabels(prop, labels);

        // Add descriptions
        prop = this.addDescriptions(prop, descriptions);

        // Add aliases
        prop = this.addAliases(prop, aliases);

        PropertyDocument newProperty;
        try {
            newProperty = this.dataEditor.editPropertyDocument(prop, false, "Create new property",
                    Collections.emptyList());
        } catch (IOException | MediaWikiApiErrorException e) {
            throw new TripleStoreException("Error updating property", e);
        }

        return newProperty;
    }

    /**
     * Add labels to document.
     *
     * @param item
     *            {@link ItemDocument}
     * @param labels
     *            Map of labels.
     * @return New document.
     */
    @SuppressWarnings("unchecked")
    private <T extends TermedDocument> T addLabels(final T item, final Map<String, String> labels) {
        TermedDocument itemDocument = item;

        if (labels != null) {
            for (final Map.Entry<String, String> entry : labels.entrySet()) {
                itemDocument = itemDocument.withLabel(new MonolingualTextValueImpl(entry.getValue(), entry.getKey()));
            }
        }

        return (T) itemDocument;
    }

    /**
     * Add descriptions to document.
     *
     * @param item
     *            {@link ItemDocument}
     * @param descriptions
     *            Map of descriptions.
     * @return New document.
     */
    @SuppressWarnings("unchecked")
    private <T extends TermedDocument> T addDescriptions(final T item, final Map<String, String> descriptions) {
        TermedDocument itemDocument = item;

        if (descriptions != null) {
            for (final Map.Entry<String, String> entry : descriptions.entrySet()) {
                itemDocument = itemDocument
                        .withDescription(new MonolingualTextValueImpl(entry.getValue(), entry.getKey()));
            }
        }

        return (T) itemDocument;
    }

    /**
     * Add descriptions to document.
     *
     * @param item
     *            {@link ItemDocument}
     * @param aliases
     *            Map of aliases.
     * @return New document.
     */
    @SuppressWarnings("unchecked")
    private <T extends TermedDocument> T addAliases(final T item, final Map<String, List<String>> aliases) {
        TermedDocument itemDocument = item;

        if (aliases != null) {
            List<MonolingualTextValue> aliasesList;

            for (final Map.Entry<String, List<String>> entry : aliases.entrySet()) {
                aliasesList = new ArrayList<>();
                for (final String alias : entry.getValue()) {
                    aliasesList.add(new MonolingualTextValueImpl(alias, entry.getKey()));
                }
                itemDocument = itemDocument.withAliases(entry.getKey(), aliasesList);
            }
        }

        return (T) itemDocument;
    }

    /**
     * Gets a document by a search string.
     *
     * @param searchString
     *            Search string.
     * @param language
     *            Language.
     * @return {@link ItemDocument}
     * @throws TripleStoreException
     *             in case of error.
     */
    private ItemDocument getItemDocument(final String searchString, final String language) throws TripleStoreException {
        return (ItemDocument) this.searchFirst(searchString, language);
    }

    /**
     * Checks if it's a new item or an existing one.
     *
     * @param item
     *            Item to check
     * @return {@code true} if it's a new item
     */
    private boolean isNew(final ItemDocument item) {
        return (item == null) || (item.getEntityId() == null) || ItemIdValue.NULL.equals(item.getEntityId());
    }

    /**
     * Checks if it's a new property or an existing one.
     *
     * @param property
     *            Property to check
     * @return {@code true} if it's a new property
     */
    private boolean isNew(final PropertyDocument property) {
        return property == null;
    }
}
