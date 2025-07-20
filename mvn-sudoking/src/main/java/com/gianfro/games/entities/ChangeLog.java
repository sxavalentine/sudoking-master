package com.gianfro.games.entities;

import com.gianfro.games.entities.deductions.CellChange;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import javax.annotation.Nullable;
import java.util.List;

@Data
//TODO: Usa solo i campi specificati. NON scrivere equals/hashCode manuali! FONDAMENTALE !!!
@EqualsAndHashCode(of = "changes", callSuper = false)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Builder
public class ChangeLog {

    List<Integer> unitExamined;
    @Nullable
    House house;
    int houseNumber;
    List<ChangeLogUnitMember> unitMembers;
    String solvingTechnique;
    @Nullable
    String solvingTechniqueVariant;
    List<CellChange> changes;
    @NonFinal
    String explanation;

    @Override
    public String toString() {
        return solvingTechniqueVariant != null ? solvingTechniqueVariant : solvingTechnique;
    }
}
