package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.Change;
import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.SkimmingResult;
import com.gianfro.games.entities.Tab;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Naked1 {

    public static final String NAKED_SINGLE = "NAKED SINGLE";

    public static SkimmingResult check(List<Tab> tabs) {
        Set<ChangeLog> changeLogs = new HashSet<>();
        try {
            for (Tab tab : tabs) {
                if (tab.getCandidates().size() == 1) {
                    Change change = Change.builder()
                            .solvingTechnique(NAKED_SINGLE)
                            .house(null)
                            .row(tab.getRow())
                            .col(tab.getCol())
                            .number(tab.getCandidates().get(0))
                            .build();
                    ChangeLog changeLog = ChangeLog.builder()
                            .unitExamined(null)
                            .house(null)
                            .houseNumber(0)
                            .unitMembers(List.of(tab))
                            .solvingTechnique(NAKED_SINGLE)
                            .solvingTechniqueVariant(null)
                            .changes(List.of(change))
                            .build();
                    changeLogs.add(changeLog);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in NAKED SINGLE: " + e.getMessage());
        }
        return new SkimmingResult(tabs, new ArrayList<>(changeLogs));
    }
}
