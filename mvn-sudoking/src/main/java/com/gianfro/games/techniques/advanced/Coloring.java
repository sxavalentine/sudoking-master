package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class Coloring {

    /**
     * Starting from two strongly linked SudokuCells, we split them in two colors: the first will be colorA, the second colorB.
     * We then proceed coloring all others strongly linked cells until the map is completed.
     * The cells with a certain color must be all true or all false (aka: containing or not containing a certain candidates).
     * We can then draw two types of conclusions:
     * 1) if an uncolored SudokuCell can see two different colors, we can remove the candidate X from its candidates
     * 2) if a colored SudokuCell can see another cell of the same color, we can remove the candidate X from all the cells with that color.
     */

    public static final String COLORING = "COLORING";

    public static final String COLOR_TRAP = "COLOR TRAP";

    public static final String COLOR_WRAP = "COLOR WRAP";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>(colorGrid(sudoku));
        return changeLogs;
    }

    private static List<ChangeLog> colorGrid(Sudoku sudoku) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        for (Integer candidate : Utils.NUMBERS) {
            Set<SudokuCell> colorA = new HashSet<>();
            Set<SudokuCell> colorB = new HashSet<>();
            for (SudokuCell cell : sudoku.getCells().stream().filter(c -> c.isEmpty()).toList()) {
                List<SudokuCell> strongLinks = findStrongLinksOnCandidate(cell, sudoku, candidate);
                if (!strongLinks.isEmpty()) {
                    colorA.add(cell);
                    colorB.addAll(strongLinks);
                    break;
                }
            }
            if (!colorA.isEmpty()) {
                boolean isColorA = false;
                Set<SudokuCell> additions = new HashSet<>(colorB);
                while (!additions.isEmpty()) {
//                    Set<SudokuCell> currentColor = isColorA ? colorA : colorB;
                    Set<SudokuCell> oppositeColor = isColorA ? colorB : colorA;
                    Set<SudokuCell> newAdditions = new HashSet<>();
                    for (SudokuCell colorCell : additions) {
                        List<SudokuCell> strongLinks = findStrongLinksOnCandidate(colorCell, sudoku, candidate);
                        if (!strongLinks.isEmpty()) {
                            for (SudokuCell link : strongLinks) {
                                if (oppositeColor.add(link)) {
                                    newAdditions.add(link);
                                }
                            }
                        }
                    }
                    isColorA = !isColorA;
                    additions = newAdditions;
                }
                //at this point colorA and colorB are completely filled. We need to check if we can eliminate something
                List<ChangeLogUnitMember> unitMembers = new LinkedList<>();
                unitMembers.addAll(colorA.stream().map(a -> new Link(a, true, candidate)).toList());
                unitMembers.addAll(colorB.stream().map(b -> new Link(b, false, candidate)).toList());

                List<Integer> removedCandidates = List.of(candidate);

                // COLOR WRAP
                List<SudokuCell> colorWrapped = sudoku.getCells().stream().filter(
                        c -> c.getCandidates().contains(candidate) &&
                                !colorA.contains(c) &&
                                !colorB.contains(c) &&
                                !colorA.stream().filter(a -> Utils.cellCanSeeOtherCell(a, c)).toList().isEmpty() &&
                                !colorB.stream().filter(b -> Utils.cellCanSeeOtherCell(b, c)).toList().isEmpty()
                ).toList();
                if (!colorWrapped.isEmpty()) {
                    Set<CellSkimmed> deductions = new HashSet<>();
                    for (SudokuCell cell : colorWrapped) {
                        deductions.add(
                                CellSkimmed.builder()
                                        .solvingTechnique(COLOR_WRAP)
                                        .cell(cell)
                                        .removedCandidates(removedCandidates)
                                        .build());
                    }
                    ChangeLog changeLog = ChangeLog.builder()
                            .unitExamined(removedCandidates)
                            .unitMembers(unitMembers)
                            .solvingTechnique(COLORING)
                            .solvingTechniqueVariant(COLOR_WRAP)
                            .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                            .build();
                    changeLogs.add(changeLog);
                }
                // COLOR TRAP
                List<SudokuCell> colorTrapped = new LinkedList<>();
                for (SudokuCell aCell : colorA) {
                    if (!colorA.stream().filter(c -> Utils.cellCanSeeOtherCell(c, aCell)).toList().isEmpty()) {
                        colorTrapped = colorA.stream().toList();
                        break;
                    }
                }
                for (SudokuCell bCell : colorB) {
                    if (!colorB.stream().filter(c -> Utils.cellCanSeeOtherCell(c, bCell)).toList().isEmpty()) {
                        colorTrapped = colorB.stream().toList();
                        break;
                    }
                }
                if (!colorTrapped.isEmpty()) {
                    Set<CellSkimmed> deductions = new HashSet<>();
                    for (SudokuCell cell : colorTrapped) {
                        deductions.add(
                                CellSkimmed.builder()
                                        .solvingTechnique(COLOR_TRAP)
                                        .cell(cell)
                                        .removedCandidates(removedCandidates)
                                        .build());
                    }
                    ChangeLog changeLog = ChangeLog.builder()
                            .unitExamined(removedCandidates)
                            .unitMembers(unitMembers)
                            .solvingTechnique(COLORING)
                            .solvingTechniqueVariant(COLOR_TRAP)
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
    private static List<SudokuCell> findStrongLinksOnCandidate(SudokuCell cell, Sudoku sudoku, int candidate) {
        List<SudokuCell> strongLinks = new LinkedList<>();
        for (House house : House.values()) {
            List<SudokuCell> houseCells = Utils.getHouseCells(sudoku, house, cell.getHouseNumber(house));
            List<SudokuCell> welcomingCells = houseCells.stream().filter(c ->
                            c.getCandidates().contains(candidate) &&
                                    c != cell)
                    .toList();
            if (welcomingCells.size() == 1) {
                strongLinks.addAll(welcomingCells);
            }
        }
        return strongLinks;
    }

    public static void main(String[] args) {
        Sudoku sudoku = Sudoku.fromString(SudokuList.COLOR_TRAP);
        SolutionStep solutionStep = SudokuSolver.useBasicSolvingTechniques(sudoku);
        Sudoku newSudoku = Utils.applyDeductions(solutionStep.getChangeLogs(), sudoku);
        Utils.megaGrid(newSudoku);
        Coloring.check(newSudoku);
    }


}
