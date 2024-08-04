package com.gianfro.games.techniques.advanced.utils;

import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Link;
import com.gianfro.games.entities.Tab;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChainUtils {

    /**
     * Given a Tab and a List<Link>, check if a Link of the chain is the corresponding Tab
     */
    public static boolean chainContainsTab(List<Link> chain, Tab tab) {
        for (Link link : chain) {
            if (link.getTab().equals(tab)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Given a Tab and a List<Tab>, returns the List<Tab> that share at least a House and a candidate with the Tab
     */
    public static List<Tab> getSeenCells(Tab tab, List<Tab> tabs) {
        List<Tab> seenTabs = new ArrayList<>();
        for (Tab t : tabs) {
            if (cellsSeeEachOther(tab, t) && getSharedCandidates(tab, t).size() > 0) {
                seenTabs.add(t);
            }
        }
        return seenTabs;
    }

    //TODO IMPORTANT: a occhio mi sembra scritto malissimo, logica fallata, non deve controllare se vede due link qualunque della catena, ma il primo e l'ultimo
    // controlla se un tab incrocia almeno due caselle che sono una true e l'altra false
    public static boolean seesTwoCellsWithDifferentValue(Tab tab, List<Link> chain) {
        Set<Boolean> valuesSeen = new HashSet<>();
        for (Link link : chain) {
            if (cellsSeeEachOther(tab, link.getTab())) {
                valuesSeen.add(link.isOn());
            }
        }
        return valuesSeen.size() == 2;
    }

    // dati due tabs, stabilisce se il primo differisce dal secondo, lo vede (ovvero ha in comune almeno una casa con esso) e se contiene il candidato passato come parametro
    // (per ora usato solo da Chain.findNegativeChain) rendere un metodo private della classe ?
    public static boolean seesCell(Tab tab, Tab toBeSeen, int candidate) {
        if (tab.getNumbers().contains(candidate) && tab != toBeSeen) {
            return
                    tab.getBox() == toBeSeen.getBox() ||
                            tab.getRow() == toBeSeen.getRow() ||
                            tab.getCol() == toBeSeen.getCol();
        }
        return false;
    }

    /**
     * Given two Tab, returns the House in common between them
     */
    public static boolean cellsSeeEachOther(Tab t1, Tab t2) {
        if (t1 != t2) {
            return t1.getBox() == t2.getBox() || t1.getRow() == t2.getRow() || t1.getCol() == t2.getCol();
        }
        return false;
    }

    /**
     * Given two Tab, returns the List of candidates they share
     */
    public static List<Integer> getSharedCandidates(Tab t1, Tab t2) {
        return t1.getNumbers().stream().filter(candidate -> t2.getNumbers().contains(candidate)).collect(Collectors.toList());
    }

    /**
     * Given two Tab, returns the House in common between them
     * TODO it should return null if they don't share any house (meaning they don't see each other). Instead it returns COL, it's misleading
     */
    public static House getSharedHouse(Tab t1, Tab t2) {
        return
                t1.getBox() == t2.getBox() ? House.BOX :
                        t1.getRow() == t2.getRow() ? House.ROW : House.COL;
    }
}
