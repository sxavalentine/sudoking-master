package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;

import java.util.LinkedHashSet;
import java.util.Set;

public class Naked4 {

    /**
     * If in a Sudoku House (BOX, ROW, COL) there is a group of exactly 4 cells containing only a certain quad of candidates (eg: [1,2,3,4])
     * then we can remove those candidates from all other cells of that House.
     * NOTE: the 4 cells forming the quad don't necessarily need to include ALL 4 candidates
     */

    public static final String NAKED_QUAD = "NAKED QUAD";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        changeLogs.addAll(TechniqueUtils.checkNaked(NAKED_QUAD, sudoku, House.BOX));
        changeLogs.addAll(TechniqueUtils.checkNaked(NAKED_QUAD, sudoku, House.ROW));
        changeLogs.addAll(TechniqueUtils.checkNaked(NAKED_QUAD, sudoku, House.COL));
        return changeLogs;
    }
    
}