package com.seti.nequi.test.domain.usecase.franchise;

import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.exception.DomainException;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

class CreateFranchiseUseCaseTest {

    @Test
    void create_ok() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        CreateFranchiseUseCase useCase = new CreateFranchiseUseCase(port);

        Franchise input = Franchise.builder().name("New Fran").build();
        Mockito.when(port.save(input))
                .thenReturn(Mono.just(Franchise.builder().id("f1").name("New Fran").build()));

        StepVerifier.create(useCase.execute(input))
                .expectNextMatches(f -> "f1".equals(f.getId()) && "New Fran".equals(f.getName()))
                .verifyComplete();

        Mockito.verify(port).save(input);
        Mockito.verifyNoMoreInteractions(port);
    }
/*
    @Test
    void create_blankName_throwsDomainException() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        CreateFranchiseUseCase useCase = new CreateFranchiseUseCase(port);

        Franchise bad = Franchise.builder().name(" ").build();

        StepVerifier.create(useCase.execute(bad))
                .expectError(DomainException.class)
                .verify();

        Mockito.verifyNoInteractions(port);
    }

    @Test
    void create_nullFranchise_throwsDomainException() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        CreateFranchiseUseCase useCase = new CreateFranchiseUseCase(port);

        StepVerifier.create(useCase.execute(null))
                .expectError(DomainException.class)
                .verify();

        Mockito.verifyNoInteractions(port);
    }
    */

}
