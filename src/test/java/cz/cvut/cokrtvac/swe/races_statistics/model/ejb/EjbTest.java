package cz.cvut.cokrtvac.swe.races_statistics.model.ejb;

import cz.cvut.cokrtvac.swe.races_statistics.model.entity.ScoreSheetEntity;
import cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb.GenericDAO;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

@RunWith(Arquillian.class)
public class EjbTest {

    @Inject
    private GenericDAO dao;

    @Deployment
    public static Archive<?> getDeployment() {
        WebArchive arch = ShrinkWrap.create(WebArchive.class)
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/RacesStatistics-ds.xml"))
                .addAsResource(new File("src/main/resources"), "")
                .addPackages(true, "cz.cvut.cokrtvac.swe.races_statistics.model")
                .addPackages(true, "cz.cvut.cokrtvac.swe.races_statistics.utils");

        arch.addAsLibraries(Maven.resolver()
                .loadPomFromFile("pom.xml").resolve("org.apache.jena:jena-arq")
                .withTransitivity().asFile()
        );

        arch.addAsLibraries(Maven.resolver()
                .loadPomFromFile("pom.xml").resolve("net.sourceforge.htmlcleaner:htmlcleaner")
                .withTransitivity().asFile()
        );

        System.out.println(arch.toString(true));
        return arch;
    }

    @Test
    public void testLoader() {
        System.out.println("Num of score-sheets: " + dao.getAllScoreSheets().size());

        for(ScoreSheetEntity e : dao.getAllScoreSheets()){
            System.out.println(e.getId());
        }
    }

}
