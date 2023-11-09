package com.gianfro.games.solving.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.solving.techniques.advanced.utils.ChainUtils;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Coloring {

    public static final String COLORING = "COLORING";
    public static final String COLOR_WRAP = "COLOR WRAP";
    public static final String COLOR_TRAP = "COLOR TRAP";

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


    private static SkimmingResult chain(House house, List<Tab> tabs) {
        List<ChangeLog> changeLogs = new LinkedList<>();
        try {
            for (int number : Utils.NUMBERS) {
                List<ChangeLogUnitMember> unitMembers = new ArrayList<>();
                List<Link> fullColorMap = null;
                for (int houseNumber : Utils.NUMBERS) {
                    if (fullColorMap == null) {
                        // gets the house tabs that contain the number as candidate
                        List<Tab> welcomingTabs = Utils.getHouseTabs(house, houseNumber, tabs).stream().filter(t -> t.getNumbers().contains(number)).collect(Collectors.toList());
                        if (welcomingTabs.size() == 2) {

                            List<Link> colorMap = new ArrayList<>();

                            Tab tab1 = welcomingTabs.get(0);
                            Tab tab2 = welcomingTabs.get(1);

                            Link link1 = new Link(tab1, false, number);
                            Link link2 = new Link(tab2, true, number);

                            colorMap.add(link1);
                            colorMap.add(link2);

                            // after finding the first two opposite colors of the map, we call the getColorMap method to find the remaining ones
                            colorMap = getColorMap(colorMap, Arrays.asList(link1, link2), tabs, number);

                            // if I obtained a colorMap with more than two links
                            if (colorMap.size() > 2) {
                                fullColorMap = colorMap;
                                unitMembers.addAll(colorMap);
                                List<Change> skimmings = new ArrayList<>();

                                List<Tab> colorA = colorMap.stream().filter(x -> x.isOn()).map(x -> x.getTab()).collect(Collectors.toList());
                                List<Tab> colorB = colorMap.stream().filter(x -> !x.isOn()).map(x -> x.getTab()).collect(Collectors.toList());

                                // check among the tabs that are not part of the colorMap
                                List<Tab> tabsNotInColorMap = tabs.stream().filter(t -> t.getNumbers().contains(number) && !colorA.contains(t) && !colorB.contains(t)).collect(Collectors.toList());
                                for (Tab tab : tabsNotInColorMap) {
                                    List<Tab> seenColorA = ChainUtils.getSeenCells(tab, colorA);
                                    List<Tab> seenColorB = ChainUtils.getSeenCells(tab, colorB);

                                    // if a cell can see two or more cells with different colors, then we can remove the number from its candidate
                                    if (!seenColorA.isEmpty() && !seenColorB.isEmpty()) {
                                        tab.getNumbers().remove(Integer.valueOf(number));

                                        Skimming skimming = new Skimming(COLOR_TRAP, house, tab, Collections.singletonList(number));
                                        skimmings.add(skimming);
                                    }
                                }

                                if (!skimmings.isEmpty()) {
                                    ChangeLog changeLog = new ChangeLog(
                                            Collections.singletonList(number),
                                            house,
                                            houseNumber,
                                            unitMembers,
                                            COLORING,
                                            COLOR_TRAP,
                                            skimmings);
                                    changeLogs.add(changeLog);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in COLORING " + house + ": " + e.getMessage());
        }
        return new SkimmingResult(tabs, changeLogs);
    }

    private static List<Link> getColorMap(List<Link> colorMap, List<Link> lastLinksAdded, List<Tab> tabs, int number) {

        List<Tab> colorMapTabs = colorMap.stream().map(x -> x.getTab()).collect(Collectors.toList());
        List<Link> newLinksAdded = new LinkedList<>();

        for (Link link : lastLinksAdded) {
            // returns the list of the other tabs seen by the link that contains the number as candidate and are not already part of colorMap
            List<Tab> seenTabs = ChainUtils.getSeenCells(link.getTab(), tabs).stream().filter(t -> !colorMapTabs.contains(t) && t.getNumbers().contains(number)).collect(Collectors.toList());
            for (Tab t : seenTabs) {
                // we look for which house they share
                House sharedHouse = getSharedHouse(t, link);
                // we then look for all tabs in the shared house that contain the candidate number
                List<Tab> houseTabs = tabs.stream()
                        .filter(ht -> ht.getNumbers().contains(number) &&
                                (sharedHouse.equals(House.BOX) ? ht.getBox() == link.getBox() : sharedHouse.equals(House.ROW) ? ht.getRow() == link.getRow() : ht.getCol() == link.getCol()))
                        .collect(Collectors.toList());
                // if there are only 2 tabs (the link and the seen tab)
                if (houseTabs.size() == 2) {
                    Link newLink = new Link(t, !link.isOn(), number);
                    newLinksAdded.add(newLink);
                }
            }
        }

        if (!newLinksAdded.isEmpty()) {
            colorMap.addAll(newLinksAdded);
            colorMap = getColorMap(colorMap, newLinksAdded, tabs, number);
        }

        return colorMap;
    }

    /**
     * Returns the House in common between two Tab
     */
    private static House getSharedHouse(Tab tab, Link link) {
        return
                tab.getBox() == link.getTab().getBox() ? House.BOX :
                        tab.getRow() == link.getTab().getRow() ? House.ROW : House.COL;
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST COLORING -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Utils.buildSudoku(SudokuList.COLOR_WRAP);
        sudoku = Utils.buildSudoku(SudokuList.COLOR_TRAP);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        tabs = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs).getTabs();

        tabs = RemotePairs.check(tabs).getTabs();
        tabs = DiscontinuousNiceLoop.check(tabs).getTabs();
        tabs = XWing.check(tabs).getTabs();
        tabs = XChain.check(tabs).getTabs();

        Utils.megaGrid(sudoku, tabs);
        SkimmingResult result = check(tabs);
        Utils.printChangeLogs(result);

        Utils.megaGrid(sudoku, result.getTabs());
    }
}
