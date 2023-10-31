package com.gianfro.games.entities;

import java.util.List;

import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChangeLog {

	List<Integer> unitExamined;
	@Nullable House house;
	@Nullable int houseNumber;
	List<ChangeLogUnitMember> unitMembers;
	String solvingTechnique;
	@Nullable String solvingTechniqueVariant;
	List<Change> changes;
	
	@Override
	public String toString() {
		return solvingTechniqueVariant != null ? solvingTechniqueVariant : solvingTechnique;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ChangeLog) {
			ChangeLog cl = (ChangeLog)obj;
			return cl.changes.equals(this.changes);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 1;
	}
}
