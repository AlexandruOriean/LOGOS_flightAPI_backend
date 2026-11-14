package com.codecool.flight_api_project.common.web;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/** Builds {@code Location} headers for {@code 201 Created} responses. */
public final class Locations {

    private Locations() {}

    /** The URI of a newly created child resource: {@code <current request URI>/{id}}. */
    public static URI ofCreated(Object id) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
