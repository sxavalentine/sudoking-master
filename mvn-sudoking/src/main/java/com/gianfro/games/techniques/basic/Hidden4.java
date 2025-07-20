package com.gianfro.games.techniques.basic;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.Sudoku;

import java.util.LinkedHashSet;
import java.util.Set;

public class Hidden4 {

    /**
     * If there are four cells within a house such as that four candidates appear nowhere outside those cells in that house,
     * those four candidates must be placed in the four cells. All other candidates can therefore be eliminated.
     */

    public static final String HIDDEN_QUAD = "HIDDEN QUAD";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>();
        changeLogs.addAll(TechniqueUtils.checkHidden(HIDDEN_QUAD, sudoku, House.BOX));
        changeLogs.addAll(TechniqueUtils.checkHidden(HIDDEN_QUAD, sudoku, House.ROW));
        changeLogs.addAll(TechniqueUtils.checkHidden(HIDDEN_QUAD, sudoku, House.COL));
        return changeLogs;
    }

}