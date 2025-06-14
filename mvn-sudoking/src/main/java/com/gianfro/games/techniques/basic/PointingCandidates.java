package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.*;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class PointingCandidates {

    /**
     * If in a box all candidates of a certain digit are confined to a row or column,
     * that digit cannot appear outside of that box in that row or column.
     */

    public final static String POINTING_CANDIDATES = "POINTING CANDIDATES";
    private final static String POINTING_PAIR = "POINTING PAIR";
    private final static String POINTING_TRIPLE = "POINTING TRIPLE";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = pointingCandidates(House.ROW, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = pointingCandidates(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult pointingCandidates(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int boxNumber : Utils.NUMBERS) {
                for (int number : Utils.NUMBERS) {
                    List<Tab> boxTabs = Utils.getHouseTabs(House.BOX, boxNumber, tabs);
                    List<ChangeLogUnitMember> welcomingTabs = new ArrayList<>();
                    Set<Integer> welcomingHouses = new HashSet<>();
                    for (Tab tab : boxTabs) {
                        if (tab.getCandidates().contains(number)) {
                            welcomingTabs.add(tab);
                            if (house == House.ROW) {
                                welcomingHouses.add(tab.getRow());
                            } else if (house == House.COL) {
                                welcomingHouses.add(tab.getCol());
                            }
                        }
                    }
                    if (welcomingHouses.size() == 1) {
                        String method = welcomingTabs.size() == 2 ? POINTING_PAIR : POINTING_TRIPLE;
                        int welcomingHouse = new ArrayList<>(welcomingHouses).get(0);
                        List<Tab> houseTabs = Utils.getHouseTabs(house, welcomingHouse, tabs);
                        boolean deductionsDone = false;
                        List<Change> unitSkimmings = new ArrayList<>();
                        for (Tab tab : houseTabs) {
                            if (tab.getBox() != boxNumber && tab.getCandidates().contains(number)) {
                                tab.getCandidates().remove(Integer.valueOf(number));
                                Skimming skimming = Skimming.builder()
                                        .solvingTechnique(method)
                                        .house(house)
                                        .row(tab.getRow())
                                        .col(tab.getCol())
                                        .number(0)
                                        .tab(tab)
                                        .removedCandidates(List.of(number))
                                        .build();
                                unitSkimmings.add(skimming);
                                deductionsDone = true;
                            }
                        }
                        if (deductionsDone) {
                            ChangeLog changeLog = ChangeLog.builder()
                                    .unitExamined(List.of(number))
                                    .house(House.BOX)
                                    .houseNumber(boxNumber)
                                    .unitMembers(welcomingTabs)
                                    .solvingTechnique(POINTING_CANDIDATES)
                                    .solvingTechniqueVariant(method)
                                    .changes(unitSkimmings)
                                    .build();
                            changeLogs.add(changeLog);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in POINTING CANDIDATES " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }
}