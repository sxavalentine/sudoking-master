package com.gianfro.games.solving.techniques;

import com.gianfro.games.entities.*;
import com.gianfro.games.explainers.SudokuExplainer;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Naked2 {

    /**
     * If you can find two cells, both in the same house, that have only the same two candidates left,
     * you can eliminate that two candidates from all other cells in that house.
     */

    public static final String NAKED_PAIR = "NAKED PAIR";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = nakedPairs(House.BOX, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = nakedPairs(House.ROW, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = nakedPairs(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult nakedPairs(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int houseNumber : Utils.NUMBERS) {
                Set<List<Integer>> pairsSet = new HashSet<>();
                List<List<Integer>> pairsList = new ArrayList<>();
                List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                for (Tab tab : houseTabs) {
                    if (tab.getNumbers().size() == 2) {
                        pairsSet.add(tab.getNumbers());
                        pairsList.add(tab.getNumbers());
                    }
                }
                List<List<Integer>> pairs = new ArrayList<>(pairsSet);
                for (List<Integer> pair : pairs) {
                    List<ChangeLogUnitMember> pairTabs = new ArrayList<>();
                    if (Collections.frequency(pairsList, pair) == 2) {
                        List<Tab> houseTabs2 = Utils.getHouseTabs(house, houseNumber, tabs);
                        List<Change> unitSkimmings = new ArrayList<>();
                        boolean deductionsDone = false;
                        for (Tab tab : houseTabs2) {
                            if (!tab.getNumbers().equals(pair)) {
                                List<Integer> candidatesToBeRemoved = pair.stream().filter(x -> tab.getNumbers().remove(x)).collect(Collectors.toList());
                                if (!candidatesToBeRemoved.isEmpty()) {
                                    Skimming skimming = new Skimming(NAKED_PAIR, house, tab, candidatesToBeRemoved);
                                    unitSkimmings.add(skimming);
                                    deductionsDone = true;
                                }
                            } else {
                                pairTabs.add(tab);
                            }
                        }
                        if (deductionsDone) {
                            changeLogs.add(new ChangeLog(pair, house, houseNumber, pairTabs, NAKED_PAIR, null, unitSkimmings));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in NAKED PAIR " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST NAKED PAIRS:  -----------------------------------------");

        Sudoku sudoku = SudokuList.TEST_NAKED_2_ALL;
        Utils.grid(sudoku);

        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        SkimmingResult result = check(tabs);
        List<ChangeLog> changeLogs =
                result.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(NAKED_PAIR))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            SudokuExplainer.explainChange(changeLog);
            System.out.println();
        }
    }
}
