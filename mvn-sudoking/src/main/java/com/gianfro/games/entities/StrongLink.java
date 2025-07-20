package com.gianfro.games.entities;

import com.gianfro.games.utils.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class StrongLink {

    SudokuCell linkA;
    SudokuCell linkB;
    int sharedCandidate;
    House sharedHouse;
    int houseNumber;

    /**
     * Given a Sudoku and two SudokuCells (from that Sudoku), check if there is a strong link between them and returns it,
     * otherwise returns null
     */
    public static StrongLink findStrongLink(Sudoku sudoku, SudokuCell a, SudokuCell b) {
        for (House house : House.values()) {
            int houseA = a.getHouseNumber(house);
            int houseB = b.getHouseNumber(house);
            if (houseA == houseB) {
                List<Integer> sharedCandidates = a.getCandidates().stream().filter(c -> b.getCandidates().contains(c)).toList();
                for (Integer candidate : sharedCandidates) {
                    List<SudokuCell> welcomingCells = Utils.getEmptyHouseCells(sudoku, house, houseA).stream().filter(
                            c -> c.getCandidates().contains(candidate)).toList();
                    if (welcomingCells.size() == 2) {
                        return StrongLink.builder()
                                .linkA(a)
                                .linkB(b)
                                .sharedCandidate(candidate)
                                .sharedHouse(house)
                                .houseNumber(houseA)
                                .build();
                    }
                }
            }
        }
        return null;
    }
}
