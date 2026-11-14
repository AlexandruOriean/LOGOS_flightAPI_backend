package com.codecool.flightapi.common.error;

/** A requested resource does not exist. Rendered as {@code 404 Not Found}. */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String resource, String field, Object value) {
        super("%s with %s '%s' was not found".formatted(resource, field, value));
    }

    public static ResourceNotFoundException byId(String resource, Object id) {
        return new ResourceNotFoundException(resource, "id", id);
    }
}
