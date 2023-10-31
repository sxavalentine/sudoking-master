package com.gianfro.games.solving.techniques;

import com.gianfro.games.entities.*;
import com.gianfro.games.explainers.SudokuExplainer;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Naked4 {

    /**
     * If you can find four cells, all in the same house, that have only the same four candidates left,
     * you can eliminate that candidates from all other cells in that house.
     * It is important to note that not all cells must contain all four candidates,
     * but there must not be more than four candidates in the four cells all together.
     */

    public static final String NAKED_QUAD = "NAKED QUAD";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = nakedQuadruples(House.BOX, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        result = nakedQuadruples(House.ROW, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        result = nakedQuadruples(House.COL, tabs);
        changeLogs.addAll(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    // TODO algoritmo SICURAMENTE da modificare: non e' detto che tutti i membri di una quadrupla siano in una singola casella
    private static SkimmingResult nakedQuadruples(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int houseNumber : Utils.NUMBERS) {
                List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                Set<List<Integer>> quadruples = new HashSet<>();
                List<Change> unitSkimmings = new ArrayList<>();
                boolean deductionsDone = false;
                for (Tab tab : houseTabs) {
                    if (tab.getNumbers().size() == 4) {
                        quadruples.add(tab.getNumbers());
                    }
                }
                for (List<Integer> quad : quadruples) {
                    List<ChangeLogUnitMember> quadTabs = houseTabs.stream().filter(tab -> Utils.candidatesAreSameOrSubset(tab, quad)).collect(Collectors.toList());
                    if (quadTabs.size() == 4) {
                        for (Tab tab : houseTabs) {
                            if (!quadTabs.contains(tab)) {
                                List<Integer> candidatesToBeRemoved = quad.stream().filter(x -> tab.getNumbers().remove(x)).collect(Collectors.toList());
                                if (!candidatesToBeRemoved.isEmpty()) {
                                    Skimming skimming = new Skimming(NAKED_QUAD, house, tab, candidatesToBeRemoved);
                                    unitSkimmings.add(skimming);
                                    deductionsDone = true;
                                }
                            }
                        }
                        if (deductionsDone) {
                            changeLogs.add(new ChangeLog(quad, house, houseNumber, quadTabs, NAKED_QUAD, null, unitSkimmings));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in NAKED QUADRUPLE " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST NAKED QUADRUPLE -----------------------------------------");

//		Sudoku sudoku = SudokuList.TEST_NAKED_4_BOX;
        Sudoku sudoku = SudokuList.TEST_NAKED_4_ROW;
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        SkimmingResult result = check(tabs);
        List<ChangeLog> changeLogs =
                result.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(NAKED_QUAD))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            SudokuExplainer.explainChange(changeLog);
            System.out.println();
        }
    }
}