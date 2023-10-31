package com.gianfro.games.repository;

import com.gianfro.games.entities.SolutionOutput;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SudokuSolutionsRepository extends MongoRepository<SolutionOutput, String> {

    SolutionOutput findItemByStartingNumbers(String startingNumbers);

//    @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
//    List<SolutionOutput> findAll(String category);

    public long count();

}
