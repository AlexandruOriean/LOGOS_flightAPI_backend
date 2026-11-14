package com.codecool.flightapi.common.error;

/** A resource cannot be created because a conflicting one already exists. Rendered as {@code 409 Conflict}. */
public class ResourceAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
