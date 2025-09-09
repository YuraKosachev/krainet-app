package com.krainet.auth.core.interfaces.services.base;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public interface Deletable {
    void deleteById(UUID id);
}
