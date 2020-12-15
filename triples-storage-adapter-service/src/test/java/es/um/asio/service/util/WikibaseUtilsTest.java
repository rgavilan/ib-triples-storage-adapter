package es.um.asio.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.wikidata.wdtk.datamodel.implementation.MonolingualTextValueImpl;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;


@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class WikibaseUtilsTest {
	
	@Autowired
	private WikibaseUtils wikibaseUtils;
	
	@Value("${app.wikibase.query.default-language}")
    private String defaultLanguage;
	
	
	@TestConfiguration
	static class WikibaseUtilTestConfig {
		@Bean
		WikibaseUtils wikibaseUtils() {
			return new WikibaseUtils();
		}
	}
	
	@Test
	public void createMonolingualTextValue() {
		String text = "text";
		MonolingualTextValue result = wikibaseUtils.createMonolingualTextValue("text");
		assertNotNull(result);
		assertEquals(result.getText(), text);
	}
	
	@Test
	public void sanitizePropertyValue() {
		String textSanitized = "text";
		String text = textSanitized + "\n" ;
		String result = wikibaseUtils.sanitizePropertyValue("text");
		assertNotNull(result);
		assertEquals(result, textSanitized);
	}

}
