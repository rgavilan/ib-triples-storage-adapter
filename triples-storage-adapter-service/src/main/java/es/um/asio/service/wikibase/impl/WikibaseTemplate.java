package es.um.asio.service.wikibase.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.helpers.PropertyDocumentBuilder;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.wikibase.WikibaseConnectionManager;
import es.um.asio.service.wikibase.WikibaseOperations;

/**
 * Wikibase template
 */
@ConditionalOnProperty(prefix = "app.wikibase", name = "enabled", havingValue = "true", matchIfMissing = false)
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
    public ItemDocument searchItem(final MonolingualTextValue textValue) throws TripleStoreException {       
        ItemDocument item = null;
        int itemNumber = 1;
        Map<String, EntityDocument> results;
        try {
            do {
                final ArrayList<String> fetchItems = new ArrayList<>();
                for (int i = itemNumber; i < (itemNumber + 1000); i++) {
                    fetchItems.add("Q" + i);
                }
                itemNumber += 1000;
                results = dataFetcher.getEntityDocuments(fetchItems);
                for (final EntityDocument ed : results.values()) {
                    final ItemDocument pd = (ItemDocument) ed;
                    if (pd.getLabels().containsValue(textValue)) {
                        return pd;
                    }
                }
            }
            while(results!=null && !results.isEmpty());
            
        } catch (Exception e) {
            throw new TripleStoreException(e);
        }
      
        return item;
    }

 
    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyDocument getOrCreateProperty(MonolingualTextValue label, MonolingualTextValue description, String dataTypeIdValue)
            throws TripleStoreException {        
        //Properties throw an exception when created. Therefore, first we create it
        //and then we search for it based on label
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
            //e.printStackTrace();
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
        Map<String, EntityDocument> results = null;
        do {
            final ArrayList<String> fetchProperties = new ArrayList<>();
            for (int i = propertyNumber; i < (propertyNumber + 1000); i++) {
                fetchProperties.add("P" + i);
            }
            propertyNumber += 1000;
            results = dataFetcher.getEntityDocuments(fetchProperties);
            for (final EntityDocument ed : results.values()) {
                final PropertyDocument pd = (PropertyDocument) ed;
                if (dataTypeIdValue.equals(pd.getDatatype().getIri()) && pd.getLabels().containsValue(label)) {                    
                    return pd;
                }
            }
        }
        while(results!=null && !results.isEmpty());
        
        return property;
    }
   

}
