package com.bol.model.enums;


/**
 * to use a hibernate generic converter to prevent write a converter for all enums
 * all enums should implement this interface.
 */

public interface PersistableEnum<T> {
    T getValue();
}
