package com.codecool.flightapi.airplane;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codecool.flightapi.common.error.ResourceNotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Web layer of {@link AirplaneController}: status codes, headers, bodies and problem+json errors. */
@WebMvcTest(AirplaneController.class)
@WithMockUser
class AirplaneControllerTest {

    private static final String AIRPLANE_JSON = """
            {"manufacturer":"Airbus","model":"A380","numberOfSeats":853,"speed":1185}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AirplaneService airplaneService;

    private final Airplane airbus = new Airplane(1L, "Airbus", "A380", 853L, 1185);
    private final Airplane boeing = new Airplane(2L, "Boeing", "737MAX", 180L, 800);

    @Test
    @DisplayName("GET /api/v1/airplanes -> 200 with all airplanes")
    void getAirplanes() throws Exception {
        when(airplaneService.findAll()).thenReturn(List.of(airbus, boeing));

        mockMvc.perform(get("/api/v1/airplanes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].model").value("A380"))
                .andExpect(jsonPath("$[1].manufacturer").value("Boeing"));
    }

    @Test
    @DisplayName("GET /api/v1/airplanes/{id} -> 200 with the airplane")
    void getAirplane() throws Exception {
        when(airplaneService.findById(1L)).thenReturn(airbus);

        mockMvc.perform(get("/api/v1/airplanes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.model").value("A380"));
    }

    @Test
    @DisplayName("GET /api/v1/airplanes/{id} for an unknown id -> 404 problem+json")
    void getUnknownAirplane() throws Exception {
        when(airplaneService.findById(99L)).thenThrow(ResourceNotFoundException.byId("Airplane", 99L));

        mockMvc.perform(get("/api/v1/airplanes/{id}", 99))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Resource not found"))
                .andExpect(jsonPath("$.detail").value("Airplane with id '99' was not found"))
                .andExpect(jsonPath("$.instance").value("/api/v1/airplanes/99"));
    }

    @Test
    @DisplayName("POST /api/v1/airplanes -> 201 with Location header and the created airplane")
    void createAirplane() throws Exception {
        when(airplaneService.create(any(Airplane.class))).thenReturn(airbus);

        mockMvc.perform(post("/api/v1/airplanes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AIRPLANE_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/airplanes/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/v1/airplanes/{id} -> 200 with the updated airplane")
    void updateAirplane() throws Exception {
        when(airplaneService.update(eq(1L), any(Airplane.class))).thenReturn(airbus);

        mockMvc.perform(put("/api/v1/airplanes/{id}", 1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AIRPLANE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("A380"));
    }

    @Test
    @DisplayName("DELETE /api/v1/airplanes/{id} -> 204 without body")
    void deleteAirplane() throws Exception {
        mockMvc.perform(delete("/api/v1/airplanes/{id}", 1).with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(airplaneService).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/v1/airplanes/{id} for an unknown id -> 404 problem+json")
    void deleteUnknownAirplane() throws Exception {
        doThrow(ResourceNotFoundException.byId("Airplane", 99L))
                .when(airplaneService)
                .delete(99L);

        mockMvc.perform(delete("/api/v1/airplanes/{id}", 99).with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @DisplayName("malformed JSON body -> 400 problem+json")
    void malformedJsonIsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/airplanes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("non-numeric id -> 400 problem+json instead of 500")
    void nonNumericIdIsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/airplanes/{id}", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @DisplayName("unexpected exception -> 500 problem+json without internal details")
    void unexpectedErrorDoesNotLeakDetails() throws Exception {
        when(airplaneService.findAll()).thenThrow(new IllegalStateException("db password is hunter2"));

        mockMvc.perform(get("/api/v1/airplanes"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred."));
    }
}
