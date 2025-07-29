package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellSkimmed;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class Coloring {

    /**
     * Starting from two strongly linked SudokuCells, we split them in two colors: the first will be colorA, the second colorB.
     * We then proceed coloring all others strongly linked cells until the map is completed.
     * The cells with a certain color must be all true or all false (aka: containing or not containing a certain candidates).
     * We can then draw two types of conclusions:
     * 1) if an uncolored SudokuCell can see two different colors, we can remove the candidate X from its candidates (COLOR WRAP).
     * 2) if a colored SudokuCell can see another cell of the same color, we can remove the candidate X from all the cells with that color (COLOR TRAP).
     */

    public static final String COLORING = "COLORING";

    public static final String COLOR_TRAP = "COLOR TRAP";

    public static final String COLOR_WRAP = "COLOR WRAP";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>(colorGrid(sudoku));
        return changeLogs;
    }

    private static List<ChangeLog> colorGrid(Sudoku sudoku) {
        List<ChangeLog> changeLogs = new ArrayList<>();
//        Map<Integer, List<StrongLink>> strongLinksMap = ChainUtils.getStrongLinksMap(sudoku);
//        for (Integer candidate : strongLinksMap.keySet()) {
//
//            List<StrongLink> strongLinks = strongLinksMap.get(candidate);
//            List<SudokuCell> stronglyLinksCells = strongLinks.stream().flatMap(
//                    sl -> List.of(sl.getLinkA(), sl.getLinkB()).stream()).toList();
//
//            Set<SudokuCell> colorA = new HashSet<>();
//            colorA.add(strongLinks.get(0).getLinkA());
//
//            Set<SudokuCell> colorB = new HashSet<>();
//            colorB.add(strongLinks.get(0).getLinkB());
//
//            boolean isColorA = false;
//            Set<SudokuCell> additions = new HashSet<>(colorB);
//            while (!additions.isEmpty()) {
//                Set<SudokuCell> oppositeColor = isColorA ? colorB : colorA;
//                Set<SudokuCell> newAdditions = new HashSet<>();
//                for (SudokuCell colorCell : additions) {
//                    List<SudokuCell> stronglyLinked = stronglyLinksCells.stream().filter(c -> Utils.cellCanSeeOtherCell(c, colorCell)).toList();
//                    for (SudokuCell link : stronglyLinked) {
//                        if (oppositeColor.add(link)) {
//                            newAdditions.add(link);
//                        }
//                    }
//                }
//                isColorA = !isColorA;
//                additions = newAdditions;
//            }
//            //at this point colorA and colorB are completely filled. We need to check if we can eliminate something
//            List<ChangeLogUnitMember> unitMembers = new ArrayList<>();
//            unitMembers.addAll(colorA.stream().map(a -> new Link(a, true, candidate)).toList());
//            unitMembers.addAll(colorB.stream().map(b -> new Link(b, false, candidate)).toList());
//
//            List<Integer> removedCandidates = List.of(candidate);
//
//            // COLOR WRAP
//            List<SudokuCell> colorWrapped = sudoku.getCells().stream().filter(
//                    c -> c.getCandidates().contains(candidate) &&
//                            !colorA.contains(c) &&
//                            !colorB.contains(c) &&
//                            !colorA.stream().filter(a -> Utils.cellCanSeeOtherCell(a, c)).toList().isEmpty() &&
//                            !colorB.stream().filter(b -> Utils.cellCanSeeOtherCell(b, c)).toList().isEmpty()
//            ).toList();
//            if (!colorWrapped.isEmpty()) {
//                Set<CellSkimmed> deductions = new HashSet<>();
//                for (SudokuCell cell : colorWrapped) {
//                    deductions.add(
//                            CellSkimmed.builder()
//                                    .solvingTechnique(COLOR_WRAP)
//                                    .cell(cell)
//                                    .removedCandidates(removedCandidates)
//                                    .build());
//                }
//                ChangeLog changeLog = ChangeLog.builder()
//                        .unitExamined(removedCandidates)
//                        .unitMembers(unitMembers)
//                        .solvingTechnique(COLORING)
//                        .solvingTechniqueVariant(COLOR_WRAP)
//                        .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
//                        .build();
//                changeLogs.add(changeLog);
//            }
//            // COLOR TRAP
//            List<SudokuCell> colorTrapped = new ArrayList<>();
//            for (SudokuCell aCell : colorA) {
//                if (!colorA.stream().filter(c -> Utils.cellCanSeeOtherCell(c, aCell)).toList().isEmpty()) {
//                    colorTrapped = colorA.stream().toList();
//                    break;
//                }
//            }
//            for (SudokuCell bCell : colorB) {
//                if (!colorB.stream().filter(c -> Utils.cellCanSeeOtherCell(c, bCell)).toList().isEmpty()) {
//                    colorTrapped = colorB.stream().toList();
//                    break;
//                }
//            }
//            if (!colorTrapped.isEmpty()) {
//                Set<CellSkimmed> deductions = new HashSet<>();
//                for (SudokuCell cell : colorTrapped) {
//                    deductions.add(
//                            CellSkimmed.builder()
//                                    .solvingTechnique(COLOR_TRAP)
//                                    .cell(cell)
//                                    .removedCandidates(removedCandidates)
//                                    .build());
//                }
//                ChangeLog changeLog = ChangeLog.builder()
//                        .unitExamined(removedCandidates)
//                        .unitMembers(unitMembers)
//                        .solvingTechnique(COLORING)
//                        .solvingTechniqueVariant(COLOR_TRAP)
//                        .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
//                        .build();
//                changeLogs.add(changeLog);
//            }
//        }
//        return changeLogs;

        for (Integer candidate : Utils.NUMBERS) {
            Set<SudokuCell> colorA = new HashSet<>();
            Set<SudokuCell> colorB = new HashSet<>();
            for (SudokuCell cell : sudoku.getCells().stream().filter(SudokuCell::isEmpty).toList()) {
                List<SudokuCell> strongLinks = ChainUtils.findStrongLinksOnCandidate(cell, sudoku, candidate);
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
                    Set<SudokuCell> oppositeColor = isColorA ? colorB : colorA;
                    Set<SudokuCell> newAdditions = new HashSet<>();
                    for (SudokuCell colorCell : additions) {
                        List<SudokuCell> strongLinks = ChainUtils.findStrongLinksOnCandidate(colorCell, sudoku, candidate);
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
                List<ChangeLogUnitMember> unitMembers = new ArrayList<>();
                unitMembers.addAll(colorA.stream().map(a -> new Link(a, candidate, 0)).toList());
                unitMembers.addAll(colorB.stream().map(b -> new Link(b, 0, candidate)).toList());

                List<Integer> removedCandidates = List.of(candidate);

                // COLOR WRAP
                List<SudokuCell> colorWrapped = sudoku.getCells().stream().filter(
                        c -> c.getCandidates().contains(candidate) &&
                                !colorA.contains(c) &&
                                !colorB.contains(c) &&
                                !colorA.stream().filter(a -> a.canSeeOther(c)).toList().isEmpty() &&
                                !colorB.stream().filter(b -> b.canSeeOther(c)).toList().isEmpty()
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
                List<SudokuCell> colorTrapped = new ArrayList<>();
                for (SudokuCell aCell : colorA) {
                    if (!colorA.stream().filter(c -> c.canSeeOther(aCell)).toList().isEmpty()) {
                        colorTrapped = colorA.stream().toList();
                        break;
                    }
                }
                for (SudokuCell bCell : colorB) {
                    if (!colorB.stream().filter(c -> c.canSeeOther(bCell)).toList().isEmpty()) {
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
}
