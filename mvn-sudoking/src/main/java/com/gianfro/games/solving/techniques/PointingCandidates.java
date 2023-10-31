package com.gianfro.games.solving.techniques;

import com.gianfro.games.entities.*;
import com.gianfro.games.explainers.SudokuExplainer;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class PointingCandidates {

    /**
     * If in a box all candidates of a certain digit are confined to a row or column,
     * that digit cannot appear outside of that box in that row or column.
     */

    public final static String POINTING_CANDIDATES = "POINTING CANDIDATES";
    private final static String POINTING_PAIR = "POINTING PAIR";
    private final static String POINTING_TRIPLE = "POINTING TRIPLE";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = pointingCandidates(House.ROW, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = pointingCandidates(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult pointingCandidates(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int boxNumber : Utils.NUMBERS) {
                for (int number : Utils.NUMBERS) {
                    List<Tab> boxTabs = Utils.getHouseTabs(House.BOX, boxNumber, tabs);
                    List<ChangeLogUnitMember> welcomingTabs = new ArrayList<>();
                    Set<Integer> welcomingHouses = new HashSet<>();
                    for (Tab tab : boxTabs) {
                        if (tab.getNumbers().contains(number)) {
                            welcomingTabs.add(tab);
                            if (house == House.ROW) {
                                welcomingHouses.add(tab.getRow());
                            } else if (house == House.COL) {
                                welcomingHouses.add(tab.getCol());
                            }
                        }
                    }
                    if (welcomingHouses.size() == 1) {
                        String method = welcomingTabs.size() == 2 ? POINTING_PAIR : POINTING_TRIPLE;
                        int welcomingHouse = new ArrayList<>(welcomingHouses).get(0);
                        List<Tab> houseTabs = Utils.getHouseTabs(house, welcomingHouse, tabs);
                        boolean deductionsDone = false;
                        List<Change> unitSkimmings = new ArrayList<>();
                        for (Tab tab : houseTabs) {
                            if (tab.getBox() != boxNumber && tab.getNumbers().contains(number)) {
                                tab.getNumbers().remove(Integer.valueOf(number));
                                Skimming s = new Skimming(method, house, tab, Collections.singletonList(number));
                                unitSkimmings.add(s);
                                deductionsDone = true;
                            }
                        }
                        if (deductionsDone) {
                            changeLogs.add(new ChangeLog(
                                    Collections.singletonList(number),
                                    House.BOX,
                                    boxNumber,
                                    welcomingTabs,
                                    POINTING_CANDIDATES,
                                    method,
                                    unitSkimmings));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in POINTING CANDIDATES " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST POINTING CANDIDATES -----------------------------------------");

//		Sudoku sudoku = SudokuList.TEST_POINTING_CANDIDATES_ROW;
//		Sudoku sudoku = SudokuList.TEST_POINTING_CANDIDATES_COL;
        Sudoku sudoku = SudokuList.TEST_POINTING_CANDIDATES_TRIPLE;
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        SkimmingResult result = check(tabs);
        List<ChangeLog> changeLogs =
                result.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(POINTING_CANDIDATES))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            SudokuExplainer.explainChange(changeLog);
            System.out.println();
        }
    }
}