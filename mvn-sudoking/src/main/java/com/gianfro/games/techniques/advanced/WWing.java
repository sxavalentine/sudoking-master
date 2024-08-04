package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.techniques.advanced.utils.ChainUtils;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class WWing {

    /**
     * If two bi-value cells containing the same candidates are connected by a strong Link on one of their candidates,
     * we can remove the other candidate from any other cell that can see both cells.
     */

    public static final String W_WING = "W WING";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = wWing(tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        return new SkimmingResult(tabs, changeLogs);
    }

    private static SkimmingResult wWing(List<Tab> tabs) {

        List<ChangeLog> changeLogs = new LinkedList<>();

        try {
            List<Tab> biValueCells = tabs.stream().filter(t -> t.getNumbers().size() == 2).collect(Collectors.toList());
            for (Tab t1 : biValueCells) {
                List<Tab> sameCandidatesTabs = tabs.stream().filter(t -> !t.equals(t1) && t.getNumbers().equals(t1.getNumbers())).collect(Collectors.toList());
                for (Tab t4 : sameCandidatesTabs) {
                    List<Link> chain;
                    List<Tab> t1SeenTabs = ChainUtils.getSeenCells(t1, tabs.stream().filter(t -> !t.equals(t4)).collect(Collectors.toList()));
                    List<Tab> t4SeenTabs = ChainUtils.getSeenCells(t4, tabs.stream().filter(t -> !t.equals(t1)).collect(Collectors.toList()));

                    for (Tab t2 : t1SeenTabs) {
                        for (Tab t3 : t4SeenTabs) {
                            // I search for two tabs (one that can see t1, one that can see t4), that see each other
                            if (ChainUtils.cellsSeeEachOther(t2, t3)) {
                                // if they do, I check what candidate they share that is also a t1 candidate (and consequentially t4)
                                Integer middleLinksCandidate = t2.getNumbers().stream().filter(i -> t1.getNumbers().contains(i)).findFirst().orElse(null);
                                // then, I check what House they share, so that I can check they are the only two cells in that house with that candidate
                                House sharedHouse = ChainUtils.getSharedHouse(t2, t3);
                                List<Tab> sharedHouseTabs = tabs.stream().
                                        filter(t ->
                                                t.getNumbers().contains(middleLinksCandidate) &&
                                                        (sharedHouse.equals(House.BOX) ? t.getBox() == t2.getBox() :
                                                                sharedHouse.equals(House.ROW) ? t.getRow() == t2.getRow() :
                                                                        t.getCol() == t2.getCol()))
                                        .collect(Collectors.toList());
                                if (sharedHouseTabs.size() == 2 && sharedHouseTabs.contains(t2) && sharedHouseTabs.contains(t3)) {
                                    Link l1 = new Link(t1, false, middleLinksCandidate);
                                    Link l2 = new Link(t2, true, middleLinksCandidate);
                                    Link l3 = new Link(t3, false, middleLinksCandidate);
                                    Link l4 = new Link(t4, true, middleLinksCandidate);
                                    chain = Arrays.asList(l1, l2, l3, l4);

                                    Integer outerLinksCandidate = t1.getNumbers().stream().filter(n -> !n.equals(middleLinksCandidate)).findFirst().orElse(null);
                                    List<Change> skimmings = new LinkedList<>();

                                    for (Tab tab : tabs) {
                                        if (ChainUtils.seesCell(tab, t1, outerLinksCandidate) && ChainUtils.seesCell(tab, t4, outerLinksCandidate)) {
                                            tab.getNumbers().remove(outerLinksCandidate);
                                            Skimming skimming = new Skimming(W_WING, null, tab, Collections.singletonList(outerLinksCandidate));
                                            skimmings.add(skimming);
                                        }
                                    }

                                    if (!skimmings.isEmpty()) {
                                        List<ChangeLogUnitMember> unitMembers = new LinkedList<>(chain);
                                        ChangeLog changeLog = new ChangeLog(
                                                Collections.singletonList(outerLinksCandidate),
                                                null,
                                                0,
                                                unitMembers,
                                                W_WING,
                                                null,
                                                skimmings);
                                        changeLogs.add(changeLog);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in W WING: " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST W WING -----------------------------------------");

        Sudoku sudoku;
        sudoku = Utils.buildSudoku(SudokuList.W_WING_1);
//        sudoku = Utils.buildSudoku(SudokuList.W_WING_2);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        Utils.megaGrid(sudoku, tabs);

        SkimmingResult result = check(tabs);

        Utils.printChangeLogs(result);
    }
}
