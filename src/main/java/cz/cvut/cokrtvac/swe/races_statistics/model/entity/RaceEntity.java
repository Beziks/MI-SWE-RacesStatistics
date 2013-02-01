package cz.cvut.cokrtvac.swe.races_statistics.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="race")
public class RaceEntity implements Similarible<RaceEntity> {
	@Id	
	private String name;	
	@OneToMany(mappedBy="race", cascade = CascadeType.ALL)
	private List<RaceVolumeEntity> races = new ArrayList<RaceVolumeEntity>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RaceVolumeEntity> getRaces() {
		return races;
	}

	public void setRaces(List<RaceVolumeEntity> races) {
		this.races = races;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		RaceEntity other = (RaceEntity) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public boolean isPossibleEquals(RaceEntity c) {
		if(c.getName().trim().equalsIgnoreCase(getName().trim())){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "RaceEntity [name=" + name + "]";
	}
	
	public String createId(){
		return getName().replace(" ", "_");
	}
}
