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

public class Chain {

    /**
     * X CHAINS must start and end with a strong link. A strong link is a "If A is false then B is true". So they always need to have an even numbers of links"
     */

    public static final String CHAIN = "CHAIN";
    public static final String X_CHAIN = "X CHAIN";
    public static final String DISCONTINUOUS_NICE_LOOP_TRUE = "DISCONTINUOUS NICE LOOP (true)";
    public static final String DISCONTINUOUS_NICE_LOOP_FALSE = "DISCONTINUOUS NICE LOOP (false)";


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

        if (changeLogs.isEmpty()) {
            result = chainAll(tabs);
            changeLogs.addAll(result.getChangeLogs());
            tabs = result.getTabs();
        }

        return new SkimmingResult(tabs, changeLogs);
    }


    // prova a formare una catena partendo da una casa (BOX, ROW o COL) che ha solo due celle candidate a ospitare un numero.
    // Essi saranno primo e secondo anello della catena (rispettivamente false e true)
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
                            if (chain.get(chain.size() - 1).getTab() == link1.getTab()) {
                                for (Link link : chain) {
                                    unitMembers.add((ChangeLogUnitMember) link.getTab());
                                }
                                Change change = new Change(CHAIN, house, tab1.getRow(), tab1.getCol(), number);
                                ChangeLog changeLog = new ChangeLog(
                                        Collections.singletonList(number),
                                        house,
                                        houseNumber,
                                        unitMembers,
                                        CHAIN,
                                        DISCONTINUOUS_NICE_LOOP_FALSE,
                                        Collections.singletonList(change));
                                ///
                                //							String cell = Utils.ROWS_LETTERS.get(tab1.getRow() - 1) + tab1.getCol();
                                //							System.out.println("CONTRADICTION: IN THE CHAIN THE CELL " + cell + " WITH THE CANDIDATE " + number + " STARTS FALSE AND ENDS TRUE");
                                //							System.out.println("SO IT MUST BE TRUE");
                                //							for (Link link : chain) {
                                //								System.out.println(link);
                                //							}
                                ///
                                changeLogs.add(changeLog);
                                return new SkimmingResult(tabs, changeLogs);
                            }
                        } catch (XChainException xce) {
                            List<Change> skimmings = new ArrayList<>();
                            for (Link link : xce.getChain()) {
                                unitMembers.add((ChangeLogUnitMember) link.getTab());
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
                                    CHAIN,
                                    DISCONTINUOUS_NICE_LOOP_FALSE,
                                    skimmings);
                            ///
                            //						System.out.println("I FOUND AN X CHAIN");
                            //						for (Link link : xce.getChain()) {
                            //							System.out.println(link);
                            //						}
                            //						for (tab tab : xce.getTabs()) {
                            //							System.out.println(number + " CAN BE REMOVED FROM " + tab);
                            //						}
                            ///
                            changeLogs.add(changeLog);
                            return new SkimmingResult(tabs, changeLogs);

                        } catch (NoPossibleChainException npce) {
                            //TODO che si fa in caso di eccezione?
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
        throw new NoPossibleChainException();
    }


    // prova a formare una catena con qualsiasi tab, a prescindere da quanti candidati abbia. Il primo elemento della catena sara' dato per vero
    private static SkimmingResult chainAll(List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        for (int number : Utils.NUMBERS) {
            for (Tab tab : tabs) {
                List<ChangeLogUnitMember> unitMembers = new ArrayList<>();
                if (tab.getNumbers().contains(number)) {

                    Link link1 = new Link(tab, true, number);
                    List<Link> chain = new ArrayList<>();
                    chain.add(link1);

                    try {
                        chain = findPositiveChain(chain, link1, null, tabs, number);
                        if (chain.get(chain.size() - 1).getTab() == link1.getTab()) {
                            for (Link link : chain) {
                                unitMembers.add(link.getTab());
                            }
                            tab.getNumbers().remove(Integer.valueOf(number));
                            Skimming skimming = new Skimming(CHAIN, House.BOX, tab, Collections.singletonList(number));
                            ChangeLog changeLog = new ChangeLog(
                                    Collections.singletonList(number),
                                    House.BOX,
                                    0,
                                    unitMembers,
                                    CHAIN,
                                    DISCONTINUOUS_NICE_LOOP_TRUE,
                                    Collections.singletonList(skimming));
                            ///
//							String cell = Utils.ROWS_LETTERS.get(tab.getRow() - 1) + tab.getCol();
//							System.out.println("CONTRADICTION: IN THE CHAIN THE CELL " + cell + " WITH THE CANDIDATE " + number + " STARTS TRUE AND ENDS FALSE");
//							System.out.println("SO IT CAN'T CONTAIN " + number);
//							for (Link link : chain) {
//								System.out.println(link);
//							}
                            ///
                            changeLogs.add(changeLog);
                        }
                    } catch (NoPossibleChainException npce) {
                        //TODO che si fa in tal caso?
                    }
                }
            }
        }
        return new SkimmingResult(tabs, changeLogs);
    }


    // positive IN QUANTO IL PRIMO ANELLO DELLA CATENA PARTE DAL PRESUPPOSTO CHE CONTENGA IL CANDIDATO
    private static List<Link> findPositiveChain(List<Link> chain, Link lastLink, Link linkBeforeLast, List<Tab> tabs, int candidate) {
        List<Tab> seenTabs = getSeenTabs(lastLink.getTab(), tabs);
        for (Tab tab : seenTabs) {
            Tab linkBeforeLastTab = linkBeforeLast == null ? null : linkBeforeLast.getTab();
            if (tab.getNumbers().contains(candidate) && tab != linkBeforeLastTab) {

                if (tab.equals(chain.get(0).getTab())) {
                    Link newLink = new Link(tab, !lastLink.isOn(), candidate);
                    chain.add(newLink);
                    return chain;
                } else {
                    List<Link> clonedChain = new ArrayList<>(chain);
                    House sharedHouse = getSharedHouse(tab, lastLink);
                    Tab tabForNextLink = lookForWeakLinks(sharedHouse, tab, tabs, candidate);
                    if (tabForNextLink != null
                            && !ChainUtils.chainContainsTab(clonedChain, tabForNextLink)
                            && !tabForNextLink.equals(lastLink.getTab())
                            && !tabForNextLink.equals(clonedChain.get(0).getTab())) {

                        Link newLink = new Link(tab, !lastLink.isOn(), candidate);
                        Link nextLink = new Link(tabForNextLink, !newLink.isOn(), candidate);

                        clonedChain.add(newLink);
                        clonedChain.add(nextLink);

                        try {
                            clonedChain = findPositiveChain(clonedChain, nextLink, newLink, tabs, candidate);
                            return clonedChain;
                        } catch (NoPossibleChainException npce) {
                            ///
//							System.out.println("NON HO TROVATO IL PROSEGUO DELLA CATENA");
//							for (Link l : clonedChain) {
//								System.out.println(l);
//							}
//							System.out.println("TAB ANALIZZATO: " + tab);
//							System.out.println();
                            ///
                        }
                    }
                }
            }
        }
        throw new NoPossibleChainException();
    }


    // data una lista di tabs e un tab, restituisce gli elementi della lista che condividono almeno una casa con esso (diversi dal tab)
    private static List<Tab> getSeenTabs(Tab tab, List<Tab> tabs) {
        return tabs
                .stream()
                .filter(t -> t != tab && (t.getBox() == tab.getBox() || t.getRow() == tab.getRow() || t.getCol() == tab.getCol()))
                .collect(Collectors.toList());
    }


    // restituisce le casa che accomuna due tabs
    private static House getSharedHouse(Tab tab, Link link) {
        return
                tab.getBox() == link.getTab().getBox() ? House.BOX :
                        tab.getRow() == link.getTab().getRow() ? House.ROW : House.COL;
    }


    // dato un tab di partenza, una casa che NON deve essere condivisa, un candidato e una lista di tabs,
    // restituisce il tab che nelle case non condivise presenta una sola altra casella per ospitare il candidato (essendo lui vero, l'altra sara' per forza falsa)
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

//		Sudoku sudoku = SudokuList.BLOCKED.get(0);
//        Sudoku sudoku = SudokuList.DISCOUNTINUOUS_NICE_LOOP;
        Sudoku sudoku = SudokuList.TEST_X_CHAIN_1;
//		Sudoku sudoku = SudokuList.TEST_X_CHAIN_2;

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        tabs = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs).getTabs();
        Utils.grid(sudoku);
        SkimmingResult result = check(tabs);

        Utils.printChangeLogs(result);
    }
}