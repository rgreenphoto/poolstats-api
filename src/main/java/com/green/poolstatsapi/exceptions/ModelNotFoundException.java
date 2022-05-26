package com.green.poolstatsapi.exceptions;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(String model, Long id) {
        super("Could not find "+ model + " with id of " + id);
    }
}
