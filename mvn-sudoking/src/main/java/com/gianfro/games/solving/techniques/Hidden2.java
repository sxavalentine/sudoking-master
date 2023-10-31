package com.gianfro.games.solving.techniques;

import com.gianfro.games.entities.*;
import com.gianfro.games.explainers.SudokuExplainer;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Hidden2 {

    /**
     * If there are two cells within a house such as that two candidates appear nowhere outside those cells in that house,
     * those two candidates must be placed in the two cells. All other candidates can therefore be eliminated.
     */

    public static final String HIDDEN_PAIR = "HIDDEN PAIR";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = hiddenPairs(House.BOX, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenPairs(House.ROW, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenPairs(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult hiddenPairs(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int houseNumber : Utils.NUMBERS) {
                List<Integer> candidatesWithTwoOccurences = new ArrayList<>();
                for (int number : Utils.NUMBERS) {
                    int occurences = 0;
                    List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                    for (Tab tab : houseTabs) {
                        if (tab.getNumbers().contains(number)) {
                            occurences++;
                        }
                    }
                    if (occurences == 2) {
                        candidatesWithTwoOccurences.add(number);
                    }
                }
                if (candidatesWithTwoOccurences.size() >= 2) {
                    List<List<Integer>> possiblePairs = Utils.findAllPossibleTuples(candidatesWithTwoOccurences, 2);
                    for (List<Integer> possiblePair : possiblePairs) {
                        List<ChangeLogUnitMember> pairTabs = new ArrayList<>();
                        List<Tab> shamTabs = new ArrayList<>();
                        List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                        for (Tab tab : houseTabs) {
                            if (Utils.containsAtLeastXCandidates(tab.getNumbers(), possiblePair, 1)) {
                                if (Utils.containsAtLeastXCandidates(tab.getNumbers(), possiblePair, 2)) {
                                    pairTabs.add((ChangeLogUnitMember) tab);
                                } else {
                                    shamTabs.add(tab);
                                }
                            }
                        }
                        if (pairTabs.size() == 2 && shamTabs.isEmpty()) {
                            List<Change> unitSkimmings = new ArrayList<>();
                            for (ChangeLogUnitMember unitMember : pairTabs) {
                                Tab tab = (Tab) unitMember;
                                List<Integer> candidatesToBeRemoved = tab.getNumbers().stream().filter(x -> !possiblePair.contains(x)).collect(Collectors.toList());
                                tab.getNumbers().removeAll(candidatesToBeRemoved);
                                if (!candidatesToBeRemoved.isEmpty()) {
                                    Skimming skimming = new Skimming(HIDDEN_PAIR, house, tab, candidatesToBeRemoved);
                                    unitSkimmings.add(skimming);
                                }
                            }
                            if (!unitSkimmings.isEmpty()) {
                                changeLogs.add(new ChangeLog(possiblePair, house, houseNumber, pairTabs, HIDDEN_PAIR, null, unitSkimmings));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in HIDDEN PAIR " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST HIDDEN PAIRS -----------------------------------------");

        Sudoku sudoku = SudokuList.TEST_HIDDEN_2_BOX; // ROW 1
//		Sudoku sudoku = SudokuList.TEST_HIDDEN_2_B; // COL 9
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        SkimmingResult result = check(tabs);
        List<ChangeLog> changeLogs =
                result.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(HIDDEN_PAIR))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            SudokuExplainer.explainChange(changeLog);
            System.out.println();
        }
    }
}