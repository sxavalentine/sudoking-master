package com.gianfro.games.explainers;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.techniques.basic.Hidden1;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.Set;

public class ExplainerHidden1 {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        changeLog.getChanges().forEach(c -> {
            sb.append(String.format(
                    "CELL %s IN %s IS THE ONLY CELL WITH THE CANDIDATE %s",
                    c.getCell().getCoordinates(),
                    Utils.getWelcomingUnit(changeLog),
                    c.getNumber()));
            sb.append("\n");
        });
        System.out.println(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST HIDDEN SINGLE -----------------------------------------");

        Sudoku sudoku;
        sudoku = Sudoku.fromString(SudokuList.TEST_HIDDEN_1_BOX);

        Utils.megaGrid(sudoku);

        Set<ChangeLog> changeLogs = Hidden1.check(sudoku);
        changeLogs.forEach(changeLog -> explain(changeLog));
    }
}
