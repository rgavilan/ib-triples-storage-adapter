package es.um.asio.service.wikibase;

import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
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
     * Get the first item document containing the {@linkplain MonolingualTextValue}.
     *
     * @param textValue the text value
     * @return the entity document
     * @throws TripleStoreException the triple store exception
     */
    ItemDocument getItem(MonolingualTextValue label) throws TripleStoreException;
    
    
    /**
     * Get or create an item document containing the {@linkplain MonolingualTextValue}.
     *
     * @param textValue the text value
     * @return the entity document
     * @throws TripleStoreException the triple store exception
     */
    ItemDocument getOrCreateItem(MonolingualTextValue label) throws TripleStoreException;

    /**
     * Item to insert.
     *
     * @param itemDocument the item document
     * @return the item document
     * @throws TripleStoreException the triple store exception
     */
    ItemDocument insert(ItemDocument itemDocument) throws TripleStoreException;
    
   
    /**
     * Replace.
     *
     * @param itemDocument the item document
     * @return the item document
     * @throws TripleStoreException the triple store exception
     */
    ItemDocument replace(ItemDocument itemDocument) throws TripleStoreException;
    
    
    /**
     * Gets or create property.
     *
     * @param label the label
     * @param description the description
     * @param dataTypeIdValue the data type id value
     * @return the or create property
     * @throws TripleStoreException the triple store exception
     */
    PropertyDocument getOrCreateProperty(MonolingualTextValue label, MonolingualTextValue description, String dataTypeIdValue)
            throws TripleStoreException;


}
