package com.gianfro.games.solving.techniques;

import com.gianfro.games.entities.*;

import java.util.*;

public class Naked1 {

    public static final String NAKED_SINGLE = "NAKED SINGLE";

    public static SkimmingResult check(List<Tab> tabs) {
        try {
            Set<ChangeLog> changeLogs = new HashSet<>();
            for (Tab tab : tabs) {
                if (tab.getNumbers().size() == 1) {
                    Change change = new Change(NAKED_SINGLE, null, tab.getRow(), tab.getCol(), tab.getNumbers().get(0));
                    changeLogs.add(new ChangeLog(null, null, 0, Arrays.asList((ChangeLogUnitMember) tab), NAKED_SINGLE, null, Arrays.asList(change)));
                }
            }
            return new SkimmingResult(tabs, new ArrayList<>(changeLogs));
        } catch (Exception e) {
            System.out.println("Exception in NAKED SINGLE: " + e.getMessage());
            return null;
        }
    }
}
