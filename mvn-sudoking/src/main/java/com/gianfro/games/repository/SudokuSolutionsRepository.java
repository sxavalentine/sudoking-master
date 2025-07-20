package com.gianfro.games.repository;

import com.gianfro.games.entities.SolutionOutput;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SudokuSolutionsRepository extends MongoRepository<SolutionOutput, String> {

    SolutionOutput findByStartingNumbers(String startingNumbers);

    @Aggregation(pipeline = "{ '$sample': { 'size': 1 } }")
    Optional<SolutionOutput> findRandomSudoku();
}
