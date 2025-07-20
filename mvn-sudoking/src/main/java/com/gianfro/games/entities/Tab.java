package com.gianfro.games.entities;

import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
/**
 * Represents a Sudoku cell that isn't solved yet.
 * @field box         -> the row where the cell is set (1 based index)
 * @field row         -> the row where the cell is set (1 based index)
 * @field col         -> the column where the cell is set (1 based index)
 * @field candidates  -> the numbers that can still be put as cell value
 * @field coordinates -> the alphanumeric coordinates of the cell (eg: A1, I9)
 */
public class Tab implements ChangeLogUnitMember {

    int box;
    int row;
    int col;
    List<Integer> candidates;
    String coordinates;

    public Tab(int row, int col, List<Integer> candidates) {
        this.box = getBoxIndex(row, col);
        this.row = row;
        this.col = col;
        this.candidates = candidates;
        this.coordinates = Utils.ROWS_LETTERS.get(row - 1) + col;
    }

    private static int getBoxIndex(int row, int col) {
        if (row < 4) {
            return col < 4 ? 1 : col < 7 ? 2 : 3;
        } else if (row < 7) {
            return col < 4 ? 4 : col < 7 ? 5 : 6;
        } else {
            return col < 4 ? 7 : col < 7 ? 8 : 9;
        }
    }

    @Override
    public String toString() {
        return coordinates + ", " + candidates;
    }
}
