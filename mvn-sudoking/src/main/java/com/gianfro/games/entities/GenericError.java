package com.gianfro.games.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Document(collection = "generic_error")
public class GenericError {

    private String startingNumbers;
    private String exceptionMessage;
}
