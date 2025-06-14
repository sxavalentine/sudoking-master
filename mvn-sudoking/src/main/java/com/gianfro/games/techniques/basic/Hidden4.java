package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.*;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Hidden4 {

    /**
     * If there are four cells within a house such as that four candidates appear nowhere outside those cells in that house,
     * those four candidates must be placed in the four cells. All other candidates can therefore be eliminated.
     */

    public static final String HIDDEN_QUAD = "HIDDEN QUAD";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = hiddenQuadruples(House.BOX, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenQuadruples(House.ROW, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenQuadruples(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult hiddenQuadruples(House house, List<Tab> tabs) {
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
                    if (occurences == 2 || occurences == 3 || occurences == 4) {
                        candidatesWithAtLeastTwoOccurences.add(number);
                    }
                }
                if (candidatesWithAtLeastTwoOccurences.size() >= 4) {
                    List<List<Integer>> possibleQuads = Utils.findAllPossibleTuples(candidatesWithAtLeastTwoOccurences, 4);
                    for (List<Integer> possibleQuad : possibleQuads) {
                        List<ChangeLogUnitMember> quadTabs = new ArrayList<>();
                        List<Tab> shamTabs = new ArrayList<>();
                        List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                        for (Tab tab : houseTabs) {
                            if (Utils.containsAtLeastXCandidates(tab.getCandidates(), possibleQuad, 1)) {
                                if (Utils.containsAtLeastXCandidates(tab.getCandidates(), possibleQuad, 2)) {
                                    quadTabs.add(tab);
                                } else {
                                    shamTabs.add(tab);
                                }
                            }
                        }
                        if (quadTabs.size() == 4 && shamTabs.isEmpty()) {
                            List<Change> unitSkimmings = new ArrayList<>();
                            for (ChangeLogUnitMember unitMember : quadTabs) {
                                Tab tab = (Tab) unitMember;
                                List<Integer> candidatesToBeRemoved = tab.getCandidates().stream().filter(x -> !possibleQuad.contains(x)).collect(Collectors.toList());
                                tab.getCandidates().removeAll(candidatesToBeRemoved);
                                if (!candidatesToBeRemoved.isEmpty()) {
                                    Skimming skimming = Skimming.builder()
                                            .solvingTechnique(HIDDEN_QUAD)
                                            .house(house)
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
                                        .unitExamined(possibleQuad)
                                        .house(house)
                                        .houseNumber(houseNumber)
                                        .unitMembers(quadTabs)
                                        .solvingTechnique(HIDDEN_QUAD)
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
            System.out.println("Exception in HIDDEN QUADRUPLE " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }
}