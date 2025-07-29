package com.gianfro.games.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gianfro.games.exceptions.SudokuBuildException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Sudoku {

    private static final Pattern SUDOKU_STRING_PATTERN = Pattern.compile("^[0-9]{81}$");

    @Transient
    List<SudokuCell> cells;

    private Sudoku(List<SudokuCell> cells) {
        this.cells = deepCopyCells(cells);
        updateBasicCandidates();
    }

    public static Sudoku fromCells(List<SudokuCell> cells) {
        return new Sudoku(cells);
    }

    /**
     * Given a string, check if it's a valid stringNumbers to build a Sudoku.
     * If it is, return a Sudoku object, if not throws a SudokuBuildException
     */
    public static Sudoku fromString(String stringNumbers) {
        if (!sudokuStringMatchesPattern(stringNumbers)) {
            throw new SudokuBuildException(stringNumbers);
        }
        List<SudokuCell> cells = new ArrayList<>();
        int cellIndex = 0;
        String[] values = stringNumbers.split("");
        for (String s : values) {
            SudokuCell cell = SudokuCell.fromNumbers(Integer.parseInt(s), (cellIndex / 9) + 1, (cellIndex % 9) + 1, null);
            cells.add(cell);
            cellIndex++;
        }
        return new Sudoku(cells);
    }

    /**
     * Given a string, check if it's a valid stringNumbers to build a Sudoku.
     * If it is, return a Sudoku object, if not throws a SudokuBuildException
     *
     * @param stringNumbers    List of numbers representing the 81 cells value of the Sudoku
     * @param stringCandidates List of strings, representing the candidates of each cell
     */
    public static Sudoku fromStringAndStringCandidates(String stringNumbers, List<String> stringCandidates) {
        if (!sudokuStringMatchesPattern(stringNumbers)) {
            throw new SudokuBuildException(stringNumbers);
        }
        List<SudokuCell> cells = new ArrayList<>();
        int cellIndex = 0;
        String[] values = stringNumbers.split("");
        for (int i = 0; i < values.length; i++) {
            List<Integer> candidates = stringCandidates.get(i).isEmpty() ?
                    new ArrayList<>() : stringCandidates.get(i).chars().map(Character::getNumericValue).boxed().toList();
            SudokuCell cell = SudokuCell.fromNumbers(Integer.parseInt(values[i]), (cellIndex / 9) + 1, (cellIndex % 9) + 1, candidates);
            cells.add(cell);
            cellIndex++;
        }
        return new Sudoku(cells);
    }

    /**
     * Given a string, check if it's a valid string to build a sudoku (used by buildSudoku(String stringNumbers))
     * Checks if the string has exactly 81 characters and consists only of digits ('0'-'9').
     */
    private static boolean sudokuStringMatchesPattern(String sudokuString) {
        if (sudokuString == null || sudokuString.isEmpty()) {
            return false;
        }
        return SUDOKU_STRING_PATTERN.matcher(sudokuString).matches();
    }

    private List<SudokuCell> deepCopyCells(List<SudokuCell> oldCells) {
        List<SudokuCell> newCells = new ArrayList<>();
        for (SudokuCell oldCell : oldCells) {
            newCells.add(SudokuCell.deepCopy(oldCell));
        }
        return newCells;
    }

    public void updateBasicCandidates() {
        // Pre-calcola tutti i valori numerici per righe, colonne e box.
        // Usiamo Set per un controllo di appartenenza efficiente (O(1) in media).
        List<List<Integer>> boxesValues = new ArrayList<>();
        List<List<Integer>> rowsValues = new ArrayList<>();
        List<List<Integer>> colsValues = new ArrayList<>();

        // Inizializza le liste di Set vuoti
        for (int i = 0; i < 9; i++) {
            boxesValues.add(new ArrayList<>());
            rowsValues.add(new ArrayList<>());
            colsValues.add(new ArrayList<>());
        }

        // Popola i Set con i valori già presenti nelle celle
        for (SudokuCell cell : this.cells) {
            if (cell.getValue() != 0) {
                rowsValues.get(cell.getRow() - 1).add(cell.getValue());
                colsValues.get(cell.getCol() - 1).add(cell.getValue());
                boxesValues.get(cell.getBox() - 1).add(cell.getValue());
            }
        }

        // Ora, per ogni cella vuota, rimuoviamo i candidati usando i valori pre-calcolati.
        for (SudokuCell cell : this.cells) {
            if (cell.getValue() == 0) {
                cell.getCandidates().removeAll(boxesValues.get(cell.getBox() - 1));
                cell.getCandidates().removeAll(rowsValues.get(cell.getRow() - 1));
                cell.getCandidates().removeAll(colsValues.get(cell.getCol() - 1));
            }
        }
    }

    /**
     * return the string of numbers forming the sudoku
     */
    @Field("numbers")
    @JsonIgnore
    public String getStringNumbers() {
        StringBuilder s = new StringBuilder();
        for (SudokuCell c : cells) {
            s.append(c.getValue());
        }
        return s.toString();
    }

    /**
     * return the List of string of candidates in each forming the sudoku
     */
    @Field("candidates")
    @JsonIgnore
    public List<String> getCandidatesStringList() {
        List<String> candidates = new ArrayList<>();
        for (SudokuCell c : cells) {
            candidates.add(
                    c.getCandidates().stream().map(String::valueOf).collect(Collectors.joining()));
        }
        return candidates;
    }

    @Override
    public String toString() {
        return getStringNumbers();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sudoku sudoku = (Sudoku) obj;
        return this.getStringNumbers().equals(sudoku.getStringNumbers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getStringNumbers());
    }
}