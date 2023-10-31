package com.gianfro.games.solving.techniques;

import com.gianfro.games.entities.*;
import com.gianfro.games.exceptions.InvalidHouseException;
import com.gianfro.games.explainers.SudokuExplainer;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Hidden1 {

    /**
     * Hidden Single means that for a given digit and house only one cell is left to place that digit.
     * The cell itself has more than one candidate left, the correct digit is thus hidden amongst the rest.
     */

    public static final String HIDDEN_SINGLE = "HIDDEN SINGLE";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;
        Set<ChangeLog> changeLogs = new HashSet<>();

        result = hiddenSingle(House.BOX, tabs);
        changeLogs.addAll(result.getChangeLogs());
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
                        List<Tab> welcomingTabs = houseTabs.stream().filter(x -> x.getNumbers().contains(number)).collect(Collectors.toList());

                        if (welcomingTabs.size() == 1) {

                            Tab tab = welcomingTabs.get(0);
                            Change change = new Change(HIDDEN_SINGLE, house, tab.getRow(), tab.getCol(), number);
                            changeLogs.add(new ChangeLog(null, house, houseNumber, Arrays.asList((ChangeLogUnitMember) tab), HIDDEN_SINGLE, null, Arrays.asList(change)));
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
            int houseNumber = 0;
            switch (house) {
                case BOX:
                    houseNumber = tab.getBox();
                    break;
                case ROW:
                    houseNumber = tab.getRow();
                    break;
                case COL:
                    houseNumber = tab.getCol();
                    break;
                default:
                    throw new InvalidHouseException();
            }
            if (houseNumber == houseNumberInput) {
                houseNumbers.removeAll(tab.getNumbers());
            }
        }
        return houseNumbers;
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST HIDDEN SINGLE -----------------------------------------");

        Sudoku sudoku = SudokuList.TEST_HIDDEN_1_BOX;
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        SkimmingResult result = check(tabs);
        List<ChangeLog> changeLogs =
                result.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(HIDDEN_SINGLE))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            SudokuExplainer.explainChange(changeLog);
            System.out.println();
        }
    }
}
