package com.gianfro.games.exceptions;

import com.gianfro.games.entities.Link;
import com.gianfro.games.entities.Tab;
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

public class XChainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    List<Link> chain;
    List<Tab> tabs;
}
