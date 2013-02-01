package cz.cvut.cokrtvac.swe.races_statistics.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="score_sheet")
public class ScoreSheetEntity {
	
	public ScoreSheetEntity() {
		super();
	}

	public ScoreSheetEntity(String id, String content) {
		super();
		this.id = id;
		this.content = content;
	}

	@Id
	private String id;
	
	@Lob
	private String content;

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
