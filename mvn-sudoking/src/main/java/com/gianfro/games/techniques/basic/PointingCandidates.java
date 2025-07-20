package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class PointingCandidates {

    /**
     * If in a Sudoku Box all cells with the candidate X belong to the same row or column
     * then we can remove that candidate X from all the other cells of that row or column
     */

    public final static String POINTING_CANDIDATES = "POINTING CANDIDATES";
    private final static String POINTING_PAIR = "POINTING PAIR";
    private final static String POINTING_TRIPLE = "POINTING TRIPLE";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        changeLogs.addAll(pointingCandidates(sudoku, House.ROW));
        changeLogs.addAll(pointingCandidates(sudoku, House.COL));
        return changeLogs;
    }

    private static List<ChangeLog> pointingCandidates(Sudoku sudoku, House house) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        for (int boxNumber : Utils.NUMBERS) {
            List<SudokuCell> emptyBoxCells = Utils.getEmptyHouseCells(sudoku, House.BOX, boxNumber);
            for (int number : Utils.NUMBERS) {
                List<ChangeLogUnitMember> welcomingCells = new ArrayList<>();
                Set<Integer> welcomingHouses = new HashSet<>();
                for (SudokuCell cell : emptyBoxCells) {
                    if (cell.getCandidates().contains(number)) {
                        welcomingCells.add(cell);
                        welcomingHouses.add(house == House.ROW ? cell.getRow() : cell.getCol());
                    }
                }
                if (welcomingHouses.size() == 1 && welcomingCells.size() >= 2) {
                    String method = welcomingCells.size() == 2 ? POINTING_PAIR : POINTING_TRIPLE;
                    int welcomingHouse = new ArrayList<>(welcomingHouses).get(0);
                    List<SudokuCell> emptyHouseCells = Utils.getEmptyHouseCells(sudoku, house, welcomingHouse);
                    Set<CellSkimmed> deductions = new HashSet<>();
                    for (SudokuCell cell : emptyHouseCells) {
                        if (cell.getBox() != boxNumber && cell.getCandidates().contains(number)) {
                            CellSkimmed skimming = CellSkimmed.builder()
                                    .solvingTechnique(method)
                                    .house(house)
                                    .cell(cell)
                                    .removedCandidates(List.of(number))
                                    .build();
                            deductions.add(skimming);
                        }
                    }
                    if (!deductions.isEmpty()) {
                        ChangeLog changeLog = ChangeLog.builder()
                                .unitExamined(List.of(number))
                                .house(House.BOX)
                                .houseNumber(boxNumber)
                                .unitMembers(welcomingCells)
                                .solvingTechnique(POINTING_CANDIDATES)
                                .solvingTechniqueVariant(method)
                                .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                                .build();
                        changeLogs.add(changeLog);
                    }
                }
            }
        }
        return changeLogs;
    }
}