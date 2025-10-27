package com.seti.nequi.test.domain.usecase.franchise;

import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.exception.DomainException;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class GetFranchiseByIdUseCaseTest {

    @Test
    void getFranchise_ok() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        GetFranchiseByIdUseCase useCase = new GetFranchiseByIdUseCase(port);

        Mockito.when(port.findById("f1"))
                .thenReturn(Mono.just(Franchise.builder().id("f1").name("Fran").build()));

        StepVerifier.create(useCase.execute("f1"))
                .expectNextMatches(f -> f.getId().equals("f1"))
                .verifyComplete();
    }

    @Test
    void getFranchise_notFound_throws() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        GetFranchiseByIdUseCase useCase = new GetFranchiseByIdUseCase(port);

        Mockito.when(port.findById("x")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("x"))
                .expectError(DomainException.class)
                .verify();
    }
}
