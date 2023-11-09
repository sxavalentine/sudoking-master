package com.gianfro.games.repository;

import com.gianfro.games.entities.No5050Entity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface No5050Repository extends MongoRepository<No5050Entity, String> {

    public long count();
}
