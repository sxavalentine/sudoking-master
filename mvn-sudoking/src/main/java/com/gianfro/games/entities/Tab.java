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

    public int getHouseNumber(House house) {
        return switch (house) {
            case BOX -> box;
            case ROW -> row;
            case COL -> col;
        };
    }

    @Override
    public String toString() {
        return coordinates + ", " + candidates;
    }
}
