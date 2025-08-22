package be.aidji.flexigate.component;

import be.aidji.flexigate.service.DynamicRoutesService;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DynamicRouteDefinitionLocator implements RouteDefinitionLocator {

    private final DynamicRoutesService dynamicRoutesService;

    public DynamicRouteDefinitionLocator(DynamicRoutesService dynamicRoutesService) {
        this.dynamicRoutesService = dynamicRoutesService;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(dynamicRoutesService.loadRoutes());
    }
}