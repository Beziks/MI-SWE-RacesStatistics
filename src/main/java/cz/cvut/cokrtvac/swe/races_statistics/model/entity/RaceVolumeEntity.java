package cz.cvut.cokrtvac.swe.races_statistics.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="race_volume")
public class RaceVolumeEntity implements Similarible<RaceVolumeEntity>{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)	
	private Integer id;
	@ManyToOne
	private RaceEntity race;
	private Date date;
	private Integer volume;
	@ManyToMany
	private List<CategoryEntity> categories = new ArrayList<CategoryEntity>();
	@OneToMany(mappedBy="race", cascade = CascadeType.ALL)
	private List<RunnerEntity> runners;

	public List<CategoryEntity> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryEntity> categories) {
		this.categories = categories;
	}

	public List<RunnerEntity> getRunners() {
		return runners;
	}

	public void setRunners(List<RunnerEntity> runners) {
		this.runners = runners;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RaceEntity getRace() {
		return race;
	}

	public void setRace(RaceEntity race) {
		this.race = race;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	@Override
	public boolean isPossibleEquals(RaceVolumeEntity c) {		
		if(c.getRace().isPossibleEquals(getRace()) && c.getVolume() == getVolume()){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "RaceVolumeEntity [race=" + race.getName() + ", date=" + date + ", volume=" + volume + "]";
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
		RaceVolumeEntity other = (RaceVolumeEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String createId(){
		return getRace().createId() + "_vol" + getVolume();
	}
}
