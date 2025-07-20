package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;

import java.util.LinkedHashSet;
import java.util.Set;

public class Naked3 {

    /**
     * If in a Sudoku House (BOX, ROW, COL) there is a group of exactly 3 cells containing only a certain triple of candidates (eg: [1,2,3])
     * then we can remove those candidates from all other cells of that House.
     * NOTE: the 3 cells forming the triple don't necessarily need to include ALL 3 candidates
     */

    public static final String NAKED_TRIPLE = "NAKED TRIPLE";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        changeLogs.addAll(TechniqueUtils.checkNaked(NAKED_TRIPLE, sudoku, House.BOX));
        changeLogs.addAll(TechniqueUtils.checkNaked(NAKED_TRIPLE, sudoku, House.ROW));
        changeLogs.addAll(TechniqueUtils.checkNaked(NAKED_TRIPLE, sudoku, House.COL));
        return changeLogs;
    }

}