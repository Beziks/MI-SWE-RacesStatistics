package cz.cvut.cokrtvac.swe.races_statistics.model.entity;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name="Person")
public class PersonEntity implements Similarible<PersonEntity> {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	private String firstname;
	private String surname;
	private Integer birthdate;
	private Boolean man;

	
	
	public PersonEntity() {
		super();
	}

	public PersonEntity(String firstname, String surname, Integer birthdate, Boolean man) {
		this.firstname = firstname;
		this.surname = surname;
		this.birthdate = birthdate;
		this.man = man;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Integer getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Integer birthdate) {
		this.birthdate = birthdate;
	}

	public Boolean getGender() {
		return man;
	}

	public void setGender(Boolean man) {
		this.man = man;
	}

	/*public boolean isPossibleEquals(PersonEntity p) {
		if (Math.abs(getBirthdate() - p.getBirthdate()) > 2) {
			return false;
		}

		int f1 = StringUtils.getLevenshteinDistance(getFirstname(),
				p.getFirstname(), 2);
		int s1 = StringUtils.getLevenshteinDistance(getSurname(),
				p.getSurname(), 2);
		if (f1 != -1 && s1 != -1) {
			return true;
		}

		int f2 = StringUtils.getLevenshteinDistance(getFirstname(),
				p.getSurname(), 2);
		int s2 = StringUtils.getLevenshteinDistance(getSurname(),
				p.getFirstname(), 2);
		if (f2 != -1 && s2 != -1) {
			return true;
		}

		return false;
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
		PersonEntity other = (PersonEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PersonEntity [id=" + id + ", firstname=" + firstname + ", surname=" + surname + ", birthdate=" + birthdate + "]";
	}
	
	public boolean isPossibleEquals(PersonEntity p) {
		if (Math.abs(getBirthdate() - p.getBirthdate()) > 0) {
			return false;
		}

		int f1 = StringUtils.getLevenshteinDistance(getFirstname(),
				p.getFirstname(), 0);
		int s1 = StringUtils.getLevenshteinDistance(getSurname(),
				p.getSurname(), 0);
		if (f1 != -1 && s1 != -1) {
			return true;
		}

		int f2 = StringUtils.getLevenshteinDistance(getFirstname(),
				p.getSurname(), 0);
		int s2 = StringUtils.getLevenshteinDistance(getSurname(),
				p.getFirstname(), 0);
		if (f2 != -1 && s2 != -1) {
			return true;
		}

		return false;
	}

	public String createId(){
		return (getFirstname() + getSurname() + getBirthdate()).replace(" ", "_");
	}
	
}
