package ru.itis.master.party.dormdeals.enums;

import java.util.List;
import java.util.NoSuchElementException;

public enum EntityType {
    USER("users"),
    SHOP("shops"),
    PRODUCT("products");
    private final List<EntityType> entityTypes;
    private final String entity;

    EntityType(String entity) {
        this.entity = entity;
        entityTypes = List.of(values());
    }

    public EntityType from(String s) {
        for (EntityType entityType : entityTypes) {
            if (entityType.entity.equals(s)) {
                return entityType;
            }
        }
        throw new NoSuchElementException(EntityType.class + " for s = " + s);
    }

    public List<EntityType> entityTypes() {
        return entityTypes;
    }

    public String entity() {
        return entity;
    }
}
