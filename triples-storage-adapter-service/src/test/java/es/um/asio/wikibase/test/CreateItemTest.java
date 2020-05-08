package es.um.asio.wikibase.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.PropertyDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.ReferenceBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.implementation.DataObjectFactoryImpl;
import org.wikidata.wdtk.datamodel.implementation.MonolingualTextValueImpl;
import org.wikidata.wdtk.datamodel.implementation.PropertyIdValueImpl;
import org.wikidata.wdtk.datamodel.interfaces.DatatypeIdValue;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Reference;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementGroup;
import org.wikidata.wdtk.datamodel.interfaces.Value;
import org.wikidata.wdtk.datamodel.interfaces.ValueSnak;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.BasicApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

public class CreateItemTest {

	// private final static String siteIri = "http://www.test.wikidata.org/entity/";
	private final static String siteIri = "http://localhost:8181/wiki/entity/";

	private static PropertyIdValue stringProperty1;
	private static PropertyIdValue stringProperty2;

	public static void main(String[] args) throws IOException, MediaWikiApiErrorException, LoginFailedException {
	    

	
	 /*   //Test guardar dos propiedades con mismo label y diferente description
	    WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");
	    final BasicApiConnection connection = new BasicApiConnection("http://localhost:8181/api.php");
	    final WikibaseDataEditor wbde = new WikibaseDataEditor(connection, siteIri);
	    
	    
	    PropertyDocument propertyDocument1 = PropertyDocumentBuilder
                .forPropertyIdAndDatatype(PropertyIdValue.NULL, DatatypeIdValue.DT_STRING)
                .withDescription(new MonolingualTextValueImpl("Descripcion prueba", "en"))  
                .withAlias(new MonolingualTextValueImpl("Alias", "en"))
                .withLabel(new MonolingualTextValueImpl("Label00006", "en")) 
                .build();
        
        
        try {
            wbde.createPropertyDocument(propertyDocument1, "One summary",null);
        } catch (IOException | MediaWikiApiErrorException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        

        PropertyDocument propertyDocument2 = PropertyDocumentBuilder
                .forPropertyIdAndDatatype(PropertyIdValue.NULL, DatatypeIdValue.DT_STRING)
                .withDescription(new MonolingualTextValueImpl("Descripcion prueba 2", "en"))
                .withAlias(new MonolingualTextValueImpl("Alias2", "en"))
                .withLabel(new MonolingualTextValueImpl("Label00006", "en")) 
                .build();
        
        try {
            wbde.createPropertyDocument(propertyDocument2,"Other summary",null);
        } catch (IOException | MediaWikiApiErrorException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
	    */
	    
	/*	// Always set your User-Agent to the name of your application:
		WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");

		//final ApiConnection connection = BasicApiConnection.getTestWikidataApiConnection();
	
		final BasicApiConnection connection = new BasicApiConnection("http://localhost:8181/api.php");
        // connection.login("WikibaseAdmin", "WikibaseDockerAdminPass");
				
		
		// Optional login -- required for operations on real wikis:
		// connection.login("my username", "my password");
		final WikibaseDataEditor wbde = new WikibaseDataEditor(connection, siteIri);

		
		// Find some test properties on test.wikidata.org:
		findSomeStringProperties(connection);

		final ItemIdValue noid = ItemIdValue.NULL; // used when creating new items
		final PropertyIdValue noidP = PropertyIdValue.NULL;
					
		var property = getOrCreateProperty(wbde, connection);
        
		final Statement statement1 = 
		        StatementBuilder.forSubjectAndProperty(noid, property.getEntityId())
		        .withValue(Datamodel.makeStringValue("jan107"))
		        .build();
					

		final ItemDocument itemDocument = ItemDocumentBuilder
		        .forItemId(noid)
		        .withLabel("Wikidata Toolkit test", "en")
				.withStatement(statement1)
				.build();
		
		ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
				"Wikidata Toolkit example test item creation", Collections.emptyList());

		final ItemIdValue newItemId = newItemDocument.getEntityId();
		System.out.println("*** Successfully created a new item " + newItemId.getId()
				+ " (see http://localhost:8181/w/index.php?title=" + newItemId.getId() + "&oldid="
				+ newItemDocument.getRevisionId() + " for this version)");
		
		
		
		final ItemIdValue otherNoid = ItemIdValue.NULL;  
		   
	      var otherProperty = getOrCreateProperty2(wbde, connection);
 
	      Reference reference = ReferenceBuilder.newInstance().withPropertyValue(otherProperty.getEntityId(), newItemId)
	                .build(); 
	      
	      Statement s1 = StatementBuilder
	                  .forSubjectAndProperty(otherNoid, otherProperty.getEntityId())
	                 .withReference(reference).build();
	      

	        final ItemDocument itemDocument2 = ItemDocumentBuilder
	                .forItemId(otherNoid)
	                .withLabel("Pruebas", "en")
	                .withStatement(s1)
	                .build();

	        ItemDocument newItemDocument2 = wbde.createItemDocument(itemDocument2,
	                "Wikidata Toolkit example test item creation", Collections.emptyList());
	        */
    
	}
	
