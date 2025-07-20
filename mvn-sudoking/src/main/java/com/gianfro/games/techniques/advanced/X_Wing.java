package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class X_Wing {

    /**
     * If in a Sudoku TODO: complete
     */

    public static final String X_WING = "X-WING";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        changeLogs.addAll(findRectangle(sudoku, House.ROW));
        changeLogs.addAll(findRectangle(sudoku, House.COL));
        return changeLogs;
    }

    private static List<ChangeLog> findRectangle(Sudoku sudoku, House house) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        // Pointless iterate up to 9, if there is an X-Wing will be found earlier
        for (int houseNumber = 1; houseNumber < 9; houseNumber++) {
            List<SudokuCell> emptyHouseCells = Utils.getEmptyHouseCells(sudoku, house, houseNumber);
            for (int candidate : Utils.NUMBERS) {
                List<SudokuCell> welcomingCells = emptyHouseCells.stream().filter(c -> c.getCandidates().contains(candidate)).toList();
                if (welcomingCells.size() == 2) {
                    //if(welcomingCells.size() <= size) {//TODO PER GENERALIZZARE (SWORDFISH, JELLYFISH)
                    House otherHouse = house == House.ROW ? House.COL : House.ROW;
                    // If we are iterating on ROW gets the set of COL, otherwise the other way round
                    Set<Integer> crossAxes1 = welcomingCells.stream().map(c -> c.getHouseNumber(otherHouse)).collect(Collectors.toSet());
                    for (int otherHouseNumber = houseNumber + 1; otherHouseNumber < 10; otherHouseNumber++) {
                        List<SudokuCell> otherWelcomingCells = Utils.getEmptyHouseCells(sudoku, house, otherHouseNumber).stream().filter(
                                c -> c.getCandidates().contains(candidate)).toList();
                        Set<Integer> crossAxes2 = otherWelcomingCells.stream().map(
                                c -> c.getHouseNumber(otherHouse)).collect(Collectors.toSet());
                        if (crossAxes1.equals(crossAxes2)) {

                            List<ChangeLogUnitMember> unitMembers = new ArrayList<>();
                            unitMembers.addAll(welcomingCells);
                            unitMembers.addAll(otherWelcomingCells);

                            List<SudokuCell> toBeSkimmed = sudoku.getCells().stream().filter(
                                            c -> crossAxes1.contains(c.getHouseNumber(otherHouse))
                                                    && c.getCandidates().contains(candidate)
                                                    && !welcomingCells.contains(c)
                                                    && !otherWelcomingCells.contains(c))
                                    .toList();

                            if (!toBeSkimmed.isEmpty()) {
                                Set<CellSkimmed> deductions = new HashSet<>();
                                List<Integer> removedCandidates = List.of(candidate);
                                for (SudokuCell cell : toBeSkimmed) {
                                    deductions.add(
                                            CellSkimmed.builder()
                                                    .solvingTechnique(X_WING)
                                                    .house(house)
                                                    .cell(cell)
                                                    .removedCandidates(removedCandidates)
                                                    .build());
                                }
                                ChangeLog changeLog = ChangeLog.builder()
                                        .unitExamined(removedCandidates)
                                        .house(house)
                                        .houseNumber(houseNumber)
                                        .unitMembers(unitMembers)
                                        .solvingTechnique(X_WING)
                                        .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                                        .build();
                                changeLogs.add(changeLog);
                            }
                        }
                    }
                }
            }
        }
        return changeLogs;
    }
}
