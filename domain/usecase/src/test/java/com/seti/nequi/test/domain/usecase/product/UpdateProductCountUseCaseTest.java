package com.seti.nequi.test.domain.usecase.product;

import com.seti.nequi.test.domain.model.entity.*;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class UpdateProductCountUseCaseTest {

    @Test
    void updateCount_ok() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        UpdateProductCountUseCase useCase = new UpdateProductCountUseCase(port);

        Product p = Product.builder().id("p1").name("P").count(1).build();
        Branch b = Branch.builder().id("b1").name("B").products(List.of(p)).build();
        Franchise f = Franchise.builder().id("f1").branches(List.of(b)).build();

        Mockito.when(port.findById("f1")).thenReturn(Mono.just(f));
        Mockito.when(port.save(Mockito.any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("f1", "b1", "p1", 9))
                .expectNextMatches(fr -> fr.getBranches().get(0).getProducts().get(0).getCount() == 9)
                .verifyComplete();
    }
/*
    @Test
    void updateCount_notFound_throws() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        UpdateProductCountUseCase useCase = new UpdateProductCountUseCase(port);

        Mockito.when(port.findById("f1")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("f1", "b1", "p1", 9))
                .expectError(com.seti.nequi.test.domain.model.exception.DomainException.class)
                .verify();
    }

 */
}
