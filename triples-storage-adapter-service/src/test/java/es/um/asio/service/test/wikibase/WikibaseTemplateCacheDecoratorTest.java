package es.um.asio.service.test.wikibase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.wikidata.wdtk.datamodel.implementation.MonolingualTextValueImpl;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;

import es.um.asio.service.exception.TripleStoreException;
import es.um.asio.service.wikibase.WikibaseOperations;
import es.um.asio.service.wikibase.impl.WikibaseTemplateCacheDecorator;

@RunWith(SpringRunner.class)
public class WikibaseTemplateCacheDecoratorTest {

    @TestConfiguration
    static class WikibaseTemplateCacheDecoratorTestConfiguration {
        @Bean
        public WikibaseTemplateCacheDecorator wikibaseOperations() {
            return new WikibaseTemplateCacheDecorator();
        }        
    } 
    
    @Autowired
    private WikibaseTemplateCacheDecorator wikibaseTemplateCacheDecorator;
    
    @MockBean(name = "WikibaseTemplate",reset = MockReset.BEFORE)
    private WikibaseOperations wikibaseTemplate;
    
    @Test
    public void whenCallGetById_thenCallWikibaseOperations() throws TripleStoreException {
        wikibaseTemplateCacheDecorator.getById("dummy");
        
        verify(wikibaseTemplate).getById("dummy");
    }
    
    @Test
    public void whenCallGetOrCreateItem_thenCallWikibaseOperations() throws TripleStoreException {
        MonolingualTextValue label = new MonolingualTextValueImpl("dummyLabel","es");
        
        wikibaseTemplateCacheDecorator.getOrCreateItem(label);
        
        verify(wikibaseTemplate).getOrCreateItem(label);
    }
    
    @Test
    public void whenCallGetOrCreateProperty_thenCallWikibaseOperations() throws TripleStoreException {
        MonolingualTextValue label = new MonolingualTextValueImpl("dummyLabel","es");
        MonolingualTextValue description = new MonolingualTextValueImpl("dummyDescription","es");
        String type = "dummyType";
        
        wikibaseTemplateCacheDecorator.getOrCreateProperty(label, description, type);
        
        verify(wikibaseTemplate).getOrCreateProperty(label, description, type);
    }
    
    @Test
    public void whenCallInsert_thenCallWikibaseOperations() throws TripleStoreException {
        ItemDocument itemDocument = mock(ItemDocument.class);
        
        wikibaseTemplateCacheDecorator.insert(itemDocument);
        
        verify(wikibaseTemplate).insert(itemDocument);
    }
    
    @Test
    public void whenCallReplace_thenCallWikibaseOperations() throws TripleStoreException {
        ItemDocument itemDocument = mock(ItemDocument.class);
        
        wikibaseTemplateCacheDecorator.replace(itemDocument);
        
        verify(wikibaseTemplate).replace(itemDocument);
    }
}
