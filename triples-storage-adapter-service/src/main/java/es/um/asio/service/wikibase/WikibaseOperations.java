package es.um.asio.service.wikibase;

import java.util.List;
import java.util.Map;

import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
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
     * Search the first document containing the {@linkplain MonolingualTextValue}.
     *
     * @param searchText the search text
     * @return the entity document
     * @throws TripleStoreException the triple store exception
     */
    EntityDocument searchFirst(MonolingualTextValue searchText) throws TripleStoreException;

    /**
     * Gets the or create property.
     *
     * @param label the label
     * @param description the description
     * @param dataTypeIdValue the data type id value
     * @return the or create property
     * @throws TripleStoreException the triple store exception
     */
    PropertyDocument getOrCreateProperty(MonolingualTextValue label, MonolingualTextValue description, String dataTypeIdValue)
            throws TripleStoreException;


    /**
     * Item to insert.
     *
     * @param itemDocument the item document
     * @return the item document
     * @throws TripleStoreException the triple store exception
     */
    ItemDocument insert(ItemDocument itemDocument) throws TripleStoreException;

}
