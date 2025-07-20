package com.gianfro.games.explainers;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.techniques.basic.Naked1;
import com.gianfro.games.utils.Utils;

import java.util.Set;

public class ExplainerNaked1 {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        changeLog.getChanges().forEach(c -> {
            sb.append(String.format(
                    "IN CELL %s NUMBER %s IS THE ONLY CANDIDATE LEFT",
                    c.getCell().getCoordinates(),
                    c.getNumber()));
            sb.append("\n");
        });
        System.out.println(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------- TEST NAKED SINGLE  -----------------------------------------");

        Sudoku sudoku;
        sudoku = Sudoku.fromString("074120000206400070198700000951840000300971008000065291000007854020004607000083910");

        Utils.megaGrid(sudoku);

        Set<ChangeLog> changeLogs = Naked1.check(sudoku);
        changeLogs.forEach(changeLog -> explain(changeLog));
    }
}
