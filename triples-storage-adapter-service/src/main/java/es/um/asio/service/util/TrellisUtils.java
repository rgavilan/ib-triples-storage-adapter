package es.um.asio.service.util;

import java.io.StringReader;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.springframework.stereotype.Component;

@Component
public class TrellisUtils {
	/**
	 * To object.
	 *
	 * @param strModel the str model
	 * @return the model
	 */
	public  Model toObject(String strModel) {
		StringReader stringReader = new StringReader(strModel);
		
		Model modelFromString = ModelFactory.createDefaultModel();
		RDFDataMgr.read(modelFromString, stringReader, null, RDFLanguages.RDFXML);
		
		return modelFromString;
	}
}
