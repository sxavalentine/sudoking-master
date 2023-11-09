package com.gianfro.games.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Sudoku {

    @JsonIgnore
    @Transient
    List<Integer> numbers;
    @JsonIgnore
    @Transient
    List<List<Integer>> rows;
    @JsonIgnore
    @Transient
    List<List<Integer>> columns;
    @JsonIgnore
    @Transient
    List<List<Integer>> boxes;
    @JsonIgnore
    @Transient
    List<List<List<Integer>>> rowTrios;
    @JsonIgnore
    @Transient
    List<List<List<Integer>>> colTrios;

    public Sudoku(List<Integer> inputNumbers) {
        this.numbers = inputNumbers;
        this.rows = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.rowTrios = new ArrayList<>();
        this.colTrios = new ArrayList<>();
        this.boxes = new ArrayList<>();

        for (int i = 0; i < 81; i += 9) {
            rows.add(inputNumbers.subList(i, i + 9));
        }

        for (int x = 0; x < 9; x++) {
            columns.add(new ArrayList<>());
            for (int y = 0; y < 9; y++) {
                columns.get(x).add(rows.get(y).get(x));
            }
        }

        for (int i = 0; i < 9; i += 3) {
            rowTrios.add(rows.subList(i, i + 3));
        }
        for (int i = 0; i < 9; i += 3) {
            colTrios.add(columns.subList(i, i + 3));
        }
        for (int i = 0; i < 9; i++) {
            boxes.add(new ArrayList<>());
            for (int x : Utils.INDEXES_02) {
                for (int y : Utils.INDEXES_02) {
                    boxes.get(i).add(rowTrios.get(i / 3).get(x).subList(3 * (i % 3), 3 + (3 * (i % 3))).get(y));
                }
            }
        }
    }

    @Override
    public String toString() {
        return getStringNumbers();
    }

    // return the string of numbers forming the sudoku
    @Field("numbers")
    public String getStringNumbers() {
        StringBuilder s = new StringBuilder();
        for (Integer i : numbers) {
            s.append(i);
        }
        return s.toString();
    }
}