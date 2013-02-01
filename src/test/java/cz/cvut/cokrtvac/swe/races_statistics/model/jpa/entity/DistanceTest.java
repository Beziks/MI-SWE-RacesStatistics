package cz.cvut.cokrtvac.swe.races_statistics.model.jpa.entity;

import cz.cvut.cokrtvac.swe.races_statistics.model.entity.ClubEntity;
import cz.cvut.cokrtvac.swe.races_statistics.model.entity.PersonEntity;
import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

	@Test
	public void testPersonDistatance(){
		PersonEntity p1 = new PersonEntity("Václav", "Čokrt", 1989, true);
		PersonEntity p2 = new PersonEntity("Vaclav", "Cokrt", 1988, true);
		PersonEntity p3 = new PersonEntity("Cokrta", "Vaclav", 1989, true);
		PersonEntity p4 = new PersonEntity("Cokrtaa", "Vaclav", 1989, true);
		PersonEntity p5 = new PersonEntity("Václav", "Čokrt", 1985, true);
		
		
		Assert.assertTrue(p1.isPossibleEquals(p2));
		Assert.assertTrue(p2.isPossibleEquals(p1));
		Assert.assertTrue(p1.isPossibleEquals(p3));
		Assert.assertFalse(p1.isPossibleEquals(p4));
		Assert.assertFalse(p1.isPossibleEquals(p5));
	}
	
	@Test
	public void testClubDistatance(){
		ClubEntity c1 = new ClubEntity("TTC Cesky Brod");
		ClubEntity c2 = new ClubEntity("Triatlon Cesky Brod");
		ClubEntity c3 = new ClubEntity("AC Sparta Praha");
		
		
		Assert.assertTrue(c1.isPossibleEquals(c2));
		Assert.assertFalse(c1.isPossibleEquals(c3));
	}
}
