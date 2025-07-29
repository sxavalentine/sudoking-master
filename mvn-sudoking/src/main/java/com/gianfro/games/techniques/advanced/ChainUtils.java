package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.House;
import com.gianfro.games.entities.StrongLink;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;
import com.gianfro.games.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChainUtils {

    /**
     * Given a SudokuCell, a Sudoku (of which the cell is part of), and a candidate, returns the list of SudokuCell that have a strong link
     * with the input cell
     */
    public static List<SudokuCell> findStrongLinksOnCandidate(SudokuCell cell, Sudoku sudoku, int candidate) {
        List<SudokuCell> strongLinks = new ArrayList<>();
        for (House house : House.values()) {
            List<SudokuCell> houseCells = Utils.getHouseCells(sudoku, house, cell.getHouseNumber(house));
            List<SudokuCell> welcomingCells = houseCells.stream().filter(c ->
                            c.getCandidates().contains(candidate) &&
                                    c != cell)
                    .toList();
            if (welcomingCells.size() == 1) {
                strongLinks.addAll(welcomingCells);
            }
        }
        return strongLinks;
    }

    /**
     * Given a SudokuCell, a List of SudokuCells (of which the cell is part of)
     * returns the list of SudokuCell that have a strong link with the input cell
     * NOTE: the welcomingCells in input must have been carefully selected before calling the method
     * (eg: they must all contain a certain candidate)
     */
    public static List<SudokuCell> findStrongLinks(SudokuCell cell, List<SudokuCell> welcomingCells) {
        List<SudokuCell> strongLinks = new ArrayList<>();
        for (House house : House.values()) {
            List<SudokuCell> houseCells = welcomingCells.stream().filter(
                    c -> c != cell && c.getHouseNumber(house) == cell.getHouseNumber(house)).toList();
            if (houseCells.size() == 1) {
                strongLinks.addAll(welcomingCells);
            }
        }
        return strongLinks;
    }

    /**
     * Given a list of SudokuCell taken from a Sudoku, returns the map of strong links among them
     * NOTE: the cells could be
     * - all the empty cells of the sudoku
     * - all the bivalue cells of the sudoku (for XY_Chain)
     */

    public static Map<Integer, List<StrongLink>> getStrongLinksMap(Sudoku sudoku) {
        Map<Integer, List<StrongLink>> strongLinksMap = new HashMap<>();
        List<SudokuCell> emptyCells = sudoku.getCells().stream().filter(c -> c.isEmpty()).toList();
        for (int candidate : Utils.NUMBERS) {
            List<SudokuCell> welcomingCells = emptyCells.stream().filter(c -> c.getCandidates().contains(candidate)).toList();
            for (House house : House.values()) {
                for (int houseNumber : Utils.NUMBERS) {
                    List<SudokuCell> welcomingHouseCells = welcomingCells.stream().filter(c -> c.getHouseNumber(house) == houseNumber).toList();
                    if (welcomingHouseCells.size() == 2) {
                        strongLinksMap.computeIfAbsent(candidate, k -> new ArrayList<>()).add(
                                new StrongLink(
                                        welcomingHouseCells.get(0),
                                        welcomingHouseCells.get(1),
                                        candidate,
                                        house,
                                        houseNumber));
                    }
                }
            }
        }
        return strongLinksMap;
    }

    public static Map<Integer, List<StrongLink>> getBiValueStrongLinksMap(Sudoku sudoku) {
        Map<Integer, List<StrongLink>> strongLinksMap = new HashMap<>();
        List<SudokuCell> emptyCells = sudoku.getCells().stream().filter(c -> c.getCandidates().size() == 2).toList();
        for (int candidate : Utils.NUMBERS) {
            List<SudokuCell> welcomingCells = emptyCells.stream().filter(c -> c.getCandidates().contains(candidate)).toList();
            for (House house : House.values()) {
                for (int houseNumber : Utils.NUMBERS) {
                    List<SudokuCell> welcomingHouseCells = welcomingCells.stream().filter(c -> c.getHouseNumber(house) == houseNumber).toList();
                    if (welcomingHouseCells.size() == 2) {
                        strongLinksMap.computeIfAbsent(candidate, k -> new ArrayList<>()).add(
                                new StrongLink(
                                        welcomingHouseCells.get(0),
                                        welcomingHouseCells.get(1),
                                        candidate,
                                        house,
                                        houseNumber));
                    }
                }
            }
        }
        return strongLinksMap;
    }
}
