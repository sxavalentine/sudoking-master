package com.gianfro.games.entities.deductions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gianfro.games.entities.House;
import com.gianfro.games.entities.SudokuCell;

//TODO: QUESTO NON SONO SICURO DI VOLERLO USARE
// Indica a Jackson di includere un campo "type" (o quello che preferisci) nel JSON per identificare il sottotipo.
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Usa il nome della classe o un nome logico
        property = "_class" // Nome del campo nel JSON che conterrà il tipo (convenzione di Spring Data/Mongo)
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = CellSolved.class, name = "CellSolved"),
        @JsonSubTypes.Type(value = CellSkimmed.class, name = "CellSkimmed"),
        @JsonSubTypes.Type(value = CellGuessed.class, name = "CellGuessed")
})

public interface CellChange {

    String getSolvingTechnique();

    House getHouse();

    SudokuCell getCell();

    int getNumber();
}
