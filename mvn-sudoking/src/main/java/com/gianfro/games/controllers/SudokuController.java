package com.gianfro.games.controllers;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.request.SudokuDTO;
import com.gianfro.games.entities.response.RandomSudokuResponse;
import com.gianfro.games.service.SudokuService;
import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;
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

    @PostMapping(value = "/getSkimmedCells", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SudokuCell>> getTabs(@NotNull @RequestBody String stringNumbers) {
        Sudoku s = Sudoku.fromString(stringNumbers);
//        Set<String> emptyCells = Utils.checkForEmptyCellsWithNoCandidates(s);
//        if (!emptyCells.isEmpty()) {
//            throw new NoCandidatesLeftException(s, emptyCells);
//        }
        return ResponseEntity.ok(s.getCells());
    }

    @PostMapping(value = "/getLittleHelp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ChangeLog>> getLittleHelp(@NotNull @RequestBody SudokuDTO sudokuDto) {
        SolutionOutput solutionOutput = sudokuService.solveSudoku(sudokuDto.toEntity());
        if (!solutionOutput.getSolutionSteps().isEmpty()) {
            SolutionStep step1 = solutionOutput.getSolutionSteps().get(0);
            return ResponseEntity.ok(step1.getChangeLogs());
        }
        throw new RuntimeException("EMPTY SOLUTION STEPS");
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

    @GetMapping(value = "/printGrid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String printGrid(@NotNull @RequestBody String stringNumbers) {
        Sudoku s = Sudoku.fromString(stringNumbers);
        return Utils.grid(s);
    }

    @GetMapping(value = "/printMegaGrid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String printMegaGrid(@NotNull @RequestBody String stringNumbers) {
        Sudoku s = Sudoku.fromString(stringNumbers);
        return Utils.megaGrid(s);
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
