package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.sudoku.solver.SudokuSolver;
import com.gianfro.games.techniques.advanced.utils.ChainUtils;
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
                                List<Change> colorWrapSkimmings = new ArrayList<>();
                                List<Change> colorTrapSkimmings = new ArrayList<>();

                                List<Tab> colorA = colorMap.stream().filter(x -> x.isOn()).map(x -> x.getTab()).collect(Collectors.toList());
                                List<Tab> colorB = colorMap.stream().filter(x -> !x.isOn()).map(x -> x.getTab()).collect(Collectors.toList());

                                //COLOR WRAP
                                boolean colorWrapA = false;
                                for (Tab t : colorA) {
                                    if (!colorWrapA) {
                                        List<Tab> seenCells = ChainUtils.getSeenCells(t, tabs);
                                        // if two or more cells with same color see each other, they can't both be true, so they both have to be false, so we can remove the number from the candidates of all cells of such color
                                        if (seenCells.stream().anyMatch(other -> colorA.contains(other))) {
                                            //TODO: find a way to highlight in the Changelog the two cells with same color that can see each other
                                            colorWrapA = true;

                                        }
                                    }
                                }
                                if (colorWrapA) {
                                    for (Tab t : colorA) {
                                        t.getNumbers().remove(Integer.valueOf(number));
                                        Skimming skimming = new Skimming(COLOR_WRAP, house, t, Collections.singletonList(number));
                                        colorWrapSkimmings.add(skimming);
                                    }
                                } else {
                                    boolean colorWrapB = false;
                                    for (Tab t : colorB) {
                                        if (!colorWrapB) {
                                            List<Tab> seenCells = ChainUtils.getSeenCells(t, tabs);
                                            // if two or more cells with same color see each other, they can't both be true, so they both have to be false, so we can remove the number from the candidates of all cells of such color
                                            if (seenCells.stream().anyMatch(other -> colorB.contains(other))) {
                                                //TODO: find a way to highlight in the Changelog the two cells with same color that can see each other
                                                colorWrapB = true;
                                            }
                                        }
                                    }
                                    if (colorWrapB) {
                                        for (Tab t : colorB) {
                                            t.getNumbers().remove(Integer.valueOf(number));
                                            Skimming skimming = new Skimming(COLOR_WRAP, house, t, Collections.singletonList(number));
                                            colorWrapSkimmings.add(skimming);
                                        }
                                    }
                                }


                                //COLOR TRAP
                                // check among the tabs that are not part of the colorMap
                                List<Tab> tabsNotInColorMap = tabs.stream().filter(t -> t.getNumbers().contains(number) && !colorA.contains(t) && !colorB.contains(t)).collect(Collectors.toList());
                                for (Tab tab : tabsNotInColorMap) {
                                    List<Tab> seenColorA = ChainUtils.getSeenCells(tab, colorA);
                                    List<Tab> seenColorB = ChainUtils.getSeenCells(tab, colorB);

                                    // if a cell can see two or more cells with different colors, then we can remove the number from its candidate
                                    if (!seenColorA.isEmpty() && !seenColorB.isEmpty()) {
                                        tab.getNumbers().remove(Integer.valueOf(number));

                                        Skimming skimming = new Skimming(COLOR_TRAP, house, tab, Collections.singletonList(number));
                                        colorTrapSkimmings.add(skimming);
                                    }
                                }

                                if (!colorWrapSkimmings.isEmpty()) {
                                    ChangeLog changeLog = new ChangeLog(
                                            Collections.singletonList(number),
                                            house,
                                            houseNumber,
                                            unitMembers,
                                            COLORING,
                                            COLOR_WRAP,
                                            colorWrapSkimmings);
                                    changeLogs.add(changeLog);
                                }

                                if (!colorTrapSkimmings.isEmpty()) {
                                    ChangeLog changeLog = new ChangeLog(
                                            Collections.singletonList(number),
                                            house,
                                            houseNumber,
                                            unitMembers,
                                            COLORING,
                                            COLOR_TRAP,
                                            colorTrapSkimmings);
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
                House sharedHouse = ChainUtils.getSharedHouse(t, link.getTab());
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
            for (Link link : newLinksAdded) {
                if (!colorMap.contains(link)) {
                    colorMap.add(link);
                }
            }
            colorMap = getColorMap(colorMap, newLinksAdded, tabs, number);
        }

        return colorMap;
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST COLORING -----------------------------------------");

        Sudoku sudoku;
//        sudoku = Utils.buildSudoku(SudokuList.COLOR_WRAP);
        sudoku = Utils.buildSudoku(SudokuList.COLOR_TRAP);

        List<Tab> tabs = Utils.getBasicTabs(sudoku);
        tabs = SudokuSolver.useStandardSolvingTechniques(sudoku, tabs).getTabs();

//        tabs = RemotePairs.check(tabs).getTabs();
//        tabs = DiscontinuousNiceLoop.check(tabs).getTabs();
//        tabs = XWing.check(tabs).getTabs();
//        tabs = XChain.check(tabs).getTabs();

        // WARNING: comment these 4 lines when testing with Sudoku COLOR WRAP
        // forced removal of candidates in cells B6 D9 H5 I6, so to have the exact same candidates of the online example of ColorWrap
        tabs.stream().filter(t -> t.getRow() == 2 && t.getCol() == 6).findFirst().orElse(null).getNumbers().remove(Integer.valueOf(8));
        tabs.stream().filter(t -> t.getRow() == 4 && t.getCol() == 9).findFirst().orElse(null).getNumbers().remove(Integer.valueOf(8));
        tabs.stream().filter(t -> t.getRow() == 8 && t.getCol() == 5).findFirst().orElse(null).getNumbers().remove(Integer.valueOf(2));
        tabs.stream().filter(t -> t.getRow() == 9 && t.getCol() == 6).findFirst().orElse(null).getNumbers().remove(Integer.valueOf(8));

        Utils.megaGrid(sudoku, tabs);

        SkimmingResult result = check(tabs);
        Utils.printChangeLogs(result);
    }
}
