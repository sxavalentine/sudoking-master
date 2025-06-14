package com.gianfro.games.entities.errorsDTO;

import com.gianfro.games.entities.Tab;
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
public class NoCandidatesLeftErrorDTO {

    String sudokuNumbers;
    Integer numberSet;
    String cellCoordinates;
    List<Tab> tabs;
    List<Tab> emptyTabs;
}
