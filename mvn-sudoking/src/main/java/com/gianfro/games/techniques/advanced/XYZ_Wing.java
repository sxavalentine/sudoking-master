package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Stream;

public class XYZ_Wing {

    /**
     * If in a Sudoku TODO: complete
     */

    public static final String XYZ_WING = "XYZ-WING";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        return new LinkedHashSet<>(findXYZ_wings(sudoku));
    }

    private static List<ChangeLog> findXYZ_wings(Sudoku sudoku) {
        List<ChangeLog> changeLogs = new ArrayList<>();
        List<SudokuCell> triValueCells = sudoku.getCells().stream().filter(c -> c.getCandidates().size() == 3).toList();
        for (SudokuCell pivot : triValueCells) {
            //Finds potential pincers X (cells with only 2 candidates left, both included in pivot candidates
            List<SudokuCell> potentialPincersX = findPotentialPincerX(pivot, sudoku);

            for (SudokuCell pincerX : potentialPincersX) {
                int candidateY = pivot.getCandidates().stream().filter(y -> !pincerX.getCandidates().contains(y)).findFirst().orElse(0);
                //Find potential pincers Y (cells with only 2 candidates left, both included in pivot candidates, but one is different from link X)
                List<SudokuCell> potentialPincersY = findPotentialPincerY(pivot, sudoku, candidateY);

                List<List<SudokuCell>> pincers = new ArrayList<>();
                for (SudokuCell pincerY : potentialPincersY) {
                    int candidateZ = pincerX.getCandidates().stream().filter(z -> pincerY.getCandidates().contains(z)).findFirst().orElse(0);
                    if (candidateZ != 0) {
                        pincers.add(List.of(pincerX, pincerY));
                    }
                }
                for (List<SudokuCell> pincer : pincers) {
                    int candidateZ = pincer.get(0).getCandidates().stream().filter(z -> pincer.get(1).getCandidates().contains(z)).findFirst().orElse(0);

                    List<SudokuCell> toBeSkimmed = sudoku.getCells().stream().filter(
                                    c -> c.getCandidates().contains(candidateZ) &&
                                            c.canSeeOther(pivot) &&
                                            c.canSeeOther(pincer.get(0)) &&
                                            c.canSeeOther(pincer.get(1)))
                            .toList();
                    if (!toBeSkimmed.isEmpty()) {
                        Set<CellSkimmed> deductions = new HashSet<>();
                        List<Integer> removedCandidates = List.of(candidateZ);
                        for (SudokuCell cell : toBeSkimmed) {
                            deductions.add(
                                    CellSkimmed.builder()
                                            .solvingTechnique(XYZ_WING)
                                            .cell(cell)
                                            .removedCandidates(removedCandidates)
                                            .build());
                        }
                        List<ChangeLogUnitMember> unitMembers = new ArrayList<>(Stream.concat(Stream.of(pivot), pincer.stream()).toList());
                        ChangeLog changeLog = ChangeLog.builder()
                                .unitExamined(removedCandidates)
                                .unitMembers(unitMembers)
                                .solvingTechnique(XYZ_WING)
                                .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                                .build();
                        changeLogs.add(changeLog);
                    }
                }
            }
        }
        return changeLogs;
    }

    /**
     * Given a SudokuCell pivot, a Sudoku (of which the pivot is part of)
     * returns the list of bivalue SudokuCell that contain all candidates of the pivot
     */
    private static List<SudokuCell> findPotentialPincerX(SudokuCell pivot, Sudoku sudoku) {
        List<SudokuCell> strongLinks = new ArrayList<>();
        for (House house : House.values()) {
            List<SudokuCell> emptyHouseCells = Utils.getEmptyHouseCells(sudoku, house, pivot.getHouseNumber(house));
            List<SudokuCell> houseStrongLinks = emptyHouseCells.stream().filter(c ->
                            c.getCandidates().size() == 2 &&
                                    pivot.getCandidates().contains(c.getCandidates().get(0)) &&
                                    pivot.getCandidates().contains(c.getCandidates().get(1)) &&
                                    pivot != c)
                    .toList();
            if (houseStrongLinks.size() == 1) {
                strongLinks.addAll(houseStrongLinks);
            }
        }
        return strongLinks;
    }

    /**
     * Given a SudokuCell pivot, a Sudoku (of which the pivot is part of), and a candidate Y
     * returns the list of bivalue SudokuCell that contain the candidate Y and have both candidates in common with the pivot
     */
    private static List<SudokuCell> findPotentialPincerY(SudokuCell pivot, Sudoku sudoku, int candidateY) {
        List<SudokuCell> strongLinks = new ArrayList<>();
        for (House house : House.values()) {
            List<SudokuCell> emptyHouseCells = Utils.getEmptyHouseCells(sudoku, house, pivot.getHouseNumber(house));
            List<SudokuCell> houseStrongLinks = emptyHouseCells.stream().filter(c ->
                            c.getCandidates().size() == 2 &&
                                    c.getCandidates().contains(candidateY) &&
                                    pivot.getCandidates().contains(c.getCandidates().get(0)) &&
                                    pivot.getCandidates().contains(c.getCandidates().get(1)) &&
                                    pivot != c)
                    .toList();
            if (houseStrongLinks.size() == 1) {
                strongLinks.addAll(houseStrongLinks);
            }
        }
        return strongLinks;
    }
}
