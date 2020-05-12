package es.um.asio.service.service.impl;

import java.io.IOException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.interfaces.DatatypeIdValue;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;
import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.service.TriplesStorageService;
import es.um.asio.service.util.TrellisUtils;
import es.um.asio.service.util.WikibaseUtils;
import es.um.asio.service.wikibase.WikibaseOperations;

@ConditionalOnProperty(prefix = "app.wikibase", name = "enabled", havingValue = "true", matchIfMissing = false)
@Service
public class WikibaseStorageServiceImpl implements TriplesStorageService {

	private final Logger logger = LoggerFactory.getLogger(WikibaseStorageServiceImpl.class);
	
    /** 
     * The trellis utils.
    */
    @Autowired
    private TrellisUtils trellisUtils;
    
    /** 
     * The wikibase utils. 
    */
    @Autowired
    private WikibaseUtils wikibaseUtils;
    
    /**
     * Wikibase template
     */
    @Autowired
    private WikibaseOperations template;

	@Override
	public void process(ManagementBusEvent message) throws TripleStoreException {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Insert new message: {}", message);
		}

		switch (message.getOperation()) {
		case INSERT:		    
	        this.save(message);
			break;
		case UPDATE:
			break;
		case DELETE:
			break;
		default:
			break;
		}
	}

	/**
	 * Save.
	 *
	 * @param message the message
	 * @throws TripleStoreException the triple store exception
	 * @throws IOException 
	 * @throws MediaWikiApiErrorException 
	 */
	void save(ManagementBusEvent message) throws TripleStoreException {
	    logger.info("Saving object in Wikibase : " + message.toString());

		Model model = trellisUtils.toObject(message.getModel());	
        var statements =  model.listStatements().toList();

		ItemIdValue itemId = ItemIdValue.NULL; 
        ItemDocumentBuilder itemDocumentBuilder = ItemDocumentBuilder.forItemId(itemId)
                .withLabel(wikibaseUtils.createMonolingualTextValue(statements.get(0).getSubject().getURI()));
        
        for (Statement statement : statements) {
            var wikiStatement = convertToWikiStatement(statement, itemId);
            if(wikiStatement != null) {
                itemDocumentBuilder.withStatement(wikiStatement);
            }
        }
     	
    	template.insert(itemDocumentBuilder.build());
    	
        logger.info("GRAYLOG-TS Creado recurso en wikibase de tipo: " + message.getClassName());
	}

	/**
	 * Convert {@link org.apache.jena.rdf.model.Statement} to {@link org.wikidata.wdtk.datamodel.interfaces.Statement}
	 *
	 * @param statement the statement
	 * @param itemId the item id
	 * @return the org.wikidata.wdtk.datamodel.interfaces. statement
	 * @throws TripleStoreException the triple store exception
	 */
    private org.wikidata.wdtk.datamodel.interfaces.Statement convertToWikiStatement(Statement statement, ItemIdValue itemId) throws TripleStoreException {
	    
	    if(IsModelType(statement)) {
            
            PropertyDocument propertyDocument = getOrCreateProperty(statement.getPredicate(), DatatypeIdValue.DT_STRING);
            if(propertyDocument == null) {
                logger.warn("Property not found {}", statement.getPredicate());
                return null;
            }
            String propertyValue =  statement.getResource().getURI();
            return StatementBuilder.forSubjectAndProperty(itemId, propertyDocument.getEntityId())
                    .withValue(Datamodel.makeStringValue(propertyValue))
                    .build();
        }
        
        if(IsReferenceToAnotherEntity(statement)) {
            
            String resource = statement.getResource().getURI();
            var item = template.searchItem(wikibaseUtils.createMonolingualTextValue(resource));
            if(item == null) {
                logger.warn("Resource not found {}", resource);
                return null;
            }
            PropertyDocument propertyDocument = getOrCreateProperty(statement.getPredicate(), DatatypeIdValue.DT_ITEM);
            if(propertyDocument == null) {
                logger.warn("Property not found {}", statement.getPredicate());
                return null;
            }
            return StatementBuilder.forSubjectAndProperty(itemId, propertyDocument.getEntityId())
                    .withValue(item.getEntityId())
                    .build();
        }
        
        else if(IsTextProperty(statement)) {
            
            PropertyDocument propertyDocument = getOrCreateProperty(statement.getPredicate(), DatatypeIdValue.DT_STRING);
            if(propertyDocument == null) {
                logger.warn("Property not found {}", statement.getPredicate());
                return null;
            }
            
            String propertyValue = this.wikibaseUtils.sanitizePropertyValue(statement.getString());
            
            return StatementBuilder.forSubjectAndProperty(itemId, propertyDocument.getEntityId())
                    .withValue(Datamodel.makeStringValue(propertyValue))
                    .build();
        }
        
         
        logger.warn("The statement could not be translated to wikibase");
       
        return null;
	}
	
    /**
     * Checks if statement defines the type of the model.
     *
     * @param statement the statement
     * @return true, if successful
     */
    private boolean IsModelType(Statement statement) {
        return statement.getObject().isResource() && statement.getPredicate().getURI().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
    }
    
    /**
     * Checks if is statement defines a reference to another entity
     *
     * @param statement the statement
     * @return true, if successful
     */
    private boolean IsReferenceToAnotherEntity(Statement statement) {
        return !IsModelType(statement) && statement.getObject().isResource();
    }
	
    /**
     * Checks if is statement defines a simple (string) property
     *
     * @param statement the statement
     * @return true, if successful
     */
    private boolean IsTextProperty(Statement statement) {
        return statement.getObject().isLiteral();
    }
	
	/**
	 * Gets the or create property.
	 *
	 * @param property the property
	 * @return the or create propertyme e
	 * @throws TripleStoreException the triple store exception
	 */
	private PropertyDocument getOrCreateProperty(Property property, String dataTypeIdValue) throws TripleStoreException {
	    var label = wikibaseUtils.createMonolingualTextValue(property.toString());
        var description = wikibaseUtils.createMonolingualTextValue(property.getLocalName());
       
        return template.getOrCreateProperty(label, description, dataTypeIdValue);
	}

}
