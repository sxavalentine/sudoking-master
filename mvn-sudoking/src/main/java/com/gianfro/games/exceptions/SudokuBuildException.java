package com.gianfro.games.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class SudokuBuildException extends RuntimeException {

	static final long serialVersionUID = 1L;
	String sudokuString;

	@Override
	public String getMessage() {
		return "The string " + sudokuString + " does not match the correct pattern to build a Sudoku (81 characters between 0 and 9)";
	}
}
