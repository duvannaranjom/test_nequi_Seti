package com.seti.nequi.test.domain.usecase.report;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.entity.Product;
import com.seti.nequi.test.domain.model.exception.DomainException;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class GetProductsWithMaxStockUseCaseTest {

    @Test
    void returns_max_product_per_branch() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        // Si tu clase es GetProductsWithMaxStockByBranchUseCase, cambia el nombre aquí:
        GetProductsWithMaxStockByBranchUseCase useCase = new GetProductsWithMaxStockByBranchUseCase(port);

        Product p1 = Product.builder().id("p1").name("P1").count(5).build();
        Product p2 = Product.builder().id("p2").name("P2").count(10).build();
        Branch b1 = Branch.builder().id("b1").name("B1").products(List.of(p1, p2)).build();

        Product q1 = Product.builder().id("q1").name("Q1").count(3).build();
        Branch b2 = Branch.builder().id("b2").name("B2").products(List.of(q1)).build();

        Franchise f = Franchise.builder().id("f1").name("Fran").branches(List.of(b1, b2)).build();

        Mockito.when(port.findById("f1")).thenReturn(Mono.just(f));

        StepVerifier.create(useCase.execute("f1"))
                .expectNextMatches(br ->
                        "b1".equals(br.getId()) &&
                                br.getProducts().size() == 1 &&
                                "p2".equals(br.getProducts().get(0).getId())
                )
                .expectNextMatches(br ->
                        "b2".equals(br.getId()) &&
                                br.getProducts().size() == 1 &&
                                "q1".equals(br.getProducts().get(0).getId())
                )
                .verifyComplete();

        Mockito.verify(port).findById("f1");
        Mockito.verifyNoMoreInteractions(port);
    }

    @Test
    void franchise_not_found_emits_domain_error_if_configured() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        GetProductsWithMaxStockByBranchUseCase useCase = new GetProductsWithMaxStockByBranchUseCase(port);

        Mockito.when(port.findById("missing")).thenReturn(Mono.empty());

        // Según tu implementación propuesta, se usa switchIfEmpty(Flux.error(new DomainException(...)))
        StepVerifier.create(useCase.execute("missing"))
                .expectError(DomainException.class)
                .verify();

        Mockito.verify(port).findById("missing");
        Mockito.verifyNoMoreInteractions(port);
    }

    @Test
    void branch_without_products_returns_empty_list_for_that_branch() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        GetProductsWithMaxStockByBranchUseCase useCase = new GetProductsWithMaxStockByBranchUseCase(port);

        Branch b = Branch.builder().id("b1").name("B1").products(List.of()).build();
        Franchise f = Franchise.builder().id("f1").name("Fran").branches(List.of(b)).build();

        Mockito.when(port.findById("f1")).thenReturn(Mono.just(f));

        StepVerifier.create(useCase.execute("f1"))
                .expectNextMatches(br ->
                        "b1".equals(br.getId()) &&
                                (br.getProducts() == null || br.getProducts().isEmpty()))
                .verifyComplete();
    }
}
