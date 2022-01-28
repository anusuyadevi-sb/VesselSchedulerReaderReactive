package com.apmm.datareader.reactive.repo;

import com.apmm.datareader.reactive.entity.Event;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface EventRepo extends ReactiveMongoRepository<Event,String> {

    @Query("{'eventId' : ?0}")
    Mono<Event> findByEventId(String eventId);
}
