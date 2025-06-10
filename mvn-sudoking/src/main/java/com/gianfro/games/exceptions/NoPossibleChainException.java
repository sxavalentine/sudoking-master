package com.gianfro.games.exceptions;

import com.gianfro.games.entities.Link;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class NoPossibleChainException extends RuntimeException {

    static final long serialVersionUID = 1L;
    final List<Link> chain;

}
