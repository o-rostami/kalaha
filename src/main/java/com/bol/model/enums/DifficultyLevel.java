package com.bol.model.enums;

import java.util.Objects;

public enum DifficultyLevel implements PersistableEnum<Integer> {

    EASY(4),
    MEDIUM(5),
    HARD(6);


    private Integer value;

    DifficultyLevel(Integer value) {
        this.value = value;
    }

    public static DifficultyLevel findByValue(Integer value) {

        if (Objects.isNull(value)) {
            return null;
        }
        for (DifficultyLevel status : DifficultyLevel.values()) {
            if (status.value.equals(value))
                return status;
        }

        return null;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static class DifficultyLevelConverter extends AbstractEnumConverter<Integer, DifficultyLevel> {
    }
}
