package com.gianfro.games.controllers;

import com.gianfro.games.entities.SolutionOutput;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.Tab;
import com.gianfro.games.service.SudokuService;
import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(SudokuController.RESOURCE_NAME)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SudokuController {

    static final String RESOURCE_NAME = "SUDOKING";

    @Autowired
    SudokuService sudokuService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SolutionOutput solveSudokuPost(@NotNull @RequestBody @NotNull String stringNumbers) {
        return this.sudokuService.solveSudoku(stringNumbers);
    }

    @GetMapping(value = "/solveSudoku", produces = MediaType.APPLICATION_JSON_VALUE)
    public SolutionOutput solveSudoku(@NotNull @RequestBody String stringNumbers) {
        return this.sudokuService.solveSudoku(stringNumbers);
    }

    @GetMapping(value = "/getTabs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Tab> getSudokuTabs(@NotNull @RequestBody String stringNumbers) {
        return this.sudokuService.getSudokuTabs(stringNumbers);
    }

    @GetMapping(value = "/solve50kSudoku", produces = MediaType.APPLICATION_JSON_VALUE)
    public void solve50kSudoku() {
        this.sudokuService.solve50kSudoku();
    }

    @GetMapping(value = "/printGrid", produces = MediaType.APPLICATION_JSON_VALUE)
    public void printGrid(@NotNull @RequestBody String stringNumbers) {
        Sudoku s = Utils.buildSudoku(stringNumbers);
        Utils.grid(s);
    }
}
