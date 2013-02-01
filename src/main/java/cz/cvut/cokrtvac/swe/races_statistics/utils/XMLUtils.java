package cz.cvut.cokrtvac.swe.races_statistics.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import java.io.StringWriter;

public class XMLUtils {
	private static Logger LOG = LoggerFactory.getLogger(XMLUtils.class);

	public static String lsSerializePretty(Document doc) {		
		DOMImplementation domImplementation = doc.getImplementation();
		if (domImplementation.hasFeature("LS", "3.0") && domImplementation.hasFeature("Core", "2.0")) {
			DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementation.getFeature("LS", "3.0");
			LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
			DOMConfiguration domConfiguration = lsSerializer.getDomConfig();
			if (domConfiguration.canSetParameter("format-pretty-print", Boolean.TRUE)) {
				lsSerializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
				LSOutput lsOutput = domImplementationLS.createLSOutput();
				lsOutput.setEncoding("UTF-8");
				StringWriter stringWriter = new StringWriter();
				lsOutput.setCharacterStream(stringWriter);
				lsSerializer.write(doc, lsOutput);
				return stringWriter.toString();
			} else {
				throw new RuntimeException("DOMConfiguration 'format-pretty-print' parameter isn't settable.");
			}
		} else {
			throw new RuntimeException("DOM 3.0 LS and/or DOM 2.0 Core not supported.");
		}
	}

	public static String lsSerialize(Document doc) {
		DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
		LSSerializer lsSerializer = domImplementation.createLSSerializer();
		return lsSerializer.writeToString(doc);
	}
	
}
