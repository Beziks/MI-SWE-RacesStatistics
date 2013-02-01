package cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb;

import cz.cvut.cokrtvac.swe.races_statistics.model.entity.*;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Stateful
public class ScoreSheetParser {

	@PersistenceContext
	private EntityManager em;

    @Inject
    private GenericDAO dao;

	private HtmlCleaner cleaner = new HtmlCleaner();
	private RaceEntity raceEntity;
	private RaceVolumeEntity raceVolume;

	private boolean man;

	public void parseScoreSheet(String id) throws XPatherException, ParseException {
		ScoreSheetEntity scoreSheet = em.find(ScoreSheetEntity.class, id);

		TagNode html = cleaner.clean(scoreSheet.getContent());

		// Race ------------------------------------------
		String name = ((TagNode) html.evaluateXPath("//body//h1")[0]).getText().toString();
		RaceEntity r = new RaceEntity();
		r.setName(name);
		raceEntity = (RaceEntity) checkPossibleMatches(r, RaceEntity.class);

		// RaceVolume ------------------------------------------
		RaceVolumeEntity rv = new RaceVolumeEntity();

		String dateStr = ((TagNode) html.evaluateXPath("//body//div[@class='date']")[0]).getText().toString();
		Date d = new SimpleDateFormat("dd. MM. yyyy").parse(dateStr);
		rv.setDate(d);

		rv.setRace(raceEntity);

		String vol = ((TagNode) html.evaluateXPath("//body//div[@class='volume']")[0]).getText().toString();
		vol = vol.substring(0, vol.indexOf("."));
		rv.setVolume(Integer.valueOf(vol));
		raceVolume = (RaceVolumeEntity) checkPossibleMatches(rv, RaceVolumeEntity.class);

		man = true;
		TagNode menDiv = ((TagNode) html.evaluateXPath("//body//div[@id='men']")[0]);
		Object[] cats = menDiv.evaluateXPath("//table");
		for (int i = 0; i < cats.length; i++) {
			TagNode c = (TagNode) cats[i];
			processCategory(c);
		}

		man = false;
		TagNode womenDiv = ((TagNode) html.evaluateXPath("//body//div[@id='women']")[0]);
		cats = womenDiv.evaluateXPath("//table");
		for (int i = 0; i < cats.length; i++) {
			TagNode c = (TagNode) cats[i];
			processCategory(c);
		}
	}

	private void processCategory(TagNode table) throws XPatherException, ParseException {
		String categoryStr = ((TagNode) table.evaluateXPath("./caption")[0]).getText().toString().trim();
		CategoryEntity cat = new CategoryEntity();
		cat.setMan(man);
		cat.setName(categoryStr);
		cat.getRaces().add(raceVolume);

		if (categoryStr.contains("39")) {
			cat.setFromAge(0);
			cat.setToAge(39);
		} else if (categoryStr.contains("70")) {
			cat.setFromAge(70);
			cat.setToAge(1000);
		} else if (categoryStr.contains("-")) {
			int ind = categoryStr.indexOf("-");
			String from = categoryStr.substring(ind - 2, ind);
			String to = categoryStr.substring(ind, ind + 2);
			cat.setFromAge(Integer.valueOf(from));
			cat.setToAge(Integer.valueOf(to));
		} else if (categoryStr.contains("34")) {
			cat.setFromAge(0);
			cat.setToAge(34);
		} else if (categoryStr.contains("45")) {
			cat.setFromAge(45);
			cat.setToAge(1000);
		} else {
			throw new IllegalArgumentException("Category cannot be processed");
		}

		cat = (CategoryEntity) checkPossibleMatches(cat, cat.getClass());

		Object[] runners = table.evaluateXPath("/tbody/tr");

		for (int i = 0; i < runners.length; i++) {
			TagNode row = (TagNode) runners[i];

			// Person -------------
			String name = ((TagNode) row.evaluateXPath("/td[@class='jmeno']")[0]).getText().toString().trim();

			int index = name.indexOf(" ");
			String sn = name.substring(0, index).trim();
			String fn = name.substring(index).trim();

			String bd = ((TagNode) row.evaluateXPath("/td[@class='rn']")[0]).getText().toString().trim();

			PersonEntity person = new PersonEntity(fn, sn, Integer.valueOf(bd), man);
			person = (PersonEntity) checkPossibleMatches(person, PersonEntity.class);

			// Club and city -------------
			String all = ((TagNode) row.evaluateXPath("/td[@class='klub']")[0]).getText().toString().trim();

			int delI = all.indexOf("(");

			String clubStr;
			String cityStr;
			if (delI >= 0) {
				clubStr = all.substring(0, delI).trim();
				cityStr = all.substring(delI);
				cityStr = cityStr.replace("(", "").replace(")", "").trim();
			} else {
				clubStr = all.trim();
				cityStr = "";
			}

			if (clubStr.isEmpty()) {
				clubStr = "-";
			}
			if (cityStr.isEmpty()) {
				cityStr = "-";
			}
			
			ClubEntity club = new ClubEntity(clubStr);
			club = (ClubEntity) checkPossibleMatches(club, club.getClass());
			
			CityEntity city = new CityEntity(cityStr);
			city = (CityEntity) checkPossibleMatches(city, city.getClass());
			
			// Number -------------
			Integer number = Integer.valueOf(((TagNode) row.evaluateXPath("/td[@class='stCislo']")[0]).getText().toString().trim());
			
			// Time -------------
			String timeStr = ((TagNode) row.evaluateXPath("/td[@class='cas']")[0]).getText().toString().trim();
			if(timeStr.equalsIgnoreCase("NF")){
				timeStr = "23:59:59";
			}
			Date time = new SimpleDateFormat("H:mm:ss").parse(timeStr);
			
			// Runner -------------
			RunnerEntity runner = new RunnerEntity(number, person, club, city, time);
			runner.setCategory(cat);
			runner.setRace(raceVolume);
			checkPossibleMatches(runner, runner.getClass());
		}

	}

	private <T> Similarible<T> checkPossibleMatches(Similarible<T> e, Class clazz) {
		List<Similarible<T>> list = getAll(clazz);

		for (Similarible<T> s : list) {
			if (!e.isPossibleEquals((T) s)) {
				continue;
			}
			return s;
		}

		em.persist(e);
		return e;
	}

	public <T extends Serializable> List<T> getAll(Class<T> clazz) {
		return dao.getAll(clazz);
	}
}
