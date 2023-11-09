package com.gianfro.games.solving.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.exceptions.InvalidHouseException;
import com.gianfro.games.exceptions.NoPossibleChainException;
import com.gianfro.games.exceptions.XChainException;
import com.gianfro.games.solving.techniques.advanced.utils.ChainUtils;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class XChain {

    /**
     * X CHAINS must start and end with a strong link. A strong link is a "If A is false then B is true". So they always need to have an even numbers of links"
     */

    public static final String CHAIN = "CHAIN";
    public static final String X_CHAIN = "X CHAIN";


    public static SkimmingResult check(List<Tab> tabs) {
        SkimmingResult result;

        result = chain(House.BOX, tabs);
        List<ChangeLog> changeLogs = new LinkedList<>(result.getChangeLogs());
        tabs = result.getTabs();

        if (changeLogs.isEmpty()) {
            result = chain(House.ROW, tabs);
            changeLogs.addAll(result.getChangeLogs());
            tabs = result.getTabs();
        }

        if (changeLogs.isEmpty()) {
            result = chain(House.COL, tabs);
            changeLogs.addAll(result.getChangeLogs());
            tabs = result.getTabs();
        }

        return new SkimmingResult(tabs, changeLogs);
    }


    /**
     * Try to form a chain starting from a House (BOX, ROW o COL) with only two cells able to fit a candidate.
     * Those two cells will be the first and second link of the chain (respectively false and true)
     */
    private static SkimmingResult chain(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            List<ChangeLogUnitMember> unitMembers = new ArrayList<>();
            for (int number : Utils.NUMBERS) {
                for (int houseNumber : Utils.NUMBERS) {
                    List<Tab> welcomingTabs = new ArrayList<>();
                    List<Tab> houseTabs = Utils.getHouseTabs(house, houseNumber, tabs);
                    for (Tab tab : houseTabs) {
                        if (tab.getNumbers().contains(number)) {
                            welcomingTabs.add(tab);
                        }
                    }
                    if (welcomingTabs.size() == 2) {
                        List<Link> chain = new ArrayList<>();

                        Tab tab1 = welcomingTabs.get(0);
                        Tab tab2 = welcomingTabs.get(1);

                        Link link1 = new Link(tab1, false, number);
                        Link link2 = new Link(tab2, true, number);

                        chain.add(link1);
                        chain.add(link2);

                        try {
                            chain = findNegativeChain(chain, link2, link1, tabs, number);
//                            if (chain.get(chain.size() - 1).getTab() == link1.getTab()) {
//                                for (Link link : chain) {
//                                    unitMembers.add(link);
//                                }
//                                Change change = new Change(CHAIN, house, tab1.getRow(), tab1.getCol(), number);
//                                ChangeLog changeLog = new ChangeLog(
//                                        Collections.singletonList(number),
//                                        house,
//                                        houseNumber,
//                                        unitMembers,
//                                        CHAIN,
//                                        DISCONTINUOUS_NICE_LOOP_FALSE,
//                                        Collections.singletonList(change));
//                                changeLogs.add(changeLog);
//                                return new SkimmingResult(tabs, changeLogs);
//                            }
                        } catch (XChainException xce) {
                            List<Change> skimmings = new ArrayList<>();
                            for (Link link : xce.getChain()) {
                                unitMembers.add(link);
                            }

                            for (Tab tab : xce.getTabs()) {
                                tab.getNumbers().remove(Integer.valueOf(number));
                                Skimming skimming = new Skimming(X_CHAIN, house, tab, Collections.singletonList(number));
                                skimmings.add(skimming);
                            }
                            ChangeLog changeLog = new ChangeLog(
                                    Collections.singletonList(number),
                                    house,
                                    houseNumber,
                                    unitMembers,
                                    X_CHAIN,
                                    null,
                                    skimmings);
                            changeLogs.add(changeLog);
                            return new SkimmingResult(tabs, changeLogs);
                        } catch (NoPossibleChainException npce) {
                            // Sometimes, when forming a chain, we get with NoPossibleChainException simply because of the order of the first two link.
                            // One example is the Sudoku 000000206000080109900700000000030090056000000029000000000106500400000030000203000,
                            // where it cn't find the X-CHAIN on 6 (Discontinuous Alternating Nice Loop, length 8):
                            // -6[C5]+6[B4]-6[B1]+6[J1]-6[J8]+6[F8]-6[F5]+6[C5]
                            // simply because C5 was put before B4.
                            // So we try forming a new chain inverting the orders of the first two links.

                            List<Link> reverseChain = new ArrayList<>();

                            reverseChain.add(new Link(tab2, false, number));
                            reverseChain.add(new Link(tab1, true, number));

                            try {
                                reverseChain = findNegativeChain(reverseChain, link1, link2, tabs, number);
//                                if (reverseChain.get(reverseChain.size() - 1).getTab() == link2.getTab()) {
//                                    for (Link link : reverseChain) {
//                                        unitMembers.add(link);
//                                    }
//                                    Change change = new Change(CHAIN, house, tab2.getRow(), tab2.getCol(), number);
//                                    ChangeLog changeLog = new ChangeLog(
//                                            Collections.singletonList(number),
//                                            house,
//                                            houseNumber,
//                                            unitMembers,
//                                            CHAIN,
//                                            DISCONTINUOUS_NICE_LOOP_FALSE,
//                                            Collections.singletonList(change));
//                                    changeLogs.add(changeLog);
//                                    return new SkimmingResult(tabs, changeLogs);
//                                }
                            } catch (XChainException xce) {
                                List<Change> skimmings = new ArrayList<>();
                                for (Link link : xce.getChain()) {
                                    unitMembers.add(link);
                                }

                                for (Tab tab : xce.getTabs()) {
                                    tab.getNumbers().remove(Integer.valueOf(number));
                                    Skimming skimming = new Skimming(X_CHAIN, house, tab, Collections.singletonList(number));
                                    skimmings.add(skimming);
                                }
                                ChangeLog changeLog = new ChangeLog(
                                        Collections.singletonList(number),
                                        house,
                                        houseNumber,
                                        unitMembers,
                                        X_CHAIN,
                                        null,
                                        skimmings);
                                changeLogs.add(changeLog);
                                return new SkimmingResult(tabs, changeLogs);
                            } catch (NoPossibleChainException npce2) {
                                //TODO stavolta non fa nulla
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in CHAIN " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }


    // negative IN QUANTO IL PRIMO ANELLO DELLA CATENA PARTE DAL PRESUPPOSTO CHE NON CONTENGA IL CANDIDATO
    private static List<Link> findNegativeChain(List<Link> chain, Link lastLink, Link linkBeforeLast, List<Tab> tabs, int candidate) {
        List<Tab> seenTabs = getSeenTabs(lastLink.getTab(), tabs);
        for (Tab tab : seenTabs) {
            if (tab.getNumbers().contains(candidate) && tab != linkBeforeLast.getTab()) {
                if (!ChainUtils.chainContainsTab(chain, tab)) {
                    House sharedHouse = getSharedHouse(tab, lastLink);

                    Tab tabForNextLink = lookForWeakLinks(sharedHouse, tab, tabs, candidate);
                    if (tabForNextLink != null) {

                        Link newLink = new Link(tab, !lastLink.isOn(), candidate);
                        Link nextLink = new Link(tabForNextLink, !newLink.isOn(), candidate);

                        chain.add(newLink);
                        chain.add(nextLink);

                        /**
                         * Looks for a possible X Chain, by checking if there is a cell that can see both ends of the chain
                         * */
                        List<Tab> xChainTabs = tabs
                                .stream()
                                .filter(t -> ChainUtils.seesCell(t, chain.get(0).getTab(), candidate) && ChainUtils.seesCell(t, tabForNextLink, candidate))
                                .collect(Collectors.toList());

                        if (!xChainTabs.isEmpty()) {
                            throw new XChainException(chain, xChainTabs);
                        }

                        if (nextLink.getTab().equals(chain.get(0).getTab())) {
                            return chain;
                        } else {
                            return findNegativeChain(chain, nextLink, newLink, tabs, candidate);
                        }
                    }
                }
            }
        }
        throw new NoPossibleChainException(chain);
    }

    /**
     * Given a list of Tab and a single Tab, returns the elements of the list that share at least a House with the Tab (except the tab itself)
     */
    private static List<Tab> getSeenTabs(Tab tab, List<Tab> tabs) {
        return tabs
                .stream()
                .filter(t -> t != tab && (t.getBox() == tab.getBox() || t.getRow() == tab.getRow() || t.getCol() == tab.getCol()))
                .collect(Collectors.toList());
    }


    /**
     * Returns the House in common between two Tab
     */
    private static House getSharedHouse(Tab tab, Link link) {
        return
                tab.getBox() == link.getTab().getBox() ? House.BOX :
                        tab.getRow() == link.getTab().getRow() ? House.ROW : House.COL;
    }


    /**
     * Given a starting Tab, a House NOT to be shared and a list of Tab,
     * returns the Tab in the not shared Houses that has only one more cell to host the candidate.
     * The other one will be false
     */
    private static Tab lookForWeakLinks(House house, Tab tab, List<Tab> tabs, int candidate) {

        List<Tab> tabsInSameBox = new ArrayList<>(Utils.getHouseTabs(House.BOX, tab.getBox(), tabs));
        List<Tab> tabsInSameRow = new ArrayList<>(Utils.getHouseTabs(House.ROW, tab.getRow(), tabs));
        List<Tab> tabsInSameCol = new ArrayList<>(Utils.getHouseTabs(House.COL, tab.getCol(), tabs));

        tabsInSameBox = tabsInSameBox.stream().filter(x -> x.getNumbers().contains(candidate) && x != tab).collect(Collectors.toList());
        tabsInSameRow = tabsInSameRow.stream().filter(x -> x.getNumbers().contains(candidate) && x != tab).collect(Collectors.toList());
        tabsInSameCol = tabsInSameCol.stream().filter(x -> x.getNumbers().contains(candidate) && x != tab).collect(Collectors.toList());

        switch (house) {
            case BOX:
                tabsInSameBox.clear();
                break;
            case ROW:
                tabsInSameRow.clear();
                break;
            case COL:
                tabsInSameCol.clear();
                break;
            default:
                throw new InvalidHouseException();
        }

        return
                tabsInSameBox.size() == 1 ? tabsInSameBox.get(0) :
                        tabsInSameRow.size() == 1 ? tabsInSameRow.get(0) :
                                tabsInSameCol.size() == 1 ? tabsInSameCol.get(0) : null;
    }


    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST CHAIN -----------------------------------------");

        Sudoku sudoku;
        sudoku = Utils.buildSudoku(SudokuList.BLOCKED.get(0));
        sudoku = Utils.buildSudoku(SudokuList.DISCOUNTINUOUS_NICE_LOOP);
        sudoku = Utils.buildSudoku(SudokuList.TEST_X_CHAIN_1);
        sudoku = Utils.buildSudoku(SudokuList.TEST_X_CHAIN_2);
        sudoku = Utils.buildSudoku("000000206000080109900700000000030090056000000029000000000106500400000030000203000");

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        tabs = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs).getTabs();
        Utils.megaGrid(sudoku, tabs);
        SkimmingResult result = check(tabs);

        Utils.printChangeLogs(result);
    }
}