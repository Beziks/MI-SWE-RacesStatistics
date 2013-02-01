package cz.cvut.cokrtvac.swe.races_statistics.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="category")
public class CategoryEntity implements Similarible<CategoryEntity> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	private String name;
	private Integer fromAge;
	private Integer toAge;
	private Boolean man;

	@OneToMany(mappedBy="category", cascade = CascadeType.ALL)
	private List<RunnerEntity> runners;

	@ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL)
	private List<RaceVolumeEntity> races = new ArrayList<RaceVolumeEntity>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<RunnerEntity> getRunners() {
		return runners;
	}

	public void setRunners(List<RunnerEntity> runners) {
		this.runners = runners;
	}

	public List<RaceVolumeEntity> getRaces() {
		return races;
	}

	public void setRaces(List<RaceVolumeEntity> races) {
		this.races = races;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFromAge() {
		return fromAge;
	}

	public void setFromAge(Integer fromAge) {
		this.fromAge = fromAge;
	}

	public Integer getToAge() {
		return toAge;
	}

	public void setToAge(Integer toAge) {
		this.toAge = toAge;
	}

	public Boolean getMan() {
		return man;
	}

	public void setMan(Boolean man) {
		this.man = man;
	}

	/*
	 * public boolean isPossibleEquals(CategoryEntity c) {
	 * if(getName().equalsIgnoreCase(c.getName())){
	 * return true;
	 * }
	 * 
	 * if(from == c.getFrom() && to == c.getTo() && man == c.man){
	 * return true;
	 * }
	 * return false;
	 * }
	 */

	public boolean isPossibleEquals(CategoryEntity c) {
		if (getName().equalsIgnoreCase(c.getName())) {
			return true;
		}

		if (fromAge == c.getFromAge() && toAge == c.getToAge() && man == c.man) {
			return true;
		}
		return false;
	}
	
	public String createId(){
		return "Cat_" + getName().replace(" ", "_");
	}
}
