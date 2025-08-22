package be.aidji.flexigate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DynamicRoutesServiceTest {

    @InjectMocks
    private final DynamicRoutesService service = new DynamicRoutesService();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "routesFile", "src/test/resources/config/routes.json");
    }


    @Test
    void loadRoutesTest(){

        // When
        List<RouteDefinition> result = service.loadRoutes();

        // Then
        assertNotNull(result);
        assert(!result.isEmpty());
    }

}
