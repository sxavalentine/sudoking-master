package com.gianfro.games.entities;

import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Sudoku cell.
 *
 * @field box         -> final, the row where the cell is set (1 based index)
 * @field row         -> final, the row where the cell is set (1 based index)
 * @field col         -> final, the column where the cell is set (1 based index)
 * @field index       -> final, the index of the cell (0 based index)
 * @field coordinates -> final, the alphanumeric coordinates of the cell (eg: A1, I9)
 * @field candidates  -> final, the numbers that can still be put as cell value (can't be re-assigned, but the content can change)
 * @field value       -> the value of the cell (0 if it's empty)
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"index"})
public class SudokuCell implements ChangeLogUnitMember {

    int box;
    int row;
    int col;
    int index;
    String coordinates;
    List<Integer> candidates;
    @NonFinal
    int value;

    private SudokuCell(int value, int row, int col, List<Integer> candidates) {
        this.value = value;
        this.row = row;
        this.col = col;
        box = ((row - 1) / 3) * 3 + ((col - 1) / 3) + 1;
        index = ((row - 1) * 9) + (col - 1);
        coordinates = Utils.ROWS_LETTERS.get(row - 1) + col;
        this.candidates = Objects.requireNonNullElseGet(candidates, () -> isEmpty() ? new ArrayList<>(Utils.NUMBERS) : new ArrayList<>());
    }

    public static SudokuCell fromNumbers(int value, int row, int col, List<Integer> candidates) {
        return new SudokuCell(value, row, col, candidates);
    }

    /**
     * Creates an exact copy of the cell passed as input
     */
    public static SudokuCell deepCopy(SudokuCell cell) {
        return new SudokuCell(cell.value, cell.row, cell.col, new ArrayList<>(cell.candidates));
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public void setValue(int value) {
        this.value = value;
        this.candidates.clear();
    }

    @Override
    public String toString() {
        return coordinates + " -> " + (isEmpty() ? "Candidates: " + candidates : "Value: " + value);
    }
}

