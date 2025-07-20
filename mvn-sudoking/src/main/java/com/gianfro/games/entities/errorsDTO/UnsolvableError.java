package com.gianfro.games.entities.errorsDTO;

import com.gianfro.games.entities.SolutionStep;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Document(collection = "unsolvable_error")
public class UnsolvableError {

    private String startingNumbers;
    private String impasseNumbers;
    private int startingDigits;
    private int impasseDigits;
    private String exceptionMessage;
    private List<SolutionStep> solutionSteps;
}
