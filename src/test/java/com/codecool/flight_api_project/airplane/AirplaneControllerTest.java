package com.codecool.flight_api_project.airplane;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @MockitoBean
    private AirplaneRepository airplaneRepository;

    private final Airplane airbus = new Airplane(1L, "Airbus", "A380", 853L, 1185);
    private final Airplane boeing = new Airplane(2L, "Boeing", "737MAX", 180L, 800);

    @Test
    @DisplayName("GET /api/v1/airplanes returns all airplanes")
    void getAirplanes() throws Exception {
        when(airplaneRepository.findAll()).thenReturn(List.of(airbus, boeing));

        mockMvc.perform(get("/api/v1/airplanes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].model").value("A380"))
                .andExpect(jsonPath("$[1].manufacturer").value("Boeing"));
    }

    @Test
    @DisplayName("GET /api/v1/airplanes/{id} returns the airplane")
    void getAirplaneBy() throws Exception {
        when(airplaneService.getAirplaneById(1L)).thenReturn(airbus);

        mockMvc.perform(get("/api/v1/airplanes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.model").value("A380"));
    }

    @Test
    @DisplayName("POST /api/v1/airplanes creates an airplane")
    void addAirplane() throws Exception {
        when(airplaneService.addAirplane(any(Airplane.class))).thenReturn(airbus);

        mockMvc.perform(post("/api/v1/airplanes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AIRPLANE_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/v1/airplanes/{id} deletes the airplane")
    void deleteAirplaneById() throws Exception {
        mockMvc.perform(delete("/api/v1/airplanes/{id}", 1).with(csrf())).andExpect(status().isOk());

        verify(airplaneService).deleteAirplaneByID(1L);
    }

    @Test
    @DisplayName("PUT /api/v1/airplanes/{id} updates the airplane")
    void updateAirplaneById() throws Exception {
        when(airplaneService.updateAirplaneById(eq(1L), any(Airplane.class))).thenReturn(airbus);

        mockMvc.perform(put("/api/v1/airplanes/{id}", 1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AIRPLANE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("A380"));
    }
}
