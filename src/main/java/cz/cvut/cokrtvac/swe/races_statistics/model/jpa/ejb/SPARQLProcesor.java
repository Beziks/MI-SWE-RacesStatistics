package cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.*;


@Stateless
@Named("sparql")
public class SPARQLProcesor {

    @Inject
    public GenericDAO dao;

    public static String FORMAT = "RDF/XML";

    public String resolve(String query) throws UnsupportedEncodingException, IOException {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);

        StringReader sr = new StringReader(dao.getFullOntology());

        model.read(sr, "http://www.semanticweb.org/ontologies/2012/cokrtvac/racesStatistics.owl", FORMAT);

        sr.close();

        String queryString =
                "#BASE <http://www.semanticweb.org/ontologies/2012/cokrtvac/racesStatistics.owl#>\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "PREFIX owl2xml: <http://www.w3.org/2006/12/owl2-xml#>\n" +
                        "PREFIX : <http://www.semanticweb.org/ontologies/2012/cokrtvac/racesStatistics.owl#>\n" +
                        "\n" + query;

        QueryExecution qexec = QueryExecutionFactory.create(queryString, model);

        StringBuffer buff = new StringBuffer();

        int cnt = 0;

        try {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.next();

                buff.append(sol.toString().replaceAll("http://www.semanticweb.org/ontologies/2012/cokrtvac/racesStatistics.owl", "") + "\n");
                cnt ++;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                qexec.close();
            } catch (Exception e) {

            }
        }

        System.out.println("OK");

        return "Number of results: " + cnt + "\n\n" + buff.toString();
    }

    public Map<String, String> getQueries() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Select all persons",
                "SELECT ?person \n" +
                        "WHERE {\n" +
                        "\t?person rdf:type :Person\n" +
                        "}\n"
        );

        map.put("Select men only",
                "SELECT ?person \n" +
                        "WHERE {\n" +
                        "\t?person rdf:type :Person .\n" +
                        "\t?person :hasGender :MaleGender .\n" +
                        "}\n"
        );

        map.put("Select women only",
                "SELECT ?person \n" +
                        "WHERE {\n" +
                        "\t?person rdf:type :Person .\n" +
                        "\t?person :hasGender :FemaleGender .\n" +
                        "}\n"
        );

        map.put("Best runner ever",
                "SELECT DISTINCT ?runner ?person ?time ?race\n" +
                        "WHERE {\n" +
                        "\t?runner rdf:type :Runner .\n" +
                        "\t?runner :time  ?time .\n" +
                        "\t?runner :isPerson ?person .\n" +
                        "\t ?race :hasRunner ?runner .\n" +
                        "\tOPTIONAL {\n" +
                        "\t\t?faster rdf:type :Runner . \n" +
                        "\t\t?faster :time ?ft . \n" +
                        "\t\tFILTER (?ft < ?time) \n" +
                        "\t}\n" +
                        "\tFILTER (!bound (?faster)) \n" +
                        "}\n"
        );

        map.put("Best female runner ever",
                "SELECT DISTINCT ?runner ?person ?time ?race\n" +
                        "WHERE {\n" +
                        "\t?runner rdf:type :Runner .\n" +
                        "\t?runner :time  ?time .\n" +
                        "\t?runner :isPerson ?person .\n" +
                        "\t ?race :hasRunner ?runner .\n" +
                        "\t ?person :hasGender :FemaleGender .\n" +
                        "\tOPTIONAL {\n" +
                        "\t\t?faster rdf:type :Runner . \n" +
                        "\t\t?faster :time ?ft . \n" +
                        "\t\tFILTER (?ft < ?time) \n" +
                        "\t}\n" +
                        "\tFILTER (!bound (?faster)) \n" +
                        "}\n"
        );

        return map;
    }

    public List<String> getQueriesIds(){
        List<String> out = new ArrayList<String>(getQueries().keySet());
        Collections.sort(out);
        return out;
    }
}
