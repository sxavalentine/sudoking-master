package com.gianfro.games.repository;

import com.gianfro.games.entities.SudokuError;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ErrorsRepository extends MongoRepository<SudokuError, String> {

    SudokuError findItemByStartingNumbers(String startingNumbers);

    public long count();

}
