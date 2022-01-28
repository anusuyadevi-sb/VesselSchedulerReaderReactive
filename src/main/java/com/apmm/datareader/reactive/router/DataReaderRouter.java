package com.apmm.datareader.reactive.router;

import com.apmm.datareader.reactive.handler.DataReaderHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.server.MethodNotAllowedException;

@Configuration
@EnableWebFlux
public class DataReaderRouter {

    @Bean
    public RouterFunction<?> route(DataReaderHandler handler) throws MethodNotAllowedException {

        return RouterFunctions
                .route(RequestPredicates.GET("/v1/events").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                    handler::getEvents)
                .andRoute(RequestPredicates.GET("/v1/event/{eventId}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        handler::getEventbyID)
                ;

    }

}
