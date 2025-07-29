package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;
import org.paukov.combinatorics3.Generator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FinnedFish {

    /**
     * If in a Sudoku TODO: complete
     */

    public static final String FINNED_X_WING = "FINNED X-WING";
    public static final String FINNED_SWORDFISH = "FINNED SWORDFISH";
    public static final String FINNED_JELLYFISH = "FINNED JELLYFISH";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();

        changeLogs.addAll(findFinnedFish(FINNED_X_WING, sudoku, House.ROW));
        changeLogs.addAll(findFinnedFish(FINNED_X_WING, sudoku, House.COL));

        changeLogs.addAll(findFinnedFish(FINNED_SWORDFISH, sudoku, House.ROW));
        changeLogs.addAll(findFinnedFish(FINNED_SWORDFISH, sudoku, House.COL));

        changeLogs.addAll(findFinnedFish(FINNED_JELLYFISH, sudoku, House.ROW));
        changeLogs.addAll(findFinnedFish(FINNED_JELLYFISH, sudoku, House.COL));
        return changeLogs;
    }

    private static List<ChangeLog> findFinnedFish(String technique, Sudoku sudoku, House house) {
        int size = getSize(technique);
        List<ChangeLog> changeLogs = new ArrayList<>();
        for (int candidate : Utils.NUMBERS) {
            List<List<SudokuCell>> regularSets = new ArrayList<>();
            List<List<SudokuCell>> finnedSets = new ArrayList<>();
            for (int houseNumber : Utils.NUMBERS) {
                List<SudokuCell> emptyHouseCells = Utils.getEmptyHouseCells(sudoku, house, houseNumber);
                List<SudokuCell> welcomingCells = emptyHouseCells.stream().filter(c -> c.getCandidates().contains(candidate)).toList();
                if (welcomingCells.size() >= 2 && welcomingCells.size() <= size) {
                    regularSets.add(welcomingCells);
                } else if (welcomingCells.size() == size + 1) {
                    finnedSets.add(welcomingCells);
                }
            }
            if (regularSets.size() >= size - 1 && !finnedSets.isEmpty()) {
                House otherHouse = house == House.ROW ? House.COL : House.ROW;
                List<List<List<SudokuCell>>> possibleFishBaseSets = Generator.combination(regularSets).simple(size - 1).stream().toList();
                for (List<SudokuCell> fin : finnedSets) {
                    for (List<List<SudokuCell>> currentFishBaseSet : possibleFishBaseSets) {
                        Set<Integer> crossAxeSet = currentFishBaseSet.stream()
                                .flatMap(crossAxe -> crossAxe.stream())
                                .map(cell -> cell.getHouseNumber(otherHouse))
                                .collect(Collectors.toSet());

                        if (crossAxeSet.size() == size) {
                            Set<Integer> finCrossAxeSet = fin.stream().map(cell -> cell.getHouseNumber(otherHouse)).collect(Collectors.toSet());
                            Set<Integer> crossAxeCombined = Stream.of(crossAxeSet, finCrossAxeSet)
                                    .flatMap(set -> set.stream())
                                    .collect(Collectors.toSet());

                            if (crossAxeCombined.size() == size + 1) {
                                List<SudokuCell> actualFinnedFishCells = Stream.concat(
                                                currentFishBaseSet.stream().flatMap(List::stream),
                                                fin.stream())
                                        .toList();

                                List<SudokuCell> toBeSkimmed = sudoku.getCells().stream().filter(c ->
                                                !actualFinnedFishCells.contains(c) &&
                                                        c.getCandidates().contains(candidate) &&
                                                        crossAxeCombined.contains(c.getHouseNumber(otherHouse)))
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
                                            .unitMembers(new ArrayList<>(actualFinnedFishCells))
                                            .solvingTechnique(technique)
                                            .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                                            .build();
                                    changeLogs.add(changeLog);
                                }
                            }
                        }

                    }
                }
            }
        }
        return changeLogs;
    }

    private static int getSize(String technique) {
        return switch (technique) {
            case FINNED_X_WING -> 2;
            case FINNED_SWORDFISH -> 3;
            case FINNED_JELLYFISH -> 4;
            default -> throw new IllegalArgumentException("Invalid technique name: " + technique);
        };
    }
}
