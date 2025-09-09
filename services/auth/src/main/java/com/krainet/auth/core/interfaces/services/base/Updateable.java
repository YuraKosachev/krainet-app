package com.krainet.auth.core.interfaces.services.base;

import java.util.function.Function;


public interface Updateable<TDtoCreate, TEntity> {
    <T> T update(TDtoCreate dto, Function<TEntity, T> mapper);
}