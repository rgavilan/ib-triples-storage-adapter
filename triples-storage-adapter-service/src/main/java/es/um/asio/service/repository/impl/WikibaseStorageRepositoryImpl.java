package es.um.asio.service.repository.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.Statement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import es.um.asio.service.config.properties.WikibaseProperties;
import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.repository.WikibaseStorageRepository;
import es.um.asio.service.util.WikibaseConstants;
import es.um.asio.service.wikibase.WikibaseTemplate;

public class WikibaseStorageRepositoryImpl implements WikibaseStorageRepository {

	
	private final Logger logger = LoggerFactory.getLogger(WikibaseStorageRepositoryImpl.class);
	
	/**
     * Wikibase template
     */
    @Autowired
    private WikibaseTemplate template;

    /**
     * Wikibase properties.
     */
    @Autowired
    private WikibaseProperties properties;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final JsonObject jData) throws TripleStoreException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Saving data to Wikibase: {}", jData);
        }

        final Map<String, String> labels = this.getLabels(jData);
        final Map<String, String> descriptions = this.getDescriptions(jData);

        if (!labels.isEmpty()) {
            final ItemDocument insertedItem = this.saveItem(labels, descriptions);

            if (insertedItem != null) {
                for (final Map.Entry<String, JsonElement> att : jData.entrySet()) {
                    final String key = att.getKey();
                    final String value = att.getValue().getAsString();
                    if (!(key.trim().equals(WikibaseConstants.LABEL_ES) || key.trim().equals(WikibaseConstants.LABEL_EN)
                            || key.trim().equals(WikibaseConstants.DESCRIPTION_ES)
                            || key.trim().equals(WikibaseConstants.DESCRIPTION_EN)
                            || key.trim().equals(WikibaseConstants.ALIASES_ES)
                            || key.trim().equals(WikibaseConstants.ALIASES_EN))) {
                        this.generateStatement(insertedItem, key, value);
                    }
                }
            }
        }
    }

    private Map<String, String> getLabels(final JsonObject jData) {
        final Map<String, String> labels = new HashMap<>();

        if (jData.has(WikibaseConstants.LABEL_ES)) {
            labels.put(WikibaseConstants.LANGUAGE_ES, jData.get(WikibaseConstants.LABEL_ES).getAsString());
        }
        if (jData.has(WikibaseConstants.LABEL_EN)) {
            labels.put(WikibaseConstants.LANGUAGE_EN, jData.get(WikibaseConstants.LABEL_EN).getAsString());
        }

        return labels;
    }

    private Map<String, String> getDescriptions(final JsonObject jData) {
        final Map<String, String> descriptions = new HashMap<>();

        if (jData.has(WikibaseConstants.DESCRIPTION_ES)) {
            descriptions.put(WikibaseConstants.LANGUAGE_ES, jData.get(WikibaseConstants.DESCRIPTION_ES).getAsString());
        }
        if (jData.has(WikibaseConstants.DESCRIPTION_EN)) {
            descriptions.put(WikibaseConstants.LANGUAGE_EN, jData.get(WikibaseConstants.DESCRIPTION_EN).getAsString());
        }

        return descriptions;
    }

    private ItemDocument saveItem(final Map<String, String> labels, final Map<String, String> descriptions)
            throws TripleStoreException {
        ItemDocument insertedItem = null;

        for (final Map.Entry<String, String> l : labels.entrySet()) {
            insertedItem = this.template.save(l.getValue(), l.getKey(), labels, descriptions, null);

            if (insertedItem != null) {
                break;
            }
        }

        return insertedItem;
    }

    private void generateStatement(final ItemDocument insertedItem, final String key, String value)
            throws TripleStoreException {
        final String[] splitedKey = key.split(":");
        if (splitedKey.length == 3) {
            final PropertyDocument pd = (PropertyDocument) this.template.getById(splitedKey[1]);
            Statement statement;
            if (splitedKey[splitedKey.length - 1].trim().equalsIgnoreCase(WikibaseConstants.WIKIBASE_ITEM)) { // Es una
                                                                                                              // entidad
                if (value.toUpperCase().contains("I:")) {
                    value = value.replace("I:", "");
                    statement = this.template.generateStatement(insertedItem.getEntityId(), pd.getEntityId(),
                            Datamodel.makeItemIdValue(value, this.properties.getSite().getUri()));
                } else {
                    final String[] values = value.split(WikibaseConstants.ITEM_SEPARATOR);
                    final EntityDocument ed = this.template.searchFirst(values[1], values[0]);
                    statement = this.template.generateStatement(insertedItem.getEntityId(), pd.getEntityId(),
                            Datamodel.makeItemIdValue(ed.getEntityId().getId(), this.properties.getSite().getUri()));
                }
            } else {
                statement = this.template.generateStatement(insertedItem.getEntityId(), pd.getEntityId(),
                        Datamodel.makeStringValue(value));
            }
            if (statement != null) {
                this.template.updateStatement(insertedItem, statement);
            }
        }
    }
}
