package com.seti.nequi.test.domain.usecase.franchise;

import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class DeleteFranchiseUseCaseTest {

    @Test
    void delete_ok() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        DeleteFranchiseUseCase useCase = new DeleteFranchiseUseCase(port);

        Franchise existing = Franchise.builder().id("f1").name("Fran").build();
        Mockito.when(port.findById("f1")).thenReturn(Mono.just(existing));
        Mockito.when(port.deleteById("f1")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("f1"))
                .verifyComplete();

        Mockito.verify(port).deleteById("f1");
    }
/*

    @Test
    void delete_notFound() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        DeleteFranchiseUseCase useCase = new DeleteFranchiseUseCase(port);
        Mockito.when(port.findById("x")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("x"))
                .verifyErrorMatches(e -> e instanceof com.seti.nequi.test.domain.model.exception.DomainException);
    }

 */
}
