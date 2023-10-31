package com.gianfro.games.solving.techniques;

import com.gianfro.games.entities.Change;
import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.SkimmingResult;
import com.gianfro.games.entities.Tab;

import java.util.*;

public class Naked1 {

    public static final String NAKED_SINGLE = "NAKED SINGLE";

    public static SkimmingResult check(List<Tab> tabs) {
        Set<ChangeLog> changeLogs = new HashSet<>();
        try {
            for (Tab tab : tabs) {
                if (tab.getNumbers().size() == 1) {
                    Change change = new Change(NAKED_SINGLE, null, tab.getRow(), tab.getCol(), tab.getNumbers().get(0));
                    changeLogs.add(
                            new ChangeLog(
                                    null,
                                    null,
                                    0,
                                    Collections.singletonList(tab),
                                    NAKED_SINGLE,
                                    null,
                                    Collections.singletonList(change)));
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in NAKED SINGLE: " + e.getMessage());
        }
        return new SkimmingResult(tabs, new ArrayList<>(changeLogs));
    }
}
