package com.bol.model.enums;


import com.bol.utils.GenericUtils;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.Objects;


/**
 * Hibernate generic converter to prevent write a converter for all enums<p>
 *
 * @author Omid Rostami
 */

public abstract class AbstractEnumConverter<T, E extends Enum<E> & PersistableEnum<T>> implements AttributeConverter<E, T> {
    private Class<E> enumClassType;

    protected AbstractEnumConverter() {
        enumClassType = (Class<E>) GenericUtils.extract(this.getClass(), 1);
    }

    /**
     * enum to column
     *
     * @param attribute
     * @return value of enum
     */
    @Override
    public T convertToDatabaseColumn(E attribute) {
        return Objects.isNull(attribute) ? null : attribute.getValue();
    }

    /**
     * column  to enum
     *
     * @param dbData
     * @return enum
     */
    @Override
    public E convertToEntityAttribute(T dbData) {

        if (Objects.isNull(dbData)) return null;

        return EnumSet.allOf(enumClassType).stream()
                .filter(e -> Objects.equals(e.getValue(), dbData))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}


