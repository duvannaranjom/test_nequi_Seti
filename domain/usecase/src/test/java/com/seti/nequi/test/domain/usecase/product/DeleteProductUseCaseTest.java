package com.seti.nequi.test.domain.usecase.product;

import com.seti.nequi.test.domain.model.entity.*;
import com.seti.nequi.test.domain.model.exception.DomainException;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteProductUseCaseTest {

    private FranchiseRepositoryPort port;
    private DeleteProductUseCase useCase;

    @BeforeEach
    void setup() {
        port = Mockito.mock(FranchiseRepositoryPort.class);
        useCase = new DeleteProductUseCase(port);
    }

    @Test
    void deleteProduct_success() {
        Product p1 = Product.builder().id("p1").name("Prod1").count(1).build();
        Product p2 = Product.builder().id("p2").name("Prod2").count(2).build();
        Branch branch = Branch.builder().id("b1").name("B1").products(new ArrayList<>(List.of(p1, p2))).build();
        Franchise franchise = Franchise.builder().id("f1").branches(new ArrayList<>(List.of(branch))).build();

        when(port.findById("f1")).thenReturn(Mono.just(franchise));
        when(port.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("f1", "b1", "p1"))
                .assertNext(f -> {
                    List<Product> remaining = f.getBranches().get(0).getProducts();
                    assertEquals(1, remaining.size());
                    assertEquals("p2", remaining.get(0).getId());
                })
                .verifyComplete();

        ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
        verify(port).save(captor.capture());
        assertEquals(1, captor.getValue().getBranches().get(0).getProducts().size());
    }
/*
    @Test
    void deleteProduct_notFound_throwsDomainException() {
        Branch branch = Branch.builder().id("b1").products(List.of()).build();
        Franchise f = Franchise.builder().id("f1").branches(List.of(branch)).build();
        when(port.findById("f1")).thenReturn(Mono.just(f));

        StepVerifier.create(useCase.execute("f1", "b1", "p1"))
                .expectError(DomainException.class)
                .verify();
        verify(port, never()).save(any());
    }
    */
/*
    @Test
    void deleteProduct_franchiseNotFound_throwsDomainException() {
        when(port.findById("missing")).thenReturn(Mono.empty());
        StepVerifier.create(useCase.execute("missing", "b1", "p1"))
                .expectError(DomainException.class)
                .verify();
    }
*/
    @Test
    void deleteProduct_blankParams_throwImmediately() {
        assertThrows(DomainException.class, () -> useCase.execute(" ", "b1", "p1").block());
        assertThrows(DomainException.class, () -> useCase.execute("f1", " ", "p1").block());
        assertThrows(DomainException.class, () -> useCase.execute("f1", "b1", " ").block());
        verifyNoInteractions(port);
    }
}