	private static PropertyDocument getOrCreateProperty2(final WikibaseDataEditor wbde, final ApiConnection connection) throws MediaWikiApiErrorException, IOException {
        PropertyDocument propertyDocument = PropertyDocumentBuilder
                .forPropertyIdAndDatatype(PropertyIdValue.NULL, DatatypeIdValue.DT_ITEM)
                .withDescription(new MonolingualTextValueImpl("Description50", "en"))
                .withLabel(new MonolingualTextValueImpl("Label50", "en")) 
                .build();
        
        
        try {
            wbde.createPropertyDocument(propertyDocument, StringUtils.EMPTY,null);
        } catch (IOException | MediaWikiApiErrorException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        final WikibaseDataFetcher wbdf = new WikibaseDataFetcher(connection, siteIri);
        wbdf.getFilter().setLanguageFilter(Collections.singleton("en"));
        

        PropertyDocument property = null;
        int propertyNumber = 1;
        while (property == null) {
            final ArrayList<String> fetchProperties = new ArrayList<>();
            for (int i = propertyNumber; i < (propertyNumber + 10); i++) {
                fetchProperties.add("P" + i);
            }
            propertyNumber += 10;
            final Map<String, EntityDocument> results = wbdf.getEntityDocuments(fetchProperties);
            for (final EntityDocument ed : results.values()) {
                final PropertyDocument pd = (PropertyDocument) ed;
                if (DatatypeIdValue.DT_ITEM.equals(pd.getDatatype().getIri()) 
                        && pd.getLabels().containsValue(new MonolingualTextValueImpl("Label50", "en"))
                        && pd.getDescriptions().containsValue(new MonolingualTextValueImpl("Description50", "en"))) {                    
                    System.out.println("* Found string property " + pd.getEntityId().getId() + " (" + pd.getLabels().get("en") + ")");
                    property = pd;
                }
            }
        }

        return property;
    }

	
	
	private static PropertyDocument getOrCreateProperty(final WikibaseDataEditor wbde, final ApiConnection connection) throws MediaWikiApiErrorException, IOException {
	    PropertyDocument propertyDocument = PropertyDocumentBuilder
	            .forPropertyIdAndDatatype(PropertyIdValue.NULL, DatatypeIdValue.DT_STRING)
                .withDescription(new MonolingualTextValueImpl("Description2", "en"))
                .withLabel(new MonolingualTextValueImpl("Label2", "en")) 
                .build();
	    
	    
        try {
            wbde.createPropertyDocument(propertyDocument, StringUtils.EMPTY,null);
        } catch (IOException | MediaWikiApiErrorException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        final WikibaseDataFetcher wbdf = new WikibaseDataFetcher(connection, siteIri);
        wbdf.getFilter().setLanguageFilter(Collections.singleton("en"));
        

        PropertyDocument property = null;
        int propertyNumber = 1;
        while (property == null) {
            final ArrayList<String> fetchProperties = new ArrayList<>();
            for (int i = propertyNumber; i < (propertyNumber + 10); i++) {
                fetchProperties.add("P" + i);
            }
            propertyNumber += 10;
            final Map<String, EntityDocument> results = wbdf.getEntityDocuments(fetchProperties);
            for (final EntityDocument ed : results.values()) {
                final PropertyDocument pd = (PropertyDocument) ed;
                if (DatatypeIdValue.DT_STRING.equals(pd.getDatatype().getIri()) 
                        && pd.getLabels().containsValue(new MonolingualTextValueImpl("Label1", "en"))
                        && pd.getDescriptions().containsValue(new MonolingualTextValueImpl("Description1", "en"))) {                    
                    System.out.println("* Found string property " + pd.getEntityId().getId() + " (" + pd.getLabels().get("en") + ")");
                    property = pd;
                }
            }
        }

        return property;
	}

	public static void findSomeStringProperties(final ApiConnection connection)
			throws MediaWikiApiErrorException, IOException {
		final WikibaseDataFetcher wbdf = new WikibaseDataFetcher(connection, siteIri);
		wbdf.getFilter().excludeAllProperties();
		wbdf.getFilter().setLanguageFilter(Collections.singleton("en"));

		final ArrayList<PropertyIdValue> stringProperties = new ArrayList<>();

		System.out.println("*** Trying to find string properties for the example ... ");
		int propertyNumber = 1;
		while (stringProperties.size() < 5) {
			final ArrayList<String> fetchProperties = new ArrayList<>();
			for (int i = propertyNumber; i < (propertyNumber + 10); i++) {
				fetchProperties.add("P" + i);
			}
			propertyNumber += 10;
			final Map<String, EntityDocument> results = wbdf.getEntityDocuments(fetchProperties);
			for (final EntityDocument ed : results.values()) {
				final PropertyDocument pd = (PropertyDocument) ed;
				if (DatatypeIdValue.DT_STRING.equals(pd.getDatatype().getIri()) && pd.getLabels().containsKey("en")) {
					stringProperties.add(pd.getEntityId());
					System.out.println("* Found string property " + pd.getEntityId().getId() + " ("
							+ pd.getLabels().get("en") + ")");
					stringProperty1 = stringProperties.get(0);
					return;
				}
			}
		}

		
		

		System.out.println("*** Done.");
	}
}
