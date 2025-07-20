package com.gianfro.games.exceptions;

import com.gianfro.games.entities.Link;
import com.gianfro.games.entities.SudokuCell;
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

/**
 * An XChainException doesn't indicate an error in the code, but gets thrown whenever (while building an XChain)
 * we find one or more cells that can see both ends of the chain and contain the selected candidate.
 * In that case, we can stop building the chain, and remove the selected candidate from those cells.
 */
public class XChainException extends RuntimeException {

    static final long serialVersionUID = 1L;

    List<Link> chain;
    List<SudokuCell> cells;
}
