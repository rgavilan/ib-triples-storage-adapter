package es.um.asio.delta.util;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.springframework.stereotype.Component;
import org.apache.jena.riot.Lang;

@Component
public class DeltaUtils {
	/**
	 * To object.
	 *
	 * @param strModel the str model
	 * @return the model
	 */
	public Model toObject(String strModel) {		
		return toObject(strModel, RDFLanguages.RDFXML);
	}
	
	/**
	 * To object.
	 *
	 * @param strModel the str model
	 * @param language the language
	 * @return the model
	 */
	public Model toObject(String strModel, Lang language) {
		StringReader stringReader = new StringReader(strModel);

		Model modelFromString = ModelFactory.createDefaultModel();
		RDFDataMgr.read(modelFromString, stringReader, null, language);

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
		String result = StringUtils.EMPTY;
		if (model != null) {
			String syntax = "RDF/XML-ABBREV";
			StringWriter out = new StringWriter();
			model.write(out, syntax);
			result = out.toString();
		}
		return result;
	}
	
}
