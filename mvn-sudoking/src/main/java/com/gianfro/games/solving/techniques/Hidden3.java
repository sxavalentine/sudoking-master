package com.gianfro.games.solving.techniques;

import com.gianfro.games.entities.*;
import com.gianfro.games.explainers.SudokuExplainer;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Hidden3 {

    /**
     * If there are three cells within a house such as that three candidates appear nowhere outside those cells in that house,
     * those three candidates must be placed in the three cells. All other candidates can therefore be eliminated.
     */

    public static final String HIDDEN_TRIPLE = "HIDDEN TRIPLE";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;
        List<ChangeLog> changeLogs = new LinkedList<>();

        result = hiddenTriples(House.BOX, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenTriples(House.ROW, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenTriples(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult hiddenTriples(House house, List<Tab> tabs) {
        try {
            List<ChangeLog> changeLogs = new LinkedList<>();
            for (int houseNumber : Utils.NUMBERS) {
                List<Integer> candidatesWithAtLeastTwoOccurences = new ArrayList<>();
                for (int number : Utils.NUMBERS) {
                    int occurences = 0;
                    List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                    for (Tab tab : houseTabs) {
                        if (tab.getNumbers().contains(number)) {
                            occurences++;
                        }
                    }
                    if (occurences == 2 || occurences == 3) {
                        candidatesWithAtLeastTwoOccurences.add(number);
                    }
                }
                if (candidatesWithAtLeastTwoOccurences.size() >= 3) {
                    List<List<Integer>> possibleTriples = Utils.findAllPossibleTuples(candidatesWithAtLeastTwoOccurences, 3);
                    for (List<Integer> possibleTriple : possibleTriples) {
                        List<ChangeLogUnitMember> tripleTabs = new ArrayList<>();
                        List<Tab> shamTabs = new ArrayList<>();
                        List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                        for (Tab tab : houseTabs) {
                            if (Utils.containsAtLeastXCandidates(tab.getNumbers(), possibleTriple, 1)) {
                                if (Utils.containsAtLeastXCandidates(tab.getNumbers(), possibleTriple, 2)) {
                                    tripleTabs.add((ChangeLogUnitMember) tab);
                                } else {
                                    shamTabs.add(tab);
                                }
                            }
                        }
                        if (tripleTabs.size() == 3 && shamTabs.isEmpty()) {
                            List<Change> unitSkimmings = new ArrayList<>();
                            for (ChangeLogUnitMember unitMember : tripleTabs) {
                                Tab tab = (Tab) unitMember;
                                List<Integer> candidatesToBeRemoved = tab.getNumbers().stream().filter(x -> !possibleTriple.contains(x)).collect(Collectors.toList());
                                tab.getNumbers().removeAll(candidatesToBeRemoved);
                                if (!candidatesToBeRemoved.isEmpty()) {
                                    unitSkimmings.add(new Skimming(HIDDEN_TRIPLE, house, tab, candidatesToBeRemoved));
                                }
                            }
                            if (!unitSkimmings.isEmpty()) {
                                changeLogs.add(new ChangeLog(possibleTriple, house, houseNumber, tripleTabs, HIDDEN_TRIPLE, null, unitSkimmings));
                            }
                        }
                    }
                }
            }
            return new SkimmingResult(tabs, changeLogs);
        } catch (Exception e) {
            System.out.println("Exception in HIDDEN TRIPLE " + house + ": " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST HIDDEN TRIPLES -----------------------------------------");

//		Sudoku sudoku = SudokuList.TEST_HIDDEN_3_BOX;
//		Sudoku sudoku = SudokuList.TEST_HIDDEN_3_ROW;
        Sudoku sudoku = SudokuList.TEST_HIDDEN_3_COL;
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        SkimmingResult result = check(tabs);
        List<ChangeLog> changeLogs =
                result.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(HIDDEN_TRIPLE))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            SudokuExplainer.explainChange(changeLog);
            System.out.println();
        }
    }
}