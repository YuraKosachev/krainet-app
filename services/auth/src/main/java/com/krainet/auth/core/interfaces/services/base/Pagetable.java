package com.krainet.auth.core.interfaces.services.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.function.Function;

public interface Pagetable<TEntity> {
   <T> Page<T> getPages(Pageable pageable, Function<TEntity, T> mapper);
}
