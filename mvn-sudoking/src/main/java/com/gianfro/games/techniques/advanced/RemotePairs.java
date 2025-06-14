package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.techniques.advanced.utils.ChainUtils;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RemotePairs {

    public static final String REMOTE_PAIRS = "REMOTE PAIRS";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result = chain(tabs);
        return new SkimmingResult(result.getTabs(), result.getChangeLogs());
    }

    private static SkimmingResult chain(List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {

            List<Tab> bivalueCells = tabs.stream().filter(x -> x.getCandidates().size() == 2).toList();
            List<List<Integer>> bivaluePairs = new ArrayList<>();

            for (Tab tab : bivalueCells) {
                if (!bivaluePairs.contains(tab.getCandidates())) {
                    bivaluePairs.add(tab.getCandidates());
                }
            }

            for (List<Integer> pair : bivaluePairs) {
                List<Tab> pairTabs = bivalueCells.stream().filter(x -> x.getCandidates().equals(pair)).collect(Collectors.toList());

                // CHAIN MUST HAVE AT LEAST 4 LINKS TO ELIMINATE SOMETHING
                if (pairTabs.size() >= 4) {
                    List<Link> chain = new ArrayList<>();
                    Tab firstLinkTab = null;
                    for (Tab tab : pairTabs) {
                        if (countCellsSeen(tab, pairTabs) == 1) {
                            firstLinkTab = tab;
                            break;
                        }
                    }
                    if (firstLinkTab != null) {
                        Link firstLink = new Link(firstLinkTab, false, pair.get(0));
                        chain.add(firstLink);
                        chain = findChain(chain, pairTabs);
                    }

                    List<Tab> skimmedTabs = new ArrayList<>();

                    // this because the chain must have an even number of links
                    // with the Sudoku in Utils.buildSudoku(SudokuList.TEST_REMOTE_PAIRS_1, cell C9 is the last link and wouldn't see F7
                    int trimming = chain.size();
                    while (chain.size() >= 5 && skimmedTabs.isEmpty()) {
                        chain = chain.subList(0, trimming);

                        if ((chain.size() % 2) == 0) {
                            for (Tab t : tabs) {
                                if (!ChainUtils.chainContainsTab(chain, t)
                                        && ChainUtils.seesTwoCellsWithDifferentValue(t, chain)) {
                                    skimmedTabs.add(t);
                                }
                            }
                        }
                        trimming -= 1;
                    }

                    if (!skimmedTabs.isEmpty()) {
                        List<Change> unitSkimmings = new ArrayList<>();
                        for (Tab tab
                                : skimmedTabs) {
                            List<Integer> candidatesToBeRemoved = new ArrayList<>();
                            for (int candidate : tab.getCandidates()) {
                                if (pair.contains(candidate)) {
                                    candidatesToBeRemoved.add(candidate);
                                }
                            }
                            if (tab.getCandidates().removeAll(candidatesToBeRemoved)) {
                                Skimming skimming = Skimming.builder()
                                        .solvingTechnique(REMOTE_PAIRS)
                                        .house(null)
                                        .row(tab.getRow())
                                        .col(tab.getCol())
                                        .number(0)
                                        .tab(tab)
                                        .removedCandidates(candidatesToBeRemoved)
                                        .build();
                                unitSkimmings.add(skimming);
                            }
                        }
                        ChangeLog changeLog = ChangeLog.builder()
                                .unitExamined(pair)
                                .house(null)
                                .houseNumber(0)
                                .unitMembers(new ArrayList<>(chain))
                                .solvingTechnique(REMOTE_PAIRS)
                                .solvingTechniqueVariant(null)
                                .changes(unitSkimmings)
                                .build();
                        changeLogs.add(changeLog);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in REMOTE PAIRS: " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }


    private static List<Link> findChain(List<Link> chain, List<Tab> tabs) {
        Link lastLink = chain.get(chain.size() - 1);
        for (Tab tab : tabs) {
            if (!ChainUtils.chainContainsTab(chain, tab) && ChainUtils.cellsSeeEachOther(tab, lastLink.getTab())) {
                chain.add(new Link(tab, !lastLink.isOn(), lastLink.getNumber()));
                return findChain(chain, tabs);
            }
        }
        return chain;
    }


    private static int countCellsSeen(Tab tab, List<Tab> tabs) {
        List<Tab> cellsSeen =
                tabs
                        .stream()
                        .filter(
                                x -> x != tab
                                        && (x.getBox() == tab.getBox()
                                        || x.getRow() == tab.getRow()
                                        || x.getCol() == tab.getCol()))
                        .toList();
        return cellsSeen.size();
    }


    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST REMOTE PAIRS -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Utils.buildSudoku(SudokuList.TEST_REMOTE_PAIRS_1);
//        sudoku = Utils.buildSudoku(SudokuList.TEST_REMOTE_PAIRS_2);
        sudoku = Utils.buildSudoku("802635017670128350315070826153206780900587231287013065408301572721850603530702108"); //TODO capire se c'è errore

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        tabs = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs).getTabs();
        Utils.megaGrid(sudoku, tabs);

        SkimmingResult result = check(tabs);

        Utils.printChangeLogs(result);
    }
}
