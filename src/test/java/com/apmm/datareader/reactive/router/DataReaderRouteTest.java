package com.apmm.datareader.reactive.router;

import com.apmm.datareader.reactive.entity.Event;
import com.apmm.datareader.reactive.handler.DataReaderHandler;
import com.apmm.datareader.reactive.repo.EventRepo;

import com.apmm.datareader.reactive.utils.EventUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataReaderRouter.class,DataReaderHandler.class})
@WebFluxTest

public class DataReaderRouteTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private EventRepo repo;

    @Before
    public void setUp() {

        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void testGetByEventId() {


        Event event = EventUtils.getEvent();
        Mono<Event> eventMono = Mono.just(event);

        given(repo.findByEventId("1")).willReturn(eventMono);
        webTestClient.get()
                .uri("/v1/event/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Event.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse.getId()).isEqualTo("12345");
                            Assertions.assertThat(userResponse.getEventId()).isEqualTo("123");
                            Assertions.assertThat(userResponse.getEventJson()).isEqualTo("eventJson");
                            Assertions.assertThat(userResponse.getEventMessage()).isEqualTo("eventXml");
                        }
                );
    }
        @Test
        public void testGetByEvents() {



            Event event= EventUtils.getEvent();
            Flux<Event> fluxEvent = Flux.just(event);

            given(repo.findAll()).willReturn(fluxEvent);
            webTestClient.get()
                    .uri("/v1/events")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(Event.class)
                    .value(userResponse -> {
                       // Assertions.assertThat(userResponse.get(0).getId()).isEqualTo("ABC123");
                                Assertions.assertThat(userResponse.get(0).getId()).isEqualTo("12345");
                                Assertions.assertThat(userResponse.get(0).getEventId()).isEqualTo("123");
                                Assertions.assertThat(userResponse.get(0).getEventJson()).isEqualTo("eventJson");
                                Assertions.assertThat(userResponse.get(0).getEventMessage()).isEqualTo("eventXml");
                            }
                    );
    }

    @Test
    public void testGetByEventIdException() {


        given(repo.findByEventId("1")).willReturn(Mono.empty());
        webTestClient.get()
                .uri("/v1/event/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                .expectStatus().isNotFound()

                ;
    }



}
