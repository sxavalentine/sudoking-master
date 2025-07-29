package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;
import org.paukov.combinatorics3.Generator;

import java.util.*;
import java.util.stream.Collectors;

public class BasicFish {

    /**
     * If in a Sudoku TODO: complete
     */

    public static final String X_WING = "X-WING";
    public static final String SWORDFISH = "SWORDFISH";
    public static final String JELLYFISH = "JELLYFISH";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
//        changeLogs.addAll(findFish(X_WING, sudoku, House.ROW));
//        changeLogs.addAll(findFish(X_WING, sudoku, House.COL));

        changeLogs.addAll(findFish(SWORDFISH, sudoku, House.ROW));
        changeLogs.addAll(findFish(SWORDFISH, sudoku, House.COL));

        changeLogs.addAll(findFish(JELLYFISH, sudoku, House.ROW));
        changeLogs.addAll(findFish(JELLYFISH, sudoku, House.COL));
        return changeLogs;
    }

    private static List<ChangeLog> findFish(String technique, Sudoku sudoku, House house) {
        int size = getSize(technique);
        List<ChangeLog> changeLogs = new ArrayList<>();
        for (int candidate : Utils.NUMBERS) {
            List<List<SudokuCell>> baseSets = new ArrayList<>();
            for (int houseNumber : Utils.NUMBERS) {
                List<SudokuCell> emptyHouseCells = Utils.getEmptyHouseCells(sudoku, house, houseNumber);
                List<SudokuCell> welcomingCells = emptyHouseCells.stream().filter(c -> c.getCandidates().contains(candidate)).toList();
                if (!welcomingCells.isEmpty() && welcomingCells.size() <= size) {
                    baseSets.add(welcomingCells);
                }
            }
            if (baseSets.size() >= size) {
                House otherHouse = house == House.ROW ? House.COL : House.ROW;
                List<List<List<SudokuCell>>> possibleFishBaseSets = Generator.combination(baseSets).simple(size).stream().toList();
                for (List<List<SudokuCell>> currentFishBaseSet : possibleFishBaseSets) {

                    Set<Integer> crossAxeSet = currentFishBaseSet.stream()
                            .flatMap(crossAxe -> crossAxe.stream())
                            .map(cell -> cell.getHouseNumber(otherHouse))
                            .collect(Collectors.toSet());

                    if (crossAxeSet.size() == size) {
                        List<SudokuCell> actualFishCells = currentFishBaseSet.stream().flatMap(crossAxe -> crossAxe.stream()).toList();

                        List<SudokuCell> toBeSkimmed = sudoku.getCells().stream().filter(c ->
                                        !actualFishCells.contains(c) &&
                                                c.getCandidates().contains(candidate) &&
                                                crossAxeSet.contains(c.getHouseNumber(otherHouse)))
                                .toList();

                        if (!toBeSkimmed.isEmpty()) {
                            Set<CellSkimmed> deductions = new HashSet<>();
                            List<Integer> removedCandidates = List.of(candidate);
                            for (SudokuCell cell : toBeSkimmed) {
                                deductions.add(
                                        CellSkimmed.builder()
                                                .solvingTechnique(technique)
                                                .house(house)
                                                .cell(cell)
                                                .removedCandidates(removedCandidates)
                                                .build());
                            }
                            ChangeLog changeLog = ChangeLog.builder()
                                    .unitExamined(removedCandidates)
                                    .house(house)
                                    .houseNumber(0)
                                    .unitMembers(new ArrayList<>(actualFishCells))
                                    .solvingTechnique(technique)
                                    .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                                    .build();
                            changeLogs.add(changeLog);
                        }
                    }
                }
            }
        }
        return changeLogs;
    }

    private static int getSize(String technique) {
        return switch (technique) {
            case X_WING -> 2;
            case SWORDFISH -> 3;
            case JELLYFISH -> 4;
            default -> throw new IllegalArgumentException("Invalid technique name: " + technique);
        };
    }

    public static void main(String[] args) {
        Sudoku sudoku = Sudoku.fromString(SudokuList.TEST_JELLIFISH_ROW);
        BasicFish.check(sudoku);
    }
}