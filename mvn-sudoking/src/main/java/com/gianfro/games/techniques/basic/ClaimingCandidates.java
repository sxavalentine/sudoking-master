package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.*;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class ClaimingCandidates {

    /**
     * If in a row (or column) all candidates of a certain digit are confined to one box,
     * that candidate that be eliminated from all other cells in that box.
     */

    public static final String CLAIMING_CANDIDATES = "CLAIMING CANDIDATES";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = boxLineReduction(House.ROW, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = boxLineReduction(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult boxLineReduction(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int houseNumber : Utils.NUMBERS) {
                for (int number : Utils.NUMBERS) {
                    List<ChangeLogUnitMember> welcomingTabs = new ArrayList<>();
                    Set<Integer> boxesOfWelcomingTabs = new HashSet<>();
                    List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                    for (Tab tab : houseTabs) {
                        if (tab.getCandidates().contains(number)) {
                            welcomingTabs.add(tab);
                            boxesOfWelcomingTabs.add(tab.getBox());
                        }
                    }
                    if (boxesOfWelcomingTabs.size() == 1) {
                        int welcomingBox = new ArrayList<>(boxesOfWelcomingTabs).get(0);
                        List<Change> unitSkimmings = new ArrayList<>();
                        boolean deductionsDone = false;
                        for (Tab tab : tabs) {
                            if (tab.getHouseNumber(house) != houseNumber && tab.getBox() == welcomingBox && tab.getCandidates().contains(number)) {
                                tab.getCandidates().remove(Integer.valueOf(number));
                                Skimming skimming = Skimming.builder()
                                        .solvingTechnique(CLAIMING_CANDIDATES)
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
                                    .house(house)
                                    .houseNumber(houseNumber)
                                    .unitMembers(welcomingTabs)
                                    .solvingTechnique(CLAIMING_CANDIDATES)
                                    .solvingTechniqueVariant(null)
                                    .changes(unitSkimmings)
                                    .build();
                            changeLogs.add(changeLog);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in CLAIMING CANDIDATES " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }
}