package com.codecool.flight_api_project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Boots the full application (Flyway migrations, JPA schema validation, security, JWT)
 * against an in-memory H2 database and exercises the authentication flow end to end.
 */
@SpringBootTest(
        properties = {
            "spring.datasource.url=jdbc:h2:mem:flight-api-it;DB_CLOSE_DELAY=-1",
            "spring.datasource.username=sa",
            "spring.datasource.password=",
            // Test-only key (64 random bytes, Base64). Never use outside tests.
            "app.jwt.secret=dGVzdC1vbmx5LWtleS1kby1ub3QtdXNlLWluLXByb2R1Y3Rpb24tMDEyMzQ1Njc4OWFiY2RlZmdoaWprbG1ub3A="
        })
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationFlowIT {

    private static final String PASSWORD = "Secret123!";

    @Autowired
    private MockMvc mockMvc;

    private String userToken;
    private String adminToken;

    @BeforeAll
    void registerAndLogin() throws Exception {
        register("it-user", "it-user@example.com", "");
        register("it-admin", "it-admin@example.com", ",\"roles\":[\"admin\"]");
        userToken = login("it-user");
        adminToken = login("it-admin");
    }

    @Test
    @DisplayName("login returns a signed JWT carrying the user's roles")
    void loginIssuesTokenWithRoles() throws Exception {
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials("it-user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        assertThat(userToken.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("wrong password is rejected with 401 problem+json")
    void wrongPasswordIsRejected() throws Exception {
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"it-user\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Invalid username or password."));
    }

    @Test
    @DisplayName("protected endpoint without a token -> 401 problem+json with WWW-Authenticate")
    void protectedEndpointWithoutTokenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/airports"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE, "Bearer"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.instance").value("/api/v1/airports"));
    }

    @Test
    @DisplayName("invalid path parameter on a public endpoint -> 400 problem+json")
    void invalidPathParameterIsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/flights/London/Henri/not-a-date"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("valid token grants access to authenticated endpoints")
    void validTokenGrantsAccess() throws Exception {
        mockMvc.perform(get("/api/v1/airports").header(HttpHeaders.AUTHORIZATION, bearer(userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].airportIataCode").value("OTP"));
    }

    @Test
    @DisplayName("tampered token is rejected with 401")
    void tamperedTokenIsRejected() throws Exception {
        String tampered = userToken.substring(0, userToken.length() - 2) + "xx";

        mockMvc.perform(get("/api/v1/airports").header(HttpHeaders.AUTHORIZATION, bearer(tampered)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("USER role cannot call admin endpoints")
    void userRoleIsForbiddenFromAdminEndpoints() throws Exception {
        mockMvc.perform(post("/api/v1/cities")
                        .header(HttpHeaders.AUTHORIZATION, bearer(userToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cityName\":\"Cluj\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @DisplayName("ADMIN role from the token claims can call admin endpoints")
    void adminRoleCanCallAdminEndpoints() throws Exception {
        mockMvc.perform(post("/api/v1/cities")
                        .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cityName\":\"Cluj\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cityName").value("Cluj"));
    }

    @Test
    @DisplayName("deleting a city still referenced by airports -> 409 problem+json, no SQL details")
    void deletingReferencedCityIsConflict() throws Exception {
        mockMvc.perform(delete("/api/v1/cities/1").header(HttpHeaders.AUTHORIZATION, bearer(adminToken)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Data integrity violation"));
    }

    private void register(String username, String email, String extraJson) throws Exception {
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"%s\",\"name\":\"IT\",\"email\":\"%s\",\"password\":\"%s\"%s}"
                                .formatted(username, email, PASSWORD, extraJson)))
                .andExpect(status().isOk());
    }

    private String login(String username) throws Exception {
        String body = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(username)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return JsonPath.read(body, "$.accessToken");
    }

    private static String credentials(String username) {
        return "{\"username\":\"%s\",\"password\":\"%s\"}".formatted(username, PASSWORD);
    }

    private static String bearer(String token) {
        return "Bearer " + token;
    }
}
