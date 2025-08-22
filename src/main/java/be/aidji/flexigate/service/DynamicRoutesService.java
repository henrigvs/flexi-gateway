package be.aidji.flexigate.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DynamicRoutesService {

    @Value("${ROUTES_FILE:config/routes.json}")
    private String routesFile;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public List<RouteDefinition> loadRoutes() {


        if(routesFile == null || routesFile.isEmpty()) {
            throw new IllegalArgumentException("Routes file path is not set or does not exist");

        } else {
            Path routesFilePath = Paths.get(routesFile);
            try {
                log.info("🔄 Chargement des routes depuis {}", routesFilePath.toAbsolutePath());
                String json = Files.readString(routesFilePath);
                List<Map<String, Object>> routes = OBJECT_MAPPER.readValue(json, new TypeReference<>() {});

                return routes.stream().map(this::mapToRouteDefinition).toList();
            } catch (Exception e) {
                log.error("❌ Impossible de charger les routes", e);
                return List.of();
            }
        }
    }

    private RouteDefinition mapToRouteDefinition(Map<String, Object> route) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId((String) route.get("id"));
        definition.setUri(URI.create((String) route.get("uri")));
        definition.setPredicates(((List<String>) route.get("predicates")).stream()
                .map(p -> {
                    PredicateDefinition predicate = new PredicateDefinition();
                    predicate.setName(p.split("=")[0]);
                    predicate.addArg("_genkey_0", p.split("=")[1]);
                    return predicate;
                }).toList());
        definition.setFilters(((List<String>) route.get("filters")).stream()
                .map(f -> {
                    FilterDefinition filter = new FilterDefinition();
                    filter.setName(f.split("=")[0]);
                    return filter;
                }).toList());
        return definition;
    }
}
