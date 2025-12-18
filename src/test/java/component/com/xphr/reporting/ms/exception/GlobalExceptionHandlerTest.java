package component.com.xphr.reporting.ms.exception;

import component.com.xphr.reporting.ms.ComponentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ComponentTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandlerTest.TestExceptionControllerConfig.class)
@DisplayName("GlobalExceptionHandler Component Tests")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = {"EMPLOYEE"})
    @DisplayName("Should handle generic exceptions and return 500 error page")
    void shouldHandleGenericException() throws Exception {
        // When & Then
        mockMvc.perform(get("/test/exception/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("errorCode"))
                .andExpect(model().attribute("errorMessage", "An unexpected error occurred. Please try again later."))
                .andExpect(model().attribute("errorCode", "500"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"EMPLOYEE"})
    @DisplayName("Should handle IllegalArgumentException and return 400 error page")
    void shouldHandleIllegalArgumentException() throws Exception {
        // When & Then
        mockMvc.perform(get("/test/exception/illegal-argument"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("errorCode"))
                .andExpect(model().attribute("errorCode", "400"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"EMPLOYEE"})
    @DisplayName("Should include exception message in errorMessage for IllegalArgumentException")
    void shouldIncludeExceptionMessageInErrorModel() throws Exception {
        // When & Then
        mockMvc.perform(get("/test/exception/illegal-argument-with-message"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorCode", "400"))
                .andExpect(model().attribute("errorMessage", "Invalid parameter provided"));
    }

    @TestConfiguration
    static class TestExceptionControllerConfig {
        @org.springframework.stereotype.Component
        @RestController
        static class TestExceptionController {

            @GetMapping("/test/exception/generic")
            public String throwGenericException() throws Exception {
                throw new RuntimeException("Test generic exception");
            }

            @GetMapping("/test/exception/illegal-argument")
            public String throwIllegalArgumentException() {
                throw new IllegalArgumentException("Invalid request");
            }

            @GetMapping("/test/exception/illegal-argument-with-message")
            public String throwIllegalArgumentExceptionWithMessage() {
                throw new IllegalArgumentException("Invalid parameter provided");
            }
        }
    }
}

