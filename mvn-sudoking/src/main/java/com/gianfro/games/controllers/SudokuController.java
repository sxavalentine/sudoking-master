package com.gianfro.games.controllers;

import com.gianfro.games.entities.*;
import com.gianfro.games.exceptions.NoCandidatesLeftException;
import com.gianfro.games.explainers.SudokuExplainer;
import com.gianfro.games.response.RandomSudokuResponse;
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
    public ResponseEntity<List<Tab>> getTabs(@NotNull @RequestBody String stringNumbers) {
        Sudoku s = Utils.buildSudoku(stringNumbers);
        List<Tab> tabs = Utils.getBasicTabs(s);
        List<Tab> emptyTabs = Utils.checkForNoCandidates(tabs);
        if (!emptyTabs.isEmpty()) {
            throw new NoCandidatesLeftException(s.getStringNumbers(), tabs, emptyTabs);
        }
        return ResponseEntity.ok(tabs);
    }

    //TODO: RIVEDERE PARECCHIO, TROPPO SCRITTO MALE
    @PostMapping(value = "/getLittleHelp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChangeLog> getLittleHelp(@NotNull @RequestBody String stringNumbers) {
        ChangeLog changeLog = null;
        try {
            SolutionOutput solutionOutput = sudokuService.solveSudoku(stringNumbers);
            if (!solutionOutput.getSolutionSteps().isEmpty()) {
                SolutionStep step1 = solutionOutput.getSolutionSteps().get(0);
                //TODO CHECK
                changeLog = step1.getChangeLogs().get(0);
                changeLog.setExplanation(SudokuExplainer.explainChange(changeLog));
            }
        } catch (Exception e) {
            //TODO
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        return ResponseEntity.ok(changeLog);
    }

    //TODO REMOVE
//    NoCandidatesLeftErrorDTO errorDTO = NoCandidatesLeftErrorDTO.builder()
//            .sudokuNumbers(s.getStringNumbers())
//            .cellCoordinates("A1") //TODO per il momento ignora il fatto che sia hardcoded
//            .numberSet(1) //TODO per il momento ignora il fatto che sia hardcoded
//            .tabs(tabs)
//            .emptyTabs(emptyTabs)
//            .build();

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
