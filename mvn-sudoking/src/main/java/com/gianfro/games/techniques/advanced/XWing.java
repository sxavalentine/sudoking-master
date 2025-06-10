package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;
import org.paukov.combinatorics3.Generator;

import java.util.*;
import java.util.stream.Collectors;

public class XWing {

    /**
     * If you can find two rows, such that all candidates of a specific digit (the fish digit) in both rows are contained in
     * the columns (the cover sets), all fish candidates in the columns that are not part of the rows can be eliminated.
     * The result is called an X-Wing in the rows.
     * If you exchange the terms rows and columns in the description above, you get an X-Wing in the columns.
     */

    public static final String X_WING = "X WING";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = xWing(House.ROW, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = xWing(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult xWing(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {

            for (int number : Utils.NUMBERS) {
                List<List<Tab>> tabsPair = new ArrayList<>();
                for (int houseNumber : Utils.NUMBERS) {
                    List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                    List<Tab> welcomingTabs = houseTabs.stream().filter(tab -> tab.getNumbers().contains(number)).toList();

                    if (welcomingTabs.size() == 2) {
                        tabsPair.add(new ArrayList<>(welcomingTabs));
                    }
                }
                if (tabsPair.size() >= 2) {

                    List<List<List<Tab>>> allPossibleCouples = findAllPossibleTabsTuples(tabsPair, 2);

                    for (List<List<Tab>> tabsCouple : allPossibleCouples) {

                        Tab firstTabPair1 = tabsCouple.get(0).get(0);
                        Tab firstTabPair2 = tabsCouple.get(1).get(0);

                        Tab secondTabPair1 = tabsCouple.get(0).get(1);
                        Tab secondTabPair2 = tabsCouple.get(1).get(1);

                        int firstTabPair1house = house == House.ROW ? firstTabPair1.getCol() : firstTabPair1.getRow();
                        int firstTabPair2house = house == House.ROW ? firstTabPair2.getCol() : firstTabPair2.getRow();

                        int secondTabPair1house = house == House.ROW ? secondTabPair1.getCol() : secondTabPair1.getRow();
                        int secondTabPair2house = house == House.ROW ? secondTabPair2.getCol() : secondTabPair2.getRow();

                        if (firstTabPair1house == firstTabPair2house && secondTabPair1house == secondTabPair2house) {

                            List<Change> unitSkimmings = new ArrayList<>();

                            List<ChangeLogUnitMember> xWingMembers = Arrays.asList(
                                    firstTabPair1,
                                    firstTabPair2,
                                    secondTabPair1,
                                    secondTabPair2);

                            List<Tab> tabsHouseFirstMembersPairs = Utils.getHouseTabs(house == House.ROW ? House.COL : House.ROW, firstTabPair1house, tabs);
                            List<Tab> tabsHouseSecondMembersPairs = Utils.getHouseTabs(house == House.ROW ? House.COL : House.ROW, secondTabPair1house, tabs);

                            List<Tab> firstMembers = Arrays.asList(firstTabPair1, firstTabPair2);
                            List<Tab> secondMembers = Arrays.asList(secondTabPair1, secondTabPair2);


                            for (Tab tab : tabsHouseFirstMembersPairs) {
                                if (!firstMembers.contains(tab)) {
                                    if (tab.getNumbers().remove(Integer.valueOf(number))) {
                                        Skimming skimming = new Skimming(X_WING, house, tab, Collections.singletonList(number));
                                        unitSkimmings.add(skimming);
                                    }
                                }
                            }
                            for (Tab tab : tabsHouseSecondMembersPairs) {
                                if (!secondMembers.contains(tab)) {
                                    if (tab.getNumbers().remove(Integer.valueOf(number))) {
                                        Skimming skimming = new Skimming(X_WING, house, tab, Collections.singletonList(number));
                                        unitSkimmings.add(skimming);
                                    }
                                }
                            }
                            if (!unitSkimmings.isEmpty()) {
                                changeLogs.add(new ChangeLog(
                                        Collections.singletonList(number),
                                        null,
                                        0,
                                        xWingMembers,
                                        X_WING,
                                        null,
                                        unitSkimmings));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in X WING " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }

    /**
     * Given a list of lists of tabs and an N number, returns all the possible tuples of N elements obtainable with that set
     */
    private static List<List<List<Tab>>> findAllPossibleTabsTuples(List<List<Tab>> tabsList, int n) {
        return Generator
                .combination(tabsList)
                .simple(n)
                .stream()
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST X WING -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Utils.buildSudoku(SudokuList.TEST_X_WING_ROW);
        sudoku = Utils.buildSudoku(SudokuList.TEST_X_WING_COL);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.megaGrid(sudoku, tabs);

        SkimmingResult result = check(tabs);
        Utils.printChangeLogs(result);
    }
}
