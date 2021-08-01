package com.bol.model.enums;

import java.util.Objects;

public enum GameStatus implements PersistableEnum<String> {

    NEW("new"),
    IN_PROGRESS("in_progress"),
    FINISHED("finished");


    private final String value;

    GameStatus(String value) {
        this.value = value;
    }

    public static GameStatus findByValue(String value) {

        if (Objects.isNull(value)) {
            return null;
        }
        for (GameStatus status : GameStatus.values()) {
            if (status.value.equals(value))
                return status;
        }

        return null;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static class GameStatusConverter extends AbstractEnumConverter<String, GameStatus> {
    }
}
