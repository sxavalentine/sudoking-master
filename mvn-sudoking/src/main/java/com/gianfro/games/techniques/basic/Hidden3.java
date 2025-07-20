package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;

import java.util.LinkedHashSet;
import java.util.Set;

public class Hidden3 {

    /**
     * If there are three cells within a house such as that three candidates appear nowhere outside those cells in that house,
     * those three candidates must be placed in the three cells. All other candidates can therefore be eliminated.
     */

    public static final String HIDDEN_TRIPLE = "HIDDEN TRIPLE";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        changeLogs.addAll(TechniqueUtils.checkHidden(HIDDEN_TRIPLE, sudoku, House.BOX));
        changeLogs.addAll(TechniqueUtils.checkHidden(HIDDEN_TRIPLE, sudoku, House.ROW));
        changeLogs.addAll(TechniqueUtils.checkHidden(HIDDEN_TRIPLE, sudoku, House.COL));
        return changeLogs;
    }
}