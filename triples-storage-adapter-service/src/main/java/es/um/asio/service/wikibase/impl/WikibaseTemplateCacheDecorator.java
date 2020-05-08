package es.um.asio.service.wikibase.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;

import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.wikibase.WikibaseOperations;

/**
 * The Class WikibaseTemplateCacheDecorator.
 */
@Service
@Primary
public class WikibaseTemplateCacheDecorator implements WikibaseOperations {

    /** 
     * The wikibase operations.
     */
    @Autowired
    private WikibaseOperations wikibaseOperations;
    
    /** 
     * The properties map.
     */
    Map<String, PropertyDocument> propertiesMap = new HashMap<String, PropertyDocument>();
        
    /**
     *  The entities searched map.
    */
    Map<String, EntityDocument> entitiesMap = new HashMap<String, EntityDocument>();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDocument getById(String id) throws TripleStoreException {
        return wikibaseOperations.getById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDocument searchFirst(MonolingualTextValue searchText) throws TripleStoreException {
        String entityKey = searchText.getText();
        
        EntityDocument entityDocumentCached = entitiesMap.get(entityKey);
        if(entityDocumentCached != null) { 
            return entityDocumentCached;
        }
        
        var entityDocument = wikibaseOperations.searchFirst(searchText);
        entitiesMap.put(entityKey, entityDocumentCached);

        return entityDocument;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyDocument getOrCreateProperty(MonolingualTextValue label, MonolingualTextValue description, String dataTypeIdValue) throws TripleStoreException {        
        String propertyKey = label.getText().concat(dataTypeIdValue);
        
        PropertyDocument propertyDocumentCached = propertiesMap.get(propertyKey);
        if(propertyDocumentCached != null) { 
            return propertyDocumentCached;
        }
        
        var propertyDocument = wikibaseOperations.getOrCreateProperty(label, description, dataTypeIdValue);
        propertiesMap.put(propertyKey, propertyDocument);

        return propertyDocument;
    }

    @Override
    public ItemDocument insert(ItemDocument itemDocument) throws TripleStoreException {
        // TODO Auto-generated method stub
        return wikibaseOperations.insert(itemDocument);
    }

}
