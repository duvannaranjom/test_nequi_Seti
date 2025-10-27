package com.seti.nequi.test.infrastructure.entrypoints.web;

import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.exception.DomainException;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(controllers = FranchiseController.class)
@Import(FranchiseControllerTest.TestConfig.class)
class FranchiseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FranchiseRepositoryPort franchiseRepository;

    @Configuration
    static class TestConfig {
        // puedes definir beans simulados si es necesario
    }

    @Test
    void getById_shouldReturnNotFound_ifMissing() {
        Mockito.when(franchiseRepository.findById("missing")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/franchises/missing")
                .exchange()
                .expectStatus().isNotFound();
    }

    /*
    @Test
    void createFranchise_shouldReturnCreated() {
        Franchise req = Franchise.builder().name("Fran").build();
        Franchise saved = Franchise.builder().id("f1").name("Fran").build();

        Mockito.when(franchiseRepository.save(any())).thenReturn(Mono.just(saved));

        webTestClient.post()
                .uri("/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("f1")
                .jsonPath("$.name").isEqualTo("Fran");
    }

    @Test
    void getAll_shouldReturnList() {
        Mockito.when(franchiseRepository.findAll())
                .thenReturn(Flux.just(
                        Franchise.builder().id("f1").name("Fran1").build(),
                        Franchise.builder().id("f2").name("Fran2").build()
                ));

        webTestClient.get()
                .uri("/franchises")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("f1")
                .jsonPath("$[1].name").isEqualTo("Fran2");
    }

    @Test
    void getById_shouldReturnFranchise() {
        Mockito.when(franchiseRepository.findById("f1"))
                .thenReturn(Mono.just(Franchise.builder().id("f1").name("Fran").build()));

        webTestClient.get()
                .uri("/franchises/f1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("f1");
    }



    @Test
    void createFranchise_shouldReturnBadRequest_onDomainException() {
        Mockito.when(franchiseRepository.save(any()))
                .thenReturn(Mono.error(new DomainException("Invalid franchise")));

        webTestClient.post()
                .uri("/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Franchise.builder().name("").build())
                .exchange()
                .expectStatus().isBadRequest();
    }
    */
}
