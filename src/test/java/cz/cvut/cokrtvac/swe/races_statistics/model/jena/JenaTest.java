package cz.cvut.cokrtvac.swe.races_statistics.model.jena;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URL;

public class JenaTest {
	public static String format = "TURTLE";
	// public static String path = "file:resources/hello.rdf";
	public static final String base = "http://www.semanticweb.org/ontologies/2012/cokrtvac/racesStatistics.owl#";

	@Test
	public void test() throws FileNotFoundException {
		OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);

        URL url = getClass().getClassLoader().getResource("racesStatistics-old.rdf");
		m.read(url.toString(), format);
		// Vsechny podtridy tridy swe:Course
		OntClass c = m.getOntClass("http://www.semanticweb.org/ontologies/2012/cokrtvac/racesStatistics.owl#Gender");
		ExtendedIterator<OntClass> it = c.listSubClasses();
		while (it.hasNext()) {
			System.out.println(it.next().getLocalName());
		}

		System.out.println("=========================================");

		OntClass person = m.getOntClass(base + "Person");
		Individual p = person.createIndividual(base + "VaclavCokrt1989");
		p.addProperty(m.getProperty(base + "firstname"), "Vaclav");
		p.addProperty(m.getProperty(base + "birthdate"), "1989");
		p.addProperty(m.getProperty(base + "surname"), "Cokrt");

		try {
			PrintStream pp = new PrintStream("outpuOnto.rdf");
			m.write(pp);
			pp.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
