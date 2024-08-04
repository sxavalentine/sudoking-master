package com.gianfro.games.repository;

import com.gianfro.games.entities.SolutionOutput;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SudokuSolutionsRepository extends MongoRepository<SolutionOutput, String> {

    SolutionOutput findByStartingNumbers(String startingNumbers);
}
