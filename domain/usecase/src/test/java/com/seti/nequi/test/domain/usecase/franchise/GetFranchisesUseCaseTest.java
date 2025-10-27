package com.seti.nequi.test.domain.usecase.franchise;

import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class GetFranchisesUseCaseTest {

    @Test
    void list_ok() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        GetFranchisesUseCase useCase = new GetFranchisesUseCase(port);

        Mockito.when(port.findAll()).thenReturn(Flux.just(
                Franchise.builder().id("f1").name("A").build(),
                Franchise.builder().id("f2").name("B").build()
        ));

        StepVerifier.create(useCase.execute())
                .expectNextMatches(f -> "f1".equals(f.getId()))
                .expectNextMatches(f -> "f2".equals(f.getId()))
                .verifyComplete();

        Mockito.verify(port).findAll();
        Mockito.verifyNoMoreInteractions(port);
    }

    @Test
    void list_empty_ok() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        GetFranchisesUseCase useCase = new GetFranchisesUseCase(port);

        Mockito.when(port.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute())
                .verifyComplete();

        Mockito.verify(port).findAll();
        Mockito.verifyNoMoreInteractions(port);
    }
}
