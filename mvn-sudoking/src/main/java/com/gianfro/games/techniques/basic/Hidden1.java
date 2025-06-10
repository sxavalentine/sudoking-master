package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.*;
import com.gianfro.games.utils.Utils;

import java.util.*;

public class Hidden1 {

    /**
     * Hidden Single means that for a given digit and house only one cell is left to place that digit.
     * The cell itself has more than one candidate left, the correct digit is thus hidden amongst the rest.
     */

    public static final String HIDDEN_SINGLE = "HIDDEN SINGLE";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = hiddenSingle(House.BOX, tabs);
        Set<ChangeLog> changeLogs = new HashSet<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenSingle(House.ROW, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenSingle(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, new ArrayList<>(changeLogs));
    }

    private static SkimmingResult hiddenSingle(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int houseNumber : Utils.NUMBERS) {
                for (int number : Utils.NUMBERS) {
                    if (!getHouseNumbers(house, houseNumber, tabs).contains(number)) {
                        List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                        List<Tab> welcomingTabs = houseTabs.stream().filter(x -> x.getNumbers().contains(number)).toList();

                        if (welcomingTabs.size() == 1) {

                            Tab tab = welcomingTabs.get(0);
                            Change change = new Change(HIDDEN_SINGLE, house, tab.getRow(), tab.getCol(), number);
                            changeLogs.add(new ChangeLog(
                                    null,
                                    house,
                                    houseNumber,
                                    Collections.singletonList(tab),
                                    HIDDEN_SINGLE,
                                    null,
                                    Collections.singletonList(change)));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in HIDDEN SINGLE " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }

    private static List<Integer> getHouseNumbers(House house, int houseNumberInput, List<Tab> tabs) {
        List<Integer> houseNumbers = new ArrayList<>(Utils.NUMBERS);
        for (Tab tab : tabs) {
            int houseNumber = switch (house) {
                case BOX -> tab.getBox();
                case ROW -> tab.getRow();
                case COL -> tab.getCol();
            };
            if (houseNumber == houseNumberInput) {
                houseNumbers.removeAll(tab.getNumbers());
            }
        }
        return houseNumbers;
    }
}
