package es.um.asio.service.wikibase.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.helpers.PropertyDocumentBuilder;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import es.um.asio.service.config.properties.WikibaseProperties;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.wikibase.WikibaseConnectionManager;
import es.um.asio.service.wikibase.WikibaseOperations;

/**
 * Wikibase template
 */
@Service(value = "WikibaseTemplate")
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


    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDocument searchFirst(final MonolingualTextValue searchText) throws TripleStoreException {        
        final List<EntityDocument> documents = this.search(searchText.getText(), searchText.getLanguageCode(), 1);
        return documents.isEmpty() ? null : documents.get(0);
    }

 
    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyDocument getOrCreateProperty(MonolingualTextValue label, MonolingualTextValue description, String dataTypeIdValue)
            throws TripleStoreException {        
        //Properties throw an exception when created. Therefore, first we create it
        //and then we search for it based on label and description
        //Related with https://github.com/Wikidata/Wikidata-Toolkit/issues/419
        
        PropertyDocument propertyDocument = PropertyDocumentBuilder
                .forPropertyIdAndDatatype(PropertyIdValue.NULL, dataTypeIdValue)
                .withLabel(label)
                .withDescription(description)                
                .build();
        
        PropertyDocument newProperty = null;
        
        //1. we create the property
        try {
            newProperty = this.dataEditor.createPropertyDocument(propertyDocument, "Create new property", Collections.emptyList());
        } catch (IOException | MediaWikiApiErrorException e) {
            e.printStackTrace();
        }
        
        //2. search the property 
        try {
            newProperty = searchProperty(label, dataTypeIdValue);
        } catch (IOException | MediaWikiApiErrorException e) {
            throw new TripleStoreException("Error creating property", e);
        }
        
        return newProperty;
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemDocument insert(ItemDocument itemDocument) throws TripleStoreException {
        ItemDocument newItem;
        try {
            newItem = this.dataEditor.createItemDocument(itemDocument, "Create new item", Collections.emptyList());
        } catch (IOException | MediaWikiApiErrorException e) {
            throw new TripleStoreException("Error creating document", e);
        }       
        
        return newItem;
    }
    
    
    /**
     * Gets the by id.
     *
     * @param id the id
     * @param siteUri the site uri
     * @return the by id
     * @throws TripleStoreException the triple store exception
     */
    private EntityDocument getById(final String id, final String siteUri) throws TripleStoreException {
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
     * Search property.
     *
     * @param label the label
     * @param dataTypeIdValue the data type id value
     * @return the property document
     * @throws MediaWikiApiErrorException the media wiki api error exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private PropertyDocument searchProperty(MonolingualTextValue label, String dataTypeIdValue) throws MediaWikiApiErrorException, IOException {       
        PropertyDocument property = null;
        int propertyNumber = 1;
        while (property == null) {
            final ArrayList<String> fetchProperties = new ArrayList<>();
            for (int i = propertyNumber; i < (propertyNumber + 100); i++) {
                fetchProperties.add("P" + i);
            }
            propertyNumber += 100;
            final Map<String, EntityDocument> results = dataFetcher.getEntityDocuments(fetchProperties);
            for (final EntityDocument ed : results.values()) {
                final PropertyDocument pd = (PropertyDocument) ed;
                if (dataTypeIdValue.equals(pd.getDatatype().getIri()) && pd.getLabels().containsValue(label)) {                    
                    return pd;
                }
            }
        }
        return property;
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
}
