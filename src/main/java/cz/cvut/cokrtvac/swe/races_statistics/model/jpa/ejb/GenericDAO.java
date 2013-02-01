package cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb;

import cz.cvut.cokrtvac.swe.races_statistics.model.entity.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 * User: Vaclav Cokrt
 * Email: beziks@gmail.com
 * Date: 31.1.13
 * Time: 22:03
 */
@Stateless
public class GenericDAO {
    @PersistenceContext
    private EntityManager em;

    public static String PATH_TEMPLATE = "/racesStatistics.rdf";
    //public static String PATH_FULL = "D:/outpuOnto.rdf";
    public static String RDF_FULL_ONTOLOGY = "RDF_FULL_ONTOLOGY";

    public <T extends Object> List<T> getAll(Class<T> clazz) {
        CriteriaBuilder b = em.getCriteriaBuilder();
        CriteriaQuery<T> q = b.createQuery(clazz);
        Root<T> r = q.from(clazz);
        q.select(r);
        return em.createQuery(q).getResultList();
    }

    public void deleteAll() {
        for (Class clazz : getAllTables()) {
            delete(clazz);
        }
    }

    private void delete(Class entity) {
        for (Object o : getAll(entity)) {
            em.remove(o);
        }
    }

    public String getOntologyTemplate() throws IOException {
        InputStream is = getClass().getResourceAsStream(PATH_TEMPLATE);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            int b = -1;
            while ((b = is.read()) >= 0) {
                bos.write(b);
            }
            return new String(bos.toString("UTF-8"));
        } finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
            }
        }
    }



    private List<Class> getAllTables() {
        List<Class> l = new ArrayList<Class>();
        l.add(RaceVolumeEntity.class);
        l.add(RaceEntity.class);
        l.add(CategoryEntity.class);
        l.add(RunnerEntity.class);
        l.add(ClubEntity.class);
        l.add(PersonEntity.class);
        l.add(CityEntity.class);
        return l;
    }

    public void remove(Object entity) {
        entity = em.merge(entity);
        em.remove(entity);
    }

    public boolean save(Object entity) {
        if (em.contains(entity)) {
            em.persist(entity);
            return true;
        }

        em.merge(entity);
        return false;
    }

    public boolean addScoreSheet(String name, String content) {
        ScoreSheetEntity e = em.find(ScoreSheetEntity.class, name);

        if (e != null) {
            e.setContent(content);
            em.merge(e);
            return false;
        }

        e = new ScoreSheetEntity(name, content);
        em.persist(e);
        return true;
    }

    public List<ScoreSheetEntity> getAllScoreSheets() {
        List<ScoreSheetEntity> all = new ArrayList<ScoreSheetEntity>();
        for(ScoreSheetEntity e : getAll(ScoreSheetEntity.class)){
            if(!e.getId().equals(RDF_FULL_ONTOLOGY)){
                all.add(e);
            }
        }
        return all;
    }

    public void saveFullOntology(String content){
        addScoreSheet(RDF_FULL_ONTOLOGY, content);
    }

    public String getFullOntology() {
        return em.find(ScoreSheetEntity.class, RDF_FULL_ONTOLOGY).getContent();
        //return FileUtils.readFile(PATH_FULL);
    }

}
