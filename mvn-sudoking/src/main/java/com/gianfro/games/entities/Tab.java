package com.gianfro.games.entities;

import com.gianfro.games.exceptions.InvalidHouseException;
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
    List<Integer> numbers;

    public Tab(int row, int col, List<Integer> numbers) {
        this.box = getBoxIndex(row, col);
        this.row = row;
        this.col = col;
        this.numbers = numbers;
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
        switch (house) {
            case BOX:
                return box;
            case ROW:
                return row;
            case COL:
                return col;
            default:
                throw new InvalidHouseException();
        }
    }

    @Override
    public String toString() {
        return Utils.ROWS_LETTERS.get(row - 1) + col + ", " + numbers;
    }
}
