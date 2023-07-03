package ru.itis.master.party.dormdeals.enums;

import java.util.List;
import java.util.NoSuchElementException;

public enum EntityType {
    USER("users"),
    SHOP("shops"),
    PRODUCT("products");
    private static final List<EntityType> ENTITY_TYPES = List.of(values());
    private final String entity;

    EntityType(String entity) {
        this.entity = entity;
    }

    public static EntityType from(String s) {
        for (EntityType entityType : ENTITY_TYPES) {
            if (entityType.entity.equals(s)) {
                return entityType;
            }
        }
        throw new NoSuchElementException(EntityType.class + " for s = " + s);
    }

    public List<EntityType> entityTypes() {
        return ENTITY_TYPES;
    }

    public String entity() {
        return entity;
    }
}
