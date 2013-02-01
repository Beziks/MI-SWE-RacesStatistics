package cz.cvut.cokrtvac.swe.races_statistics.model.entity;

import javax.persistence.*;

@Entity
@Table(name="club")
public class ClubEntity implements Similarible<ClubEntity> {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	private String name;

	
	public ClubEntity() {
		super();
	}

	public ClubEntity(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public boolean isPossibleEquals(ClubEntity c) {
		if (getName().equalsIgnoreCase(c.getName())) {
			return true;
		}

		if (StringUtils.getLevenshteinDistance(getName(), c.getName(), 3) != -1) {
			return true;
		}

		if (this.getName().toLowerCase().contains(c.getName().toLowerCase()) 
				|| c.getName().toLowerCase().contains(this.getName().toLowerCase())) {
			return true;
		}

		String[] ss1 = getName().split(" ");
		String[] ss2 = c.getName().split(" ");

		if(ss1.length <= 1 || ss2.length <= 1){
			return false;
		}
		
		int diff1 = 0;
		for (int i = 0; i < ss1.length; i++) {
			if (!c.getName().toUpperCase().contains(ss1[i].trim().toUpperCase())) {
				diff1++;
			}
		}

		if (diff1 > 1) {
			return false;
		}

		int diff2 = 0;
		for (int i = 0; i < ss2.length; i++) {
			if (!this.getName().toUpperCase().contains(ss2[i].trim().toUpperCase())) {
				diff2++;
			}
		}

		if (diff2 > 1) {
			return false;
		}

		return true;
	}*/

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
		ClubEntity other = (ClubEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClubEntity [id=" + id + ", name=" + name + "]";
	}	
	
	@Override
	public boolean isPossibleEquals(ClubEntity c) {
		if(getName().trim().equalsIgnoreCase(c.getName().trim())){
			return true;
		}
		return false;
	}
	
	public String createId(){
		return "Club_" + getName().replace(" ", "_");
	}
}
