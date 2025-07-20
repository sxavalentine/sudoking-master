package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class Skyscraper {

    /**
     * If in a Sudoku TODO: complete
     */

    public static final String SKYSCRAPER = "SKYSCRAPER";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        changeLogs.addAll(findSkyscraper(sudoku, House.ROW));
        changeLogs.addAll(findSkyscraper(sudoku, House.COL));
        return changeLogs;
    }

    private static List<ChangeLog> findSkyscraper(Sudoku sudoku, House house) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        for (int candidate : Utils.NUMBERS) {
            List<List<SudokuCell>> housesWithTwoWelcomingCells = new LinkedList<>();
            for (int houseNumber : Utils.NUMBERS) {
                List<SudokuCell> welcomingCells = Utils.getEmptyHouseCells(sudoku, house, houseNumber).stream().filter(
                        cell -> cell.getCandidates().contains(candidate)).toList();
                if (welcomingCells.size() == 2) {
                    housesWithTwoWelcomingCells.add(welcomingCells);
                }
            }
            if (housesWithTwoWelcomingCells.size() == 2) {
                House crossAxe = house == House.ROW ? House.COL : House.ROW;
                int sharedCrossAxe = 0; //The crossAxe shared (the base of the skyscraper)
                int skyscraperBasesIndex = 0; // the index where the skyscraper base cells (the middle ones) are located (1 or 2)
                for (int i = 0; i < 2; i++) {
                    int crossAxe1 = housesWithTwoWelcomingCells.get(0).get(i).getHouseNumber(crossAxe);
                    int crossAxe2 = housesWithTwoWelcomingCells.get(1).get(i).getHouseNumber(crossAxe);
                    if (crossAxe1 == crossAxe2) {
                        sharedCrossAxe = crossAxe1;
                        skyscraperBasesIndex = i;
                        break;
                    }
                }
                if (sharedCrossAxe != 0) {
                    // at this point is sure there is a skyscraper, we just need to understand how it's oriented
                    int skyscraperRoofsIndex = skyscraperBasesIndex == 0 ? 1 : 0;
                    List<SudokuCell> changeLogUnitMembers = new LinkedList<>();
                    changeLogUnitMembers.add(housesWithTwoWelcomingCells.get(0).get(skyscraperRoofsIndex));
                    changeLogUnitMembers.add(housesWithTwoWelcomingCells.get(0).get(skyscraperBasesIndex));
                    changeLogUnitMembers.add(housesWithTwoWelcomingCells.get(1).get(skyscraperBasesIndex));
                    changeLogUnitMembers.add(housesWithTwoWelcomingCells.get(1).get(skyscraperRoofsIndex));

                    List<SudokuCell> toBeSkimmed = sudoku.getCells().stream().filter(c ->
                                    c.getCandidates().contains(candidate) &&
                                            Utils.cellCanSeeOtherCell(c, changeLogUnitMembers.get(0)) &&
                                            Utils.cellCanSeeOtherCell(c, changeLogUnitMembers.get(3)))
                            .toList();

                    if (!toBeSkimmed.isEmpty()) {
                        Set<CellSkimmed> deductions = new HashSet<>();
                        List<Integer> removedCandidates = List.of(candidate);
                        for (SudokuCell cell : toBeSkimmed) {
                            deductions.add(
                                    CellSkimmed.builder()
                                            .solvingTechnique(SKYSCRAPER)
                                            .house(house)
                                            .cell(cell)
                                            .removedCandidates(removedCandidates)
                                            .build());
                        }
                        // The unitMembers are ordered as such: ROOF1, BASE 1, BASE2, ROOF2
                        List<ChangeLogUnitMember> unitMembers = new ArrayList<>(changeLogUnitMembers);
                        ChangeLog changeLog = ChangeLog.builder()
                                .unitExamined(removedCandidates)
                                .house(house)
                                .unitMembers(unitMembers)
                                .solvingTechnique(SKYSCRAPER)
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
