package com.codecool.flightapi.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/** The dev-only tooling chain ({@link DevToolsSecurityConfig}) under the {@code dev} profile. */
@SpringBootTest(
        properties = {
            "spring.datasource.url=jdbc:h2:mem:dev-tools-it;DB_CLOSE_DELAY=-1",
            "app.jwt.secret=dGVzdC1vbmx5LWtleS1kby1ub3QtdXNlLWluLXByb2R1Y3Rpb24tMDEyMzQ1Njc4OWFiY2RlZmdoaWprbG1ub3A="
        })
@ActiveProfiles("dev")
@AutoConfigureMockMvc
class DevToolsSecurityIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("dev: H2 console path is not blocked by security and may be framed by the same origin")
    void h2ConsoleIsReachableAndFrameableInDev() throws Exception {
        MvcResult result = mockMvc.perform(get("/h2/"))
                .andExpect(header().string("X-Frame-Options", "SAMEORIGIN"))
                .andReturn();
        // MockMvc only drives the DispatcherServlet, so the console servlet itself is not reached here;
        // what matters is that security lets the request through.
        assertThat(result.getResponse().getStatus()).isNotIn(401, 403);
    }

    @Test
    @DisplayName("dev: the API still requires authentication and denies framing")
    void apiRulesAreUnchangedInDev() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/airplanes/1"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }
}
