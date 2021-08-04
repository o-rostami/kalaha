package com.bol.model.enums;

import java.util.Arrays;

public enum KalahaConstants {
    emptyStone(0),
    firstPitPlayerA(1),
    secondPitPlayerA(2),
    thirdPitPlayerA(3),
    forthPitPlayerA(4),
    fifthPitPlayerA(5),
    sixthPitPlayerA(6),
    leftPitHouseId(7),
    firstPitPlayerB(8),
    secondPitPlayerB(9),
    thirdPitPlayerB(10),
    forthPitPlayerB(11),
    fifthPitPlayerB(12),
    sixthPitPlayerB(13),
    rightPitHouseId(14);


    private final Integer value;

    KalahaConstants(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public static KalahaConstants findByValue(final Integer abbr) {
        return Arrays.stream(values()).filter(value -> value.getValue().equals(abbr)).findFirst().orElse(null);
    }

}
