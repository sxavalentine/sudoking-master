package com.gianfro.games.solving.techniques;

import com.gianfro.games.entities.*;
import com.gianfro.games.explainers.SudokuExplainer;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Hidden4 {

    /**
     * If there are four cells within a house such as that four candidates appear nowhere outside those cells in that house,
     * those four candidates must be placed in the four cells. All other candidates can therefore be eliminated.
     */

    public static final String HIDDEN_QUAD = "HIDDEN QUAD";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;
        List<ChangeLog> changeLogs = new LinkedList<>();

        result = hiddenQuadruples(House.BOX, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenQuadruples(House.ROW, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = hiddenQuadruples(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult hiddenQuadruples(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
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
                    if (occurences == 2 || occurences == 3 || occurences == 4) {
                        candidatesWithAtLeastTwoOccurences.add(number);
                    }
                }
                if (candidatesWithAtLeastTwoOccurences.size() >= 4) {
                    List<List<Integer>> possibleQuads = Utils.findAllPossibleTuples(candidatesWithAtLeastTwoOccurences, 4);
                    for (List<Integer> possibleQuad : possibleQuads) {
                        List<ChangeLogUnitMember> quadTabs = new ArrayList<>();
                        List<Tab> shamTabs = new ArrayList<>();
                        List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                        for (Tab tab : houseTabs) {
                            if (Utils.containsAtLeastXCandidates(tab.getNumbers(), possibleQuad, 1)) {
                                if (Utils.containsAtLeastXCandidates(tab.getNumbers(), possibleQuad, 2)) {
                                    quadTabs.add(tab);
                                } else {
                                    shamTabs.add(tab);
                                }
                            }
                        }
                        if (quadTabs.size() == 4 && shamTabs.isEmpty()) {
                            List<Change> unitSkimmings = new ArrayList<>();
                            for (ChangeLogUnitMember unitMember : quadTabs) {
                                Tab tab = (Tab) unitMember;
                                List<Integer> candidatesToBeRemoved = tab.getNumbers().stream().filter(x -> !possibleQuad.contains(x)).collect(Collectors.toList());
                                tab.getNumbers().removeAll(candidatesToBeRemoved);
                                if (!candidatesToBeRemoved.isEmpty()) {
                                    Skimming skimming = new Skimming(HIDDEN_QUAD, house, tab, candidatesToBeRemoved);
                                    unitSkimmings.add(skimming);
                                }
                            }
                            if (!unitSkimmings.isEmpty()) {
                                changeLogs.add(new ChangeLog(possibleQuad, house, houseNumber, quadTabs, HIDDEN_QUAD, null, unitSkimmings));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in HIDDEN QUADRUPLE " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST HIDDEN QUADRUPLES -----------------------------------------");

//		Sudoku sudoku = SudokuList.TEST_HIDDEN_4_BOX;
//		Sudoku sudoku = SudokuList.TEST_HIDDEN_4_ROW;
//		Sudoku sudoku = SudokuList.TEST_HIDDEN_4_COL; // <-in teoria sarebbe in col 9, ma avendo saltato le scremature con gli altri metodi, non e' in grado di trovarlo, ma trova un esempio in col 1
        Sudoku sudoku = Utils.buildSudoku("650087024000649050040025000570438061000501000310902085000890010000213000130750098"); // TEST COL 7 STRONZO ENG RICHIEDE XY-WING
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        SkimmingResult result = check(tabs);
        List<ChangeLog> changeLogs =
                result.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(HIDDEN_QUAD))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            SudokuExplainer.explainChange(changeLog);
            System.out.println();
        }
    }
}