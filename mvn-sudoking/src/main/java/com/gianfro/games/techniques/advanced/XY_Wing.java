package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Stream;

public class XY_Wing {

    /**
     * If in a Sudoku TODO: complete
     */

    public static final String XY_WING = "XY-WING";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        return new LinkedHashSet<>(findXY_wings(sudoku));
    }

    private static List<ChangeLog> findXY_wings(Sudoku sudoku) {
        List<ChangeLog> changeLogs = new ArrayList<>();
        List<SudokuCell> bivalueCells = sudoku.getCells().stream().filter(c -> c.getCandidates().size() == 2).toList();
        for (SudokuCell pivot : bivalueCells) {
            List<SudokuCell> potentialPincersX = findXYStrongLinksOnCandidate(pivot, sudoku, pivot.getCandidates().get(0));
            List<SudokuCell> potentialPincersY = findXYStrongLinksOnCandidate(pivot, sudoku, pivot.getCandidates().get(1));

            List<List<SudokuCell>> pincers = findPincersSharingCandidateZ(potentialPincersX, potentialPincersY, pivot.getCandidates());
            for (List<SudokuCell> pairOfPincers : pincers) {
                int candidateZ = pairOfPincers.get(0).getCandidates().stream().filter(z -> !pivot.getCandidates().contains(z)).findFirst().orElse(0);
                List<SudokuCell> toBeSkimmed = sudoku.getCells().stream().filter(
                                c -> c.getCandidates().contains(candidateZ) &&
                                        c.canSeeOther(pairOfPincers.get(0)) &&
                                        c.canSeeOther(pairOfPincers.get(1)) &&
                                        c != pivot)
                        .toList();
                if (!toBeSkimmed.isEmpty()) {
                    Set<CellSkimmed> deductions = new HashSet<>();
                    List<Integer> removedCandidates = List.of(candidateZ);
                    for (SudokuCell cell : toBeSkimmed) {
                        deductions.add(
                                CellSkimmed.builder()
                                        .solvingTechnique(XY_WING)
                                        .cell(cell)
                                        .removedCandidates(removedCandidates)
                                        .build());
                    }
                    List<ChangeLogUnitMember> unitMembers = new ArrayList<>(Stream.concat(Stream.of(pivot), pairOfPincers.stream()).toList());
                    ChangeLog changeLog = ChangeLog.builder()
                            .unitExamined(removedCandidates)
                            .unitMembers(unitMembers)
                            .solvingTechnique(XY_WING)
                            .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                            .build();
                    changeLogs.add(changeLog);
                }
            }
        }
        return changeLogs;
    }

    /**
     * Given a SudokuCell, a Sudoku (of which the cell is part of), and a candidate,
     * returns the list of bivalue SudokuCell that share a strong link with the input cell
     */
    private static List<SudokuCell> findXYStrongLinksOnCandidate(SudokuCell cell, Sudoku sudoku, int candidate) {
        List<SudokuCell> strongLinks = new ArrayList<>();
        for (House house : House.values()) {
            List<SudokuCell> emptyHouseCells = Utils.getEmptyHouseCells(sudoku, house, cell.getHouseNumber(house));
            List<SudokuCell> houseStrongLinks = emptyHouseCells.stream().filter(c ->
                            c.getCandidates().size() == 2 &&
                                    c.getCandidates().contains(candidate) &&
                                    c != cell)
                    .toList();
            if (houseStrongLinks.size() == 1) {
                strongLinks.addAll(houseStrongLinks);
            }
        }
        return strongLinks;
    }

    private static List<List<SudokuCell>> findPincersSharingCandidateZ(List<SudokuCell> strongLinksX, List<SudokuCell> strongLinksY, List<Integer> xyValues) {
        List<List<SudokuCell>> pincers = new ArrayList<>();
        for (SudokuCell x : strongLinksX) {
            int candidateZ = x.getCandidates().stream().filter(z -> !xyValues.contains(z)).findFirst().orElse(0);
            for (SudokuCell y : strongLinksY) {
                if (y.getCandidates().contains(candidateZ)) {
                    pincers.add(List.of(x, y));
                }
            }
        }
        return pincers;
    }
}
