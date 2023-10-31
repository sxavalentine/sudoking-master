package com.gianfro.games.entities;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gianfro.games.utils.Utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Data
@FieldDefaults(makeFinal =true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Change {
	
	@NonFinal String solvingTechnique;
	@Nullable @JsonInclude(Include.NON_NULL) House house;
	int row;
	int col;
	int number;
	
	@Override
	public String toString() {
		return 
			solvingTechnique + 
			(house != null ? " " + house : "") + ": " +
			Utils.ROWS_LETTERS.get(row - 1) + col + " --> " + number;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Change) {
			if (!(obj instanceof Skimming)) {
				Change c = (Change)obj;
				return c.row == this.row && c.col == this.col && c.number == this.number;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 1;
	}
}