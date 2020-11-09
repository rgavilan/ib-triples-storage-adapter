package es.um.asio.service.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import es.um.asio.abstractions.constants.Constants;
import es.um.asio.abstractions.domain.ManagementBusEvent;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.service.TriplesStorageService;
import es.um.asio.service.service.uris.UrisFactoryClient;
import es.um.asio.service.util.TriplesStorageUtils;
import es.um.asio.service.util.WikibaseUtils;
import es.um.asio.service.wikibase.WikibaseOperations;

@ConditionalOnProperty(prefix = "app.wikibase", name = "enabled", havingValue = "true", matchIfMissing = false)
@Service
public class WikibaseStorageServiceImpl implements TriplesStorageService {

	private final Logger logger = LoggerFactory.getLogger(WikibaseStorageServiceImpl.class);
	
	private static final String[] ALLOWED_TYPE_DATA = {"Universidad", "GrupoInvestigacion", "Proyecto"}; 
	
    
    /** The triples storage utils. */
    @Autowired
    private TriplesStorageUtils triplesStorageUtils;
    
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
    
    /** The uris factory client. */
    @Autowired
    private UrisFactoryClient urisFactoryClient;

	@Override
	public void process(ManagementBusEvent message) throws TripleStoreException {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Insert new message: {}", message);
		}

		if(checkNotAllowedDataPolicy(message)) {
			this.logger.warn("The type {} is not allowed to process in Wikibase ", message.getClassName());
		} else {
			switch (message.getOperation()) {
			case INSERT:		    
				this.save(message);
				break;
			case UPDATE:
				break;
			case DELETE:
				break;
			case LINKED_INSERT:
				this.saveLinks(message);
				break;
			default:
				break;
			}
		}
	}
	
	private void saveLinks(ManagementBusEvent message) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Check allowed data policy.
	 * Allowed type of data:
	 * {@value es.um.asio.service.util.WikibaseConstants#NOT_ALLOWED_TYPE_DATA} 
	 * @param message the message
	 * @return true, if successful
	 */
	private boolean checkNotAllowedDataPolicy(ManagementBusEvent message) {
		boolean result = false;
		result = Arrays.asList(WikibaseStorageServiceImpl.ALLOWED_TYPE_DATA).stream().anyMatch(s->message.getClassName().equalsIgnoreCase(s));
		return !result;
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
	    logger.info("Saving object in Wikibase: {} - {}", message.getClassName(), message.getIdModel());

	    // we retrieve the model
		Model model = triplesStorageUtils.toObject(message.getModel());
		
        List<Statement> statements =  model.listStatements().toList();
        String modelId = statements.get(0).getSubject().getURI();
        if(StringUtils.isEmpty(modelId)) {
            modelId = message.getIdModel();
        }
        
        MonolingualTextValue itemToSaveLabel = wikibaseUtils.createMonolingualTextValue(modelId);
        ItemIdValue itemToSaveId = this.getItemIdValue(itemToSaveLabel);
        ItemDocumentBuilder itemDocumentBuilder = ItemDocumentBuilder.forItemId(itemToSaveId).withLabel(itemToSaveLabel);
        
        for (Statement statement : statements) {
            var wikiStatement = convertToWikiStatement(statement, itemToSaveId);
            if(wikiStatement != null) {
                itemDocumentBuilder.withStatement(wikiStatement);
            }
        }
     	        
        ItemDocument itemToSave = itemDocumentBuilder.build();
        ItemDocument savedItem = null;
        if(itemToSave.getEntityId().equals(ItemIdValue.NULL)) {
        	savedItem = template.insert(itemToSave);
        } else {
        	savedItem = template.replace(itemToSave);            
        }
        
        // factory uri notification
        if(savedItem != null && savedItem.getEntityId() != null) {
        	String canonicalLanguageURI = modelId;
        	String localURI = savedItem.getEntityId().getIri();        
        	logger.info("FactoryUriNotification: canonicalUri {}, localUri {}", canonicalLanguageURI, localURI);
        	this.urisFactoryClient.eventNotifyUrisFactory(canonicalLanguageURI, localURI, Constants.WIKIBASE);        	
        	logger.info("GRAYLOG-TS Creado recurso en wikibase de tipo: {}",message.getClassName());
        } else {
        	logger.error("Error creating resource in Wikibase: {}",message);
        }
	}

	/**
	 * Gets the item id value. Returns {@link ItemIdValue.NULL} if not exists in wikibase
	 *
	 * @param itemLabel the item label
	 * @return the item id value
	 * @throws TripleStoreException the triple store exception
	 */
	private ItemIdValue getItemIdValue(MonolingualTextValue itemLabel) throws TripleStoreException {
	    ItemIdValue itemIdValue = ItemIdValue.NULL; 
        ItemDocument itemDocument = this.template.getItem(itemLabel);
        if(itemDocument != null) {
            itemIdValue = itemDocument.getEntityId();
        }
        return itemIdValue;
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
	   
        if(isReferenceToAnotherEntity(statement)) {
            
            String resource = statement.getResource().getURI();
            if(StringUtils.isEmpty(resource)) {
                logger.warn("Resource is null {}", statement);
                return null;
            }
            ItemDocument item = template.getOrCreateItem(wikibaseUtils.createMonolingualTextValue(resource));
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
        
        else if(isTextProperty(statement)) {
            
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
     * Checks if is statement defines a reference to another entity
     *
     * @param statement the statement
     * @return true, if successful
     */
    private boolean isReferenceToAnotherEntity(Statement statement) {
        return statement.getObject().isResource();
    }
	
    /**
     * Checks if is statement defines a simple (string) property
     *
     * @param statement the statement
     * @return true, if successful
     */
    private boolean isTextProperty(Statement statement) {
        return statement.getObject().isLiteral();
    }
	
	 
	/**
	 *  Gets the or create property.
	 *
	 * @param property the property
	 * @param dataTypeIdValue the data type id value
	 * @return the or create property
	 * @throws TripleStoreException the triple store exception
	 */
	private PropertyDocument getOrCreateProperty(Property property, String dataTypeIdValue) throws TripleStoreException {
	    //We add a "." to the description, because the properties can't have the same value for label and description
	    var label = wikibaseUtils.createMonolingualTextValue(property.getLocalName());
        var description = wikibaseUtils.createMonolingualTextValue(property.getLocalName().concat("."));
       
        return template.getOrCreateProperty(label, description, dataTypeIdValue);
	}

}
