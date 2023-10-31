package com.gianfro.games.solving.techniques.advanced.utils;

import com.gianfro.games.entities.Link;
import com.gianfro.games.entities.Tab;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChainUtils {

    // dato un tab e una lista di Link 'chain', stabilisce se un anello della catena corrisponde al tab
    public static boolean chainContainsTab(List<Link> chain, Tab tab) {
        for (Link link : chain) {
            if (link.getTab().equals(tab)) {
                return true;
            }
        }
        return false;
    }


    // dato un tab e una lista di tab 'tabs', restituisce la lista dei tab con cui condivide almeno una casa e un candidato
    public static List<Tab> getSeenCells(Tab tab, List<Tab> tabs) {
        List<Tab> seenTabs = new ArrayList<>();
        for (Tab t : tabs) {
            if (cellsSeeEachOther(tab, t) && getSharedCandidates(tab, t).size() > 0) {
                seenTabs.add(t);
            }
        }
        return seenTabs;
    }


    //TODO a occhio mi sembra scritto malissimo, logica fallata, non deve controllare se vede due link qualunque della catena, ma il primo e l'ultimo

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
    // per ora usato solo da Chain.findNegativeChain) rendere un metodo private della classe ?
    public static boolean seesCell(Tab tab, Tab toBeSeen, int candidate) {
        if (tab.getNumbers().contains(candidate) && tab != toBeSeen) {
            return
                    tab.getBox() == toBeSeen.getBox() ||
                            tab.getRow() == toBeSeen.getRow() ||
                            tab.getCol() == toBeSeen.getCol();
        }
        return false;
    }


    // dati due tabs, stabilisce se hanno almeno una casa in comune
    public static boolean cellsSeeEachOther(Tab t1, Tab t2) {
        if (t1 != t2) {
            return t1.getBox() == t2.getBox() || t1.getRow() == t2.getRow() || t1.getCol() == t2.getCol();
        }
        return false;
    }


    // dati due tabs, stabilisce i candidati che hanno in comune
    public static List<Integer> getSharedCandidates(Tab t1, Tab t2) {
        return t1.getNumbers().stream().filter(candidate -> t2.getNumbers().contains(candidate)).collect(Collectors.toList());
    }
}
