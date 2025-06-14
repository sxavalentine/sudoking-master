package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.*;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Hidden2 {

    /**
     * If there are two cells within a house such as that two candidates appear nowhere outside those cells in that house,
     * those two candidates must be placed in the two cells. All other candidates can therefore be eliminated.
     */

    public static final String HIDDEN_PAIR = "HIDDEN PAIR";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = hiddenPairs(House.BOX, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenPairs(House.ROW, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenPairs(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult hiddenPairs(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int houseNumber : Utils.NUMBERS) {
                List<Integer> candidatesWithTwoOccurences = new ArrayList<>();
                for (int number : Utils.NUMBERS) {
                    int occurences = 0;
                    List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                    for (Tab tab : houseTabs) {
                        if (tab.getCandidates().contains(number)) {
                            occurences++;
                        }
                    }
                    if (occurences == 2) {
                        candidatesWithTwoOccurences.add(number);
                    }
                }
                if (candidatesWithTwoOccurences.size() >= 2) {
                    List<List<Integer>> possiblePairs = Utils.findAllPossibleTuples(candidatesWithTwoOccurences, 2);
                    for (List<Integer> possiblePair : possiblePairs) {
                        List<ChangeLogUnitMember> pairTabs = new ArrayList<>();
                        List<Tab> shamTabs = new ArrayList<>();
                        List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                        for (Tab tab : houseTabs) {
                            if (Utils.containsAtLeastXCandidates(tab.getCandidates(), possiblePair, 1)) {
                                if (Utils.containsAtLeastXCandidates(tab.getCandidates(), possiblePair, 2)) {
                                    pairTabs.add(tab);
                                } else {
                                    shamTabs.add(tab);
                                }
                            }
                        }
                        if (pairTabs.size() == 2 && shamTabs.isEmpty()) {
                            List<Change> unitSkimmings = new ArrayList<>();
                            for (ChangeLogUnitMember unitMember : pairTabs) {
                                Tab tab = (Tab) unitMember;
                                List<Integer> candidatesToBeRemoved = tab.getCandidates().stream().filter(x -> !possiblePair.contains(x)).collect(Collectors.toList());
                                tab.getCandidates().removeAll(candidatesToBeRemoved);
                                if (!candidatesToBeRemoved.isEmpty()) {
                                    Skimming skimming = Skimming.builder()
                                            .solvingTechnique(HIDDEN_PAIR)
                                            .house(null)
                                            .row(tab.getRow())
                                            .col(tab.getCol())
                                            .number(0)
                                            .tab(tab)
                                            .removedCandidates(candidatesToBeRemoved)
                                            .build();
                                    unitSkimmings.add(skimming);
                                }
                            }
                            if (!unitSkimmings.isEmpty()) {
                                ChangeLog changeLog = ChangeLog.builder()
                                        .unitExamined(possiblePair)
                                        .house(house)
                                        .houseNumber(houseNumber)
                                        .unitMembers(pairTabs)
                                        .solvingTechnique(HIDDEN_PAIR)
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
            System.out.println("Exception in HIDDEN PAIR " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }
}