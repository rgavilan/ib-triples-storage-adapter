package es.um.asio.wikibase.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.implementation.PropertyIdValueImpl;
import org.wikidata.wdtk.datamodel.interfaces.DatatypeIdValue;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.Value;
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
		// Always set your User-Agent to the name of your application:
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
		
		// PropertyIdValue property107 = new PropertyIdValueImpl("P1", siteIri);
		// PropertyIdValue property108 = new PropertyIdValueImpl("P2", siteIri);
						
		final Statement statement1 = StatementBuilder.forSubjectAndProperty(noid, Datamodel.makeWikidataPropertyIdValue("P245962")).withValue(Datamodel.makeStringValue("jan107")).build();
					

		final ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid).withLabel("Wikidata Toolkit test", "en")
				.withStatement(statement1).build();
		
		ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
				"Wikidata Toolkit example test item creation", Collections.emptyList());

		final ItemIdValue newItemId = newItemDocument.getEntityId();
		System.out.println("*** Successfully created a new item " + newItemId.getId()
				+ " (see http://localhost:8181/w/index.php?title=" + newItemId.getId() + "&oldid="
				+ newItemDocument.getRevisionId() + " for this version)");

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
