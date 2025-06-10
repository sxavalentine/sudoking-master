package com.gianfro.games.controllers;

import com.gianfro.games.entities.SolutionOutput;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.Tab;
import com.gianfro.games.response.RandomSudokuResponse;
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
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(SudokuController.RESOURCE_NAME)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SudokuController {

    static final String RESOURCE_NAME = "SUDOKING";

    @Autowired
    SudokuService sudokuService;

    @PostMapping(value = "/getTabs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Tab> getTabs(@NotNull @RequestBody String stringNumbers) {
        Sudoku s = Utils.buildSudoku(stringNumbers);
        return Utils.getBasicTabs(s);
    }

    @PostMapping(value = "/solveSudoku", produces = MediaType.APPLICATION_JSON_VALUE)
    public SolutionOutput solveSudoku(@NotNull @RequestBody String stringNumbers) {
        return this.sudokuService.solveSudoku(stringNumbers);
    }

    @GetMapping(value = "/solveSudokuWithExplanation", produces = MediaType.APPLICATION_JSON_VALUE)
    public String solveSudokuWithExplanation(@NotNull @RequestBody String stringNumbers) {
        return this.sudokuService.solveSudokuWithExplanation(stringNumbers);
    }

    @GetMapping(value = "/solve50kSudoku", produces = MediaType.APPLICATION_JSON_VALUE)
    public void solve50kSudoku() {
        this.sudokuService.solve50kSudoku();
    }

    @GetMapping(value = "/getTabs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Tab> getSudokuTabs(@NotNull @RequestBody String stringNumbers) {
        return this.sudokuService.getSudokuTabs(stringNumbers);
    }

    @GetMapping(value = "/printGrid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String printGrid(@NotNull @RequestBody String stringNumbers) {
        Sudoku s = Utils.buildSudoku(stringNumbers);
        return Utils.grid(s);
    }

    @GetMapping(value = "/printMegaGrid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String printMegaGrid(@NotNull @RequestBody String stringNumbers) {
        Sudoku s = Utils.buildSudoku(stringNumbers);
        List<Tab> tabs = Utils.getBasicTabs(s);
        return Utils.megaGrid(s, tabs);
    }

    @GetMapping(value = "/findByStartingNumbers", produces = MediaType.APPLICATION_JSON_VALUE)
    public SolutionOutput findByStartingNumbers(@NotNull @RequestBody String startingNumbers) {
        return this.sudokuService.findSolutionByStartingNumbers(startingNumbers);
    }

    @GetMapping(value = "/solveUnsolvableSudoku", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SolutionOutput> solveUnsolvableSudokus() {
        return this.sudokuService.solveUnsolvableSudokus();
    }

    @GetMapping(value = "/getRandomSudoku", produces = MediaType.APPLICATION_JSON_VALUE)
    public RandomSudokuResponse getRandomSudoku() {
        String sudokuNumbers = this.sudokuService.getRandomSudoku();
        return new RandomSudokuResponse(sudokuNumbers);
    }
}
