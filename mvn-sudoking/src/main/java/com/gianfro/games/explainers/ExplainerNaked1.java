package com.gianfro.games.explainers;

import com.gianfro.games.entities.ChangeLog;

public class ExplainerNaked1 {

    public static String explain(ChangeLog changeLog) {
        StringBuilder sb = new StringBuilder();
        changeLog.getChanges().forEach(c -> {
            sb.append(String.format(
                    "IN CELL %s NUMBER %s IS THE ONLY CANDIDATE LEFT",
                    SudokuExplainer.getCell(c),
                    c.getNumber()));
            sb.append("\n");
        });
        System.out.println(sb);
        return sb.toString();
    }
}
