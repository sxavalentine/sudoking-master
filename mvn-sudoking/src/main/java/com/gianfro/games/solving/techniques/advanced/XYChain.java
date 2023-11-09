package com.gianfro.games.solving.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.explainers.SudokuExplainer;
import com.gianfro.games.solving.techniques.advanced.utils.ChainUtils;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class XYChain {

    /**
     * XY CHAINS must start and end with a strong link. A strong link is a "If A is false then B is true". So they always need to have an even numbers of links"
     * Più semplicemente, se il primo anello della catena NON è X, allora l'ultimo deve essere X
     */

    public static final String XY_CHAIN = "XY CHAIN";

    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result = chain(tabs);
        return new SkimmingResult(result.getTabs(), result.getChangeLogs());
    }

    private static SkimmingResult chain(List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {

            List<Tab> bivalueCells = tabs.stream().filter(x -> x.getNumbers().size() == 2).collect(Collectors.toList());

            for (Tab tab : bivalueCells) {
                // if importante, perchè nel mentre i suoi candidati potrebbero essere stati rimossi dalle precedenti iterazioni del ciclo
                if (tab.getNumbers().size() == 2) {
                    List<Tab> seenTabs = ChainUtils.getSeenCells(tab, bivalueCells);
                    if (seenTabs.size() >= 2) {
                        for (Integer candidate : tab.getNumbers()) {
                            for (Tab t : seenTabs) {
                                List<Integer> nextLinkCandidates = new ArrayList<>(t.getNumbers());
                                nextLinkCandidates.remove(candidate);
                                if (nextLinkCandidates.size() == 1) {

                                    int nextLinkCandidate = nextLinkCandidates.get(0);
                                    List<Link> chain = new ArrayList<>();

                                    Link firstLink = new Link(tab, true, candidate);
                                    Link secondLink = new Link(t, false, nextLinkCandidate);

                                    chain.add(firstLink);
                                    chain.add(secondLink);

                                    ChangeLog changelog = findChain(chain, bivalueCells, tabs);
                                    if (changelog != null) {
                                        changeLogs.add(changelog);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in XY CHAIN: " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }

    private static ChangeLog findChain(List<Link> chain, List<Tab> tabs, List<Tab> allTabs) {
        Link firstLink = chain.get(0);
        Link lastLink = chain.get(chain.size() - 1);
        int candidateOff = lastLink.getNumber();

        List<Tab> seenTabs = ChainUtils.getSeenCells(lastLink.getTab(), tabs);
        for (Tab tabOn : seenTabs) {
            if (!ChainUtils.chainContainsTab(chain, tabOn) && ChainUtils.cellsSeeEachOther(tabOn, lastLink.getTab())) {

                List<Integer> tabOnCandidates = new ArrayList<>(tabOn.getNumbers());
                tabOnCandidates.remove(Integer.valueOf(candidateOff));
                if (tabOnCandidates.size() == 1) {
                    int candidateOn = tabOnCandidates.get(0);

                    List<Tab> cellOnSeenTabs = ChainUtils.getSeenCells(tabOn, tabs);
                    for (Tab tabOff : cellOnSeenTabs) {
                        if (!ChainUtils.chainContainsTab(chain, tabOff) && ChainUtils.cellsSeeEachOther(tabOff, tabOn)) {

                            List<Integer> tabOffCandidates = new ArrayList<>(tabOff.getNumbers());

                            tabOffCandidates.remove(Integer.valueOf(candidateOn));
                            if (tabOffCandidates.size() == 1) {
                                candidateOff = tabOffCandidates.get(0);

                                Link linkOn = new Link(tabOn, true, candidateOn);
                                Link linkOff = new Link(tabOff, false, candidateOff);

                                chain.add(linkOn);
                                chain.add(linkOff);

                                List<Integer> sharedCandidates = ChainUtils.getSharedCandidates(firstLink.getTab(), tabOff);
                                if (!sharedCandidates.isEmpty()
                                        && firstLink.getNumber() != linkOff.getNumber()
                                        // TODO testare fix (controlla che il numero off del primo link sia uguale al numero on dell'ultimo)
                                        // si può scrivere meglio e più leggibile però
                                        && firstLink.getTab().getNumbers().stream().filter(x -> x != firstLink.getNumber()).collect(Collectors.toList()).get(0).equals(linkOff.getNumber())
                                        //
                                        && (sharedCandidates.get(0) == firstLink.getNumber() || sharedCandidates.get(0) == linkOff.getNumber())) {
                                    List<Tab> commonCells = getCommonCells(chain, allTabs);
                                    if (!commonCells.isEmpty()) {

                                        List<Change> skimmings = new ArrayList<>();
                                        for (Tab tab : commonCells) {
                                            tab.getNumbers().remove(sharedCandidates.get(0));
                                            Skimming skimming = new Skimming(XY_CHAIN, null, tab, Collections.singletonList(sharedCandidates.get(0)));
                                            skimmings.add(skimming);
                                        }

                                        return new ChangeLog(
                                                Collections.singletonList(sharedCandidates.get(0)),
                                                null,
                                                0,
                                                new ArrayList<>(chain),
                                                XY_CHAIN,
                                                null,
                                                skimmings);
                                    }
                                }
                                return findChain(chain, tabs, allTabs);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static List<Tab> getCommonCells(List<Link> chain, List<Tab> tabs) {
        Tab firstLinkTab = chain.get(0).getTab();
        Tab lastLinkTab = chain.get(chain.size() - 1).getTab();
        int candidate = ChainUtils.getSharedCandidates(firstLinkTab, lastLinkTab).get(0);
        return tabs
                .stream()
                .filter(tab -> tab.getNumbers().contains(candidate)
                        && !ChainUtils.chainContainsTab(chain, tab)
                        && ChainUtils.cellsSeeEachOther(tab, firstLinkTab)
                        && ChainUtils.cellsSeeEachOther(tab, lastLinkTab))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST XY CHAIN -----------------------------------------");

        Sudoku sudoku;
        sudoku = Utils.buildSudoku(SudokuList.TEST_XY_CHAIN_1);
        sudoku = Utils.buildSudoku(SudokuList.TEST_XY_CHAIN_2);
        sudoku = Utils.buildSudoku(SudokuList.TEST_XY_CHAIN_3);
        sudoku = Utils.buildSudoku("802635017670128350315070826153206700900587231287013065408301572721850603530702100"); //TODO FIX ERRORE
        Utils.grid(sudoku);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        tabs = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs).getTabs();
        SkimmingResult result = check(tabs);
        List<ChangeLog> changeLogs =
                result.getChangeLogs()
                        .stream()
                        .filter(x -> x.getSolvingTechnique().equals(XY_CHAIN))
                        .collect(Collectors.toList());

        for (ChangeLog changeLog : changeLogs) {
            SudokuExplainer.explainChange(changeLog);
            System.out.println();
        }
    }
}