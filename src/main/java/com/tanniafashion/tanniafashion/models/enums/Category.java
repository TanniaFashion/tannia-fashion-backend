package com.tanniafashion.tanniafashion.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    SHIRTS,
    PANTS,
    DRESSES,
    SHOES,
    JACKETS,
    ACCESSORIES,
    TSHIRTS,
    SHORTS;

    @JsonCreator
    public static Category fromString(String value) {
        return value == null ? null : Category.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toString() {
        return this.name();
    }
}
