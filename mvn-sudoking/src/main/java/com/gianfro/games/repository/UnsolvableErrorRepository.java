package com.gianfro.games.repository;

import com.gianfro.games.entities.errorsDTO.UnsolvableError;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnsolvableErrorRepository extends MongoRepository<UnsolvableError, String> {

    UnsolvableError findItemByStartingNumbers(String startingNumbers);
}
