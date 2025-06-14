package com.gianfro.games.handlers;

import com.gianfro.games.exceptions.NoCandidatesLeftException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoCandidatesLeftException.class)
    public ResponseEntity<Map<String, Object>> handleNoCandidatesLeftException(NoCandidatesLeftException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("exceptionType", "NoCandidatesLeftException");

        body.put("message", ex.getMessage());
        body.put("sudokuNumbers", ex.getSudokuNumbers());
        body.put("tabs", ex.getTabs());
        body.put("emptyTabs", ex.getEmptyTabs());

        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}