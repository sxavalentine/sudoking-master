package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;
import org.paukov.combinatorics3.Generator;

import java.util.*;

public class W_Wing {

    /**
     * If in a Sudoku TODO: complete
     */

    public static final String W_WING = "W-WING";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        return new LinkedHashSet<>(findW_wings(sudoku));
    }

    private static List<ChangeLog> findW_wings(Sudoku sudoku) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        Map<List<Integer>, List<SudokuCell>> biValueCellsMap = new HashMap<>();
        for (SudokuCell cell : sudoku.getCells()) {
            if (cell.getCandidates().size() == 2) {
                biValueCellsMap.computeIfAbsent(cell.getCandidates(), k -> new ArrayList<>()).add(cell);
            }
        }
        biValueCellsMap.entrySet().forEach(entry -> {
            List<Integer> pairOfCandidates = entry.getKey();
            List<SudokuCell> cells = entry.getValue();
            if (cells.size() >= 2) {
                List<List<SudokuCell>> possiblePairs = Generator.combination(cells).simple(2).stream().toList();
                for (List<SudokuCell> pair : possiblePairs) {
                    SudokuCell cellA = pair.get(0);
                    SudokuCell cellB = pair.get(1);
                    if (!Utils.cellCanSeeOtherCell(cellA, cellB)) {
                        List<StrongLink> strongLinks = findStrongLinks(sudoku, cellA, cellB);
                        for (StrongLink link : strongLinks) {
                            int wingCandidate = link.getLinkA().getCandidates().stream().filter(
                                            c -> cellA.getCandidates().contains(c) && cellB.getCandidates().contains(c))
                                    .findFirst()
                                    .orElse(0);
                            int toBeRemoved = cellA.getCandidates().stream().filter(c -> c != wingCandidate).findFirst().orElse(0);
                            if (toBeRemoved != 0) {

                                List<SudokuCell> toBeSkimmed = sudoku.getCells().stream().filter(
                                        c -> c.getCandidates().contains(toBeRemoved) &&
                                                Utils.cellCanSeeOtherCell(c, cellA) &&
                                                Utils.cellCanSeeOtherCell(c, cellB)).toList();

                                if (!toBeSkimmed.isEmpty()) {
                                    Set<CellSkimmed> deductions = new HashSet<>();
                                    List<Integer> removedCandidates = List.of(toBeRemoved);
                                    for (SudokuCell cell : toBeSkimmed) {
                                        deductions.add(
                                                CellSkimmed.builder()
                                                        .solvingTechnique(W_WING)
                                                        .cell(cell)
                                                        .removedCandidates(removedCandidates)
                                                        .build());
                                    }
                                    //TODO: the unit members are placed in logical order A-linkA-linkB-B, keep this in mind for explainers
                                    List<ChangeLogUnitMember> unitMembers = List.of(cellA, link.getLinkA(), link.getLinkB(), cellB);
                                    ChangeLog changeLog = ChangeLog.builder()
                                            .unitExamined(removedCandidates)
                                            .unitMembers(unitMembers)
                                            .solvingTechnique(W_WING)
                                            .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                                            .build();
                                    changeLogs.add(changeLog);
                                }
                            }
                        }
                    }
                }
            }
        });
        return changeLogs;
    }

    private static List<StrongLink> findStrongLinks(Sudoku sudoku, SudokuCell cellA, SudokuCell cellB) {
        List<StrongLink> strongLinks = new LinkedList<>();
        List<SudokuCell> possibleLinkA = sudoku.getCells().stream().filter(
                        c -> Utils.cellsShareNCandidates(c, cellA, 1) &&
                                Utils.cellCanSeeOtherCell(c, cellA) &&
                                !Utils.cellCanSeeOtherCell(c, cellB))
                .toList();
        List<SudokuCell> possibleLinkB = sudoku.getCells().stream().filter(
                        c -> Utils.cellsShareNCandidates(c, cellB, 1) &&
                                Utils.cellCanSeeOtherCell(c, cellB) &&
                                !Utils.cellCanSeeOtherCell(c, cellA))
                .toList();
        for (SudokuCell linkA : possibleLinkA) {
            for (SudokuCell linkB : possibleLinkB) {
                StrongLink strongLink = StrongLink.findStrongLink(sudoku, linkA, linkB);
                if (strongLink != null) {
                    strongLinks.add(strongLink);
                }
            }
        }
        return strongLinks;
    }
}
