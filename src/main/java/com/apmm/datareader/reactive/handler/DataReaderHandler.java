package com.apmm.datareader.reactive.handler;

import com.apmm.datareader.reactive.entity.Event;
import com.apmm.datareader.reactive.exception.DataNotFoundException;
import com.apmm.datareader.reactive.repo.EventRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class DataReaderHandler {

    @Autowired
    private EventRepo repo;
    public Mono<ServerResponse> getEvents(ServerRequest serverRequest) {
        Flux<Event> events = repo.findAll()
                .switchIfEmpty(e-> Mono.error(new DataNotFoundException(HttpStatus.NOT_FOUND,"No data found")))
                ;


        return ServerResponse.ok().body(events, Event.class);
    }
    public Mono<ServerResponse> getEventbyID(ServerRequest serverRequest)  {
        String id = serverRequest.pathVariable("eventId");
        Mono<Event> eventbyID = repo.findByEventId(id)
                .switchIfEmpty(Mono.defer(()->Mono.error(new DataNotFoundException(HttpStatus.NOT_FOUND,"No data found for id - "+id))))
                ;





        return ServerResponse.ok().body(eventbyID, Event.class);
    }

}
