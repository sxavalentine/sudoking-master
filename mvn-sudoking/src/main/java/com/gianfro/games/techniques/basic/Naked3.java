package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.*;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Naked3 {

    /**
     * If you can find three cells, all in the same house, that have only the same three candidates left,
     * you can eliminate that candidates from all other cells in that house.
     * It is important to note that not all cells must contain all three candidates,
     * but there must not be more than three candidates in the three cells all together.
     */

    public static final String NAKED_TRIPLE = "NAKED TRIPLE";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = nakedTriples(House.BOX, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = nakedTriples(House.ROW, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = nakedTriples(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult nakedTriples(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int houseNumber : Utils.NUMBERS) {

                List<Integer> candidatesWithAtLeastTwoOccurences = new ArrayList<>();
                for (int number : Utils.NUMBERS) {
                    int occurences = 0;
                    List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                    for (Tab tab : houseTabs) {
                        if (tab.getCandidates().contains(number)) {
                            occurences++;
                        }
                    }
                    if (occurences >= 2) {
                        candidatesWithAtLeastTwoOccurences.add(number);
                    }
                }
                if (candidatesWithAtLeastTwoOccurences.size() >= 3) {
                    List<List<Integer>> possibleTriples = Utils.findAllPossibleTuples(candidatesWithAtLeastTwoOccurences, 3);
                    for (List<Integer> triple : possibleTriples) {
                        List<ChangeLogUnitMember> tripleTabs = new ArrayList<>();
                        List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                        boolean deductionsDone = false;
                        List<Change> unitSkimmings = new ArrayList<>();
                        for (Tab tab : houseTabs) {
                            if (Utils.candidatesAreSameOrSubset(tab, triple)
                                    && Utils.containsAtLeastXCandidates(tab.getCandidates(), triple, 2)) {
                                tripleTabs.add(tab);
                            }
                        }
                        if (tripleTabs.size() == 3) {
                            for (Tab tab : houseTabs) {
                                if (!tripleTabs.contains(tab)) {
                                    List<Integer> candidatesToBeRemoved = triple.stream().filter(x -> tab.getCandidates().remove(x)).collect(Collectors.toList());
                                    if (!candidatesToBeRemoved.isEmpty()) {
                                        Skimming skimming = Skimming.builder()
                                                .solvingTechnique(NAKED_TRIPLE)
                                                .house(house)
                                                .row(tab.getRow())
                                                .col(tab.getCol())
                                                .number(0)
                                                .tab(tab)
                                                .removedCandidates(candidatesToBeRemoved)
                                                .build();
                                        unitSkimmings.add(skimming);
                                        deductionsDone = true;
                                    }
                                }
                            }
                            if (deductionsDone) {
                                ChangeLog changeLog = ChangeLog.builder()
                                        .unitExamined(triple)
                                        .house(house)
                                        .houseNumber(houseNumber)
                                        .unitMembers(tripleTabs)
                                        .solvingTechnique(NAKED_TRIPLE)
                                        .solvingTechniqueVariant(null)
                                        .changes(unitSkimmings)
                                        .build();
                                changeLogs.add(changeLog);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in NAKED TRIPLE " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }
}