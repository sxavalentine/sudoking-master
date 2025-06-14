package com.gianfro.games.entities;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import javax.annotation.Nullable;
import java.util.List;

@Data
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
    List<Change> changes;
    @NonFinal
    String explanation;

    @Override
    public String toString() {
        return solvingTechniqueVariant != null ? solvingTechniqueVariant : solvingTechnique;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChangeLog cl) {
            return cl.changes.equals(this.changes);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
