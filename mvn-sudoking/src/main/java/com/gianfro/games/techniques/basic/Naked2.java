package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;

import java.util.LinkedHashSet;
import java.util.Set;

public class Naked2 {

    /**
     * If in a Sudoku House (BOX, ROW, COL) there is a group of exactly 2 cells containing only a certain pair of candidates (eg: [1,2])
     * then we can remove those candidates from all other cells of that House.
     */

    public static final String NAKED_PAIR = "NAKED PAIR";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        changeLogs.addAll(TechniqueUtils.checkNaked(NAKED_PAIR, sudoku, House.BOX));
        changeLogs.addAll(TechniqueUtils.checkNaked(NAKED_PAIR, sudoku, House.ROW));
        changeLogs.addAll(TechniqueUtils.checkNaked(NAKED_PAIR, sudoku, House.COL));
        return changeLogs;
    }

}
