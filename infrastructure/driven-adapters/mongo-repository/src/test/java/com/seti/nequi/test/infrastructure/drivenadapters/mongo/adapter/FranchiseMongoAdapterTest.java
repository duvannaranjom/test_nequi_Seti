package com.seti.nequi.test.infrastructure.drivenadapters.mongo.adapter;

import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.infrastructure.drivenadapters.mongo.document.FranchiseDocument;
import com.seti.nequi.test.infrastructure.drivenadapters.mongo.repository.FranchiseReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FranchiseMongoAdapterTest {

    @Test
    void save_maps_and_returns_domain() {
        FranchiseReactiveRepository repo = Mockito.mock(FranchiseReactiveRepository.class);
        FranchiseMongoAdapter adapter = new FranchiseMongoAdapter(repo);

        when(repo.save(Mockito.any(FranchiseDocument.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Franchise in = Franchise.builder().id("f1").name("F").build();

        StepVerifier.create(adapter.save(in))
                .expectNextMatches(fr -> "f1".equals(fr.getId()) && "F".equals(fr.getName()))
                .verifyComplete();
    }

    private FranchiseReactiveRepository repository;
    private FranchiseMongoAdapter adapter;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(FranchiseReactiveRepository.class);
        adapter = new FranchiseMongoAdapter(repository);
    }

    @Test
    void save_shouldPersistFranchise() {
        Franchise f = Franchise.builder().id("f1").name("Fran").build();
        FranchiseDocument doc = FranchiseDocument.builder().id("f1").name("Fran").build();

        when(repository.save(doc)).thenReturn(Mono.just(doc));

        StepVerifier.create(adapter.save(f))
                .assertNext(result -> {
                    assertEquals("f1", result.getId());
                    assertEquals("Fran", result.getName());
                })
                .verifyComplete();

        ArgumentCaptor<FranchiseDocument> captor = ArgumentCaptor.forClass(FranchiseDocument.class);
        verify(repository).save(captor.capture());
        assertEquals("Fran", captor.getValue().getName());
    }

    @Test
    void findById_shouldReturnMappedEntity() {
        FranchiseDocument doc = FranchiseDocument.builder().id("f1").name("Fran").build();
        when(repository.findById("f1")).thenReturn(Mono.just(doc));

        StepVerifier.create(adapter.findById("f1"))
                .assertNext(f -> {
                    assertEquals("f1", f.getId());
                    assertEquals("Fran", f.getName());
                })
                .verifyComplete();

        verify(repository).findById("f1");
    }

    @Test
    void findById_shouldReturnEmptyIfNotFound() {
        when(repository.findById("missing")).thenReturn(Mono.empty());
        StepVerifier.create(adapter.findById("missing"))
                .verifyComplete();
    }

    @Test
    void findAll_shouldReturnFluxMapped() {
        FranchiseDocument d1 = FranchiseDocument.builder().id("f1").name("Fran1").build();
        FranchiseDocument d2 = FranchiseDocument.builder().id("f2").name("Fran2").build();
        when(repository.findAll()).thenReturn(Flux.just(d1, d2));

        StepVerifier.create(adapter.findAll())
                .expectNextMatches(f -> "f1".equals(f.getId()))
                .expectNextMatches(f -> "f2".equals(f.getId()))
                .verifyComplete();

        verify(repository).findAll();
    }
}
