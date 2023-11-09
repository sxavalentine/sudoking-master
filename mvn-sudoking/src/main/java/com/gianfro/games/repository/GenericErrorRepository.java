package com.gianfro.games.repository;

import com.gianfro.games.entities.GenericError;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenericErrorRepository extends MongoRepository<GenericError, String> {

    GenericError findItemByStartingNumbers(String startingNumbers);

    public long count();

}
