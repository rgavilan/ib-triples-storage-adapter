package es.um.asio.service.service.uris;

public interface UrisFactoryClient {

	String getLocalStorageUriByResource(String id, String className);
	
	String getCanonicalUriByResource(String id, String className);

	String createProperty(String fieldName);
	
	void eventNotifyUrisFactory(String canonicalLanguageURI, String localURI, String triplesStoreTarget);
}
