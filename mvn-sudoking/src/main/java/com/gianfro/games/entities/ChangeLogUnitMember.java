package com.gianfro.games.entities;

import java.util.ArrayList;
import java.util.List;

public interface ChangeLogUnitMember {

    int getBox();

    int getRow();

    int getCol();

    int getIndex();

    String getCoordinates();

    /**
     * Returns true if the other ChangeLogUnitMember has the same ROW, COL and/or BOX of this one
     */
    default boolean canSeeOther(ChangeLogUnitMember other) {
        return this.getIndex() != other.getIndex() && (this.getBox() == other.getBox() || this.getRow() == other.getRow() || this.getCol() == other.getCol());
    }

    /**
     * Given a Sudoku, returns the list of its 20 SudokuCell peers
     */
    default List<SudokuCell> getEmptyPeers(Sudoku sudoku) {
        List<SudokuCell> peers = new ArrayList<>();
        for (SudokuCell cell : sudoku.getCells()) {
            if (cell.isEmpty() && this.canSeeOther(cell)) {
                peers.add(cell);
            }
        }
        return peers;
    }

    /**
     * Given a list of SudokuCell, returns the list of the peers among them
     * NOTE: use carefully, be sure the list you are passing as input is correct (eg: all emptyCells / all bivalue cells...)
     */
    default List<SudokuCell> getEmptyPeers(List<SudokuCell> cells) {
        List<SudokuCell> peers = new ArrayList<>();
        for (SudokuCell cell : cells) {
            if (cell.isEmpty() && this.canSeeOther(cell)) {
                peers.add(cell);
            }
        }
        return peers;
    }

    default int getHouseNumber(House house) {
        return switch (house) {
            case BOX -> getBox();
            case ROW -> getRow();
            case COL -> getCol();
        };
    }
}
