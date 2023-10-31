package com.gianfro.games.explainers;

import com.gianfro.games.entities.Change;
import com.gianfro.games.entities.ChangeLog;

public class ExplainerNaked1 {

	public static void explain(ChangeLog changeLog) {
		for (Change change : changeLog.getChanges()) {
			System.out.println("NUMBER " + change.getNumber() + " IS THE ONLY CANDIDATE IN CELL " + SudokuExplainer.getCell(change));		
		}
	}
}
