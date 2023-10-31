package com.gianfro.games.solving.techniques;

import com.gianfro.games.entities.*;
import com.gianfro.games.explainers.SudokuExplainer;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class ClaimingCandidates {

    /**
     * If in a row (or column) all candidates of a certain digit are confined to one box,
     * that candidate that be eliminated from all other cells in that box.
     */

    public static final String CLAIMING_CANDIDATES = "CLAIMING CANDIDATES";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;
        List<ChangeLog> changeLogs = new LinkedList<>();

        result = boxLineReduction(House.ROW, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = boxLineReduction(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult boxLineReduction(House house, List<Tab> tabs) {
        try {
            List<ChangeLog> changeLogs = new LinkedList<>();
            for (int houseNumber : Utils.NUMBERS) {
                for (int number : Utils.NUMBERS) {
                    List<ChangeLogUnitMember> welcomingTabs = new ArrayList<>();
                    Set<Integer> boxesOfWelcomingTabs = new HashSet<>();
                    List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                    for (Tab tab : houseTabs) {
                        if (tab.getNumbers().contains(number)) {
                            welcomingTabs.add((ChangeLogUnitMember) (tab));
                            boxesOfWelcomingTabs.add(tab.getBox());
                        }
                    }
                    if (boxesOfWelcomingTabs.size() == 1) {
                        int welcomingBox = new ArrayList<>(boxesOfWelcomingTabs).get(0);
                        List<Change> unitSkimmings = new ArrayList<>();
                        boolean deductionsDone = false;
                        for (Tab tab : tabs) {
                            if (tab.getHouseNumber(house) != houseNumber && tab.getBox() == welcomingBox && tab.getNumbers().contains(number)) {
                                tab.getNumbers().remove(new Integer(number));
                                Skimming s = new Skimming(CLAIMING_CANDIDATES, house, tab, Arrays.asList(number));
                                unitSkimmings.add(s);
                                deductionsDone = true;
                            }
                        }
                        if (deductionsDone) {
                            changeLogs.add(new ChangeLog(Arrays.asList(number), house, houseNumber, welcomingTabs, CLAIMING_CANDIDATES, null, unitSkimmings));
                        }
                    }
                }
            }
            return new SkimmingResult(tabs, changeLogs);
        } catch (Exception e) {
            System.out.println("Exception in CLAIMING CANDIDATES " + house + ": " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST CLAIMING CANDIDATES -----------------------------------------");

//		Sudoku sudoku = SudokuList.TEST_CLAIMING_CANDIDATES_ROW;
        Sudoku sudoku = SudokuList.TEST_CLAIMING_CANDIDATES_COL;
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        SkimmingResult result = check(tabs);
        List<ChangeLog> changeLogs =
                result.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(CLAIMING_CANDIDATES))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            SudokuExplainer.explainChange(changeLog);
            System.out.println();
        }
    }
}