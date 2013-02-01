package cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import cz.cvut.cokrtvac.swe.races_statistics.model.entity.*;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;


@Named
@Stateful
public class OntologyCreator {
    public static String FORMAT = "TURTLE";

    public static final String BASE = "http://www.semanticweb.org/ontologies/2012/cokrtvac/racesStatistics.owl#";

    private OntModel model;
    private OntClass man;
    private OntClass woman;

    @Inject
    private GenericDAO dao;

    public void createOntology() throws IOException {
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);

        InputStream is = new ByteArrayInputStream(dao.getOntologyTemplate().getBytes());
        model.read(is, "http://www.semanticweb.org/ontologies/2012/cokrtvac/racesStatistics.owl", FORMAT);
        is.close();

        man = model.getOntClass(BASE + "MaleGender");
        woman = model.getOntClass(BASE + "FemaleGender");

        loadRunners();
        loadCategories();
        loadRaces();

        Reasoner reasoner = ReasonerRegistry.getRDFSSimpleReasoner();
        InfModel inf = ModelFactory.createInfModel(reasoner, model);
        ValidityReport report = inf.validate();
        if(report.isValid()){
            System.out.println("OK");
        } else {
            System.out.println("Conflicts");
            for (Iterator i = report.getReports(); i.hasNext(); ) {
                System.out.println(" - " + i.next());
            }
        }

        try {
           // PrintStream pp = new PrintStream("D:/outpuOnto.rdf");
           // FileOutputStream fos = new FileOutputStream("D:/outpuOnto.rdf");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(baos, "utf-8");
            inf.write(osw);

            byte[] res = baos.toByteArray();
            dao.saveFullOntology(new String(res, "utf-8"));

            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("DONE");
    }

    private void loadRunners() {
        OntClass personC = model.getOntClass(BASE + "Person");
        OntClass addressC = model.getOntClass(BASE + "Address");
        OntClass clubC = model.getOntClass(BASE + "Club");
        OntClass runnerC = model.getOntClass(BASE + "Runner");

        for (PersonEntity p : dao.getAll(PersonEntity.class)) {
            Individual person = personC.createIndividual(BASE + p.createId());
            person.addProperty(model.getProperty(BASE + "firstname"), p.getFirstname());
            person.addProperty(model.getProperty(BASE + "surname"), p.getSurname());
            person.addLiteral(model.getProperty(BASE + "birthdate"), p.getBirthdate());
            if (p.getGender()) {
                person.addProperty(model.getProperty(BASE + "hasGender"), man);
            } else {
                person.addProperty(model.getProperty(BASE + "hasGender"), woman);
            }

        }

        for (ClubEntity c : dao.getAll(ClubEntity.class)) {
            Individual club = clubC.createIndividual(BASE + c.createId());
            club.addProperty(model.getProperty(BASE + "clubName"), c.getName());
        }

        for (CityEntity c : dao.getAll(CityEntity.class)) {
            Individual addr = addressC.createIndividual(BASE + c.createId());
            addr.addProperty(model.getProperty(BASE + "city"), c.getName());
        }

        for (RunnerEntity r : dao.getAll(RunnerEntity.class)) {
            Individual runner = runnerC.createIndividual(BASE + r.createId());
            runner.addProperty(model.getProperty(BASE + "isPerson"), model.getIndividual(BASE + r.getPerson().createId()));
            if (!r.getClub().equals("-")) {
                runner.addProperty(model.getProperty(BASE + "runFor"), model.getIndividual(BASE + r.getClub().createId()));
            }
            if (!r.getCity().equals("-")) {
                runner.addProperty(model.getProperty(BASE + "isFrom"), model.getIndividual(BASE + r.getCity().createId()));
            }

            Calendar cal = new GregorianCalendar();
            cal.setTime(r.getTime());
            int s = cal.get(Calendar.HOUR) * 60 * 60 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);

            runner.addLiteral(model.getProperty(BASE + "time"), s);
            runner.addLiteral(model.getProperty(BASE + "startNumber"), r.getStartNumber());
        }
    }

    private void loadCategories() {
        OntClass catC = model.getOntClass(BASE + "Category");
        for (CategoryEntity c : dao.getAll(CategoryEntity.class)) {
            Individual cat = catC.createIndividual(BASE + c.createId());
            cat.addLiteral(model.getProperty(BASE + "ageFrom"), c.getFromAge());
            cat.addLiteral(model.getProperty(BASE + "ageTo"), c.getToAge());
            cat.addProperty(model.getProperty(BASE + "categoryName"), c.getName());


            for (RunnerEntity r : c.getRunners()) {
                cat.addProperty(model.getProperty(BASE + "containsRunner"), model.getIndividual(BASE + r.createId()));
            }
        }
    }

    private void loadRaces() {
        OntClass seriesC = model.getOntClass(BASE + "RaceSeries");
        OntClass raceC = model.getOntClass(BASE + "Race");
        OntClass dateC = model.getOntClass(BASE + "Date");

        for (RaceEntity e : dao.getAll(RaceEntity.class)) {
            Individual series = seriesC.createIndividual(BASE + e.createId());
            series.addProperty(model.getProperty(BASE + "raceName"), e.getName());
            for (RaceVolumeEntity ve : e.getRaces()) {
                Individual race = raceC.createIndividual(BASE + ve.createId());
                race.addLiteral(model.getProperty(BASE + "volume"), ve.getVolume());

                Calendar cal = new GregorianCalendar();
                cal.setTime(ve.getDate());
                int y = cal.get(Calendar.YEAR);
                int m = cal.get(Calendar.MONTH) + 1;
                int d = cal.get(Calendar.DAY_OF_MONTH);

                Individual date = dateC.createIndividual(BASE + new SimpleDateFormat("yyyy:MM:dd").format(cal.getTime()));
                date.addLiteral(model.getProperty(BASE + "year"), y);
                date.addLiteral(model.getProperty(BASE + "month"), m);
                date.addLiteral(model.getProperty(BASE + "day"), d);

                race.addProperty(model.getProperty(BASE + "date"), date);

                race.addProperty(model.getProperty(BASE + "isInSeries"), series);
                // TODO

                for (RunnerEntity r : ve.getRunners()) {
                    race.addProperty(model.getProperty(BASE + "hasRunner"), model.getIndividual(BASE + r.createId()));
                }

                for (CategoryEntity ce : ve.getCategories()) {
                    race.addProperty(model.getProperty(BASE + "hasCategory"), model.getIndividual("BASE" + ce.createId()));
                }
            }

        }

    }

}
