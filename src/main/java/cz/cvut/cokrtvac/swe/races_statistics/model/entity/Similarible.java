package cz.cvut.cokrtvac.swe.races_statistics.model.entity;

import java.io.Serializable;

public interface Similarible<T> extends Serializable {
	public boolean isPossibleEquals(T c);
}
