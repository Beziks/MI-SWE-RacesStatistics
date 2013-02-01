package cz.cvut.cokrtvac.swe.races_statistics.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "runner",
		uniqueConstraints = @UniqueConstraint(columnNames = { "startNumber", "race_id" }))
public class RunnerEntity implements Similarible<RunnerEntity> {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	private Integer startNumber;
	@ManyToOne
	private PersonEntity person;
	@ManyToOne
	private ClubEntity club;
	@ManyToOne
	private CityEntity city;
	private Date time;

	@ManyToOne
	private CategoryEntity category;
	@ManyToOne
	private RaceVolumeEntity race;

	public RunnerEntity() {
		super();
	}

	public RunnerEntity(Integer startNumber, PersonEntity person, ClubEntity club, CityEntity city, Date time) {
		super();
		this.startNumber = startNumber;
		this.person = person;
		this.club = club;
		this.city = city;
		this.time = time;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public RaceVolumeEntity getRace() {
		return race;
	}

	public void setRace(RaceVolumeEntity race) {
		this.race = race;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public Integer getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(Integer startNumber) {
		this.startNumber = startNumber;
	}

	public PersonEntity getPerson() {
		return person;
	}

	public void setPerson(PersonEntity person) {
		this.person = person;
	}

	public ClubEntity getClub() {
		return club;
	}

	public void setClub(ClubEntity club) {
		this.club = club;
	}

	public CityEntity getCity() {
		return city;
	}

	public void setCity(CityEntity city) {
		this.city = city;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RunnerEntity other = (RunnerEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RunnerEntity [startNumber=" + startNumber + ", person=" + person.getFirstname() + " " + person.getSurname() + ", club=" + club.getName() + ", city=" + city.getName() + ", time=" + time + "]";
	}

	@Override
	public boolean isPossibleEquals(RunnerEntity c) {
		if(getStartNumber().equals(c.getStartNumber()) && getRace().equals(c.getRace())){
			return true;
		}
		return false;
	}
	
	public String createId(){
		return "Runner_" + getId() + "_" + getStartNumber();
	}
}
