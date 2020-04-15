package es.um.asio.service.util;

import java.io.StringReader;
import java.io.StringWriter;

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
	
	/**
	 * Method to transform model to string
	 *
	 * @param model  the model
	 * @param format the format
	 * @return the string
	 */
	public static String toString(Model model) {
		String syntax = "RDF/XML-ABBREV";
		StringWriter out = new StringWriter();
		model.write(out, syntax);
		return out.toString();
	}
}
