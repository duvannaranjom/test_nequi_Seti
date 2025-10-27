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

class AddProductUseCaseTest {

    private FranchiseRepositoryPort port;
    private AddProductUseCase useCase;

    @BeforeEach
    void setup() {
        port = Mockito.mock(FranchiseRepositoryPort.class);
        useCase = new AddProductUseCase(port);
    }

    @Test
    void addProduct_success() {
        String franchiseId = "f1";
        String branchId = "b1";

        Branch branch = Branch.builder().id(branchId).name("B1").products(new ArrayList<>()).build();
        Franchise franchise = Franchise.builder().id(franchiseId).name("Fran").branches(new ArrayList<>(List.of(branch))).build();

        when(port.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(port.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Product product = Product.builder().id("p1").name("Prod").count(5).build();

        StepVerifier.create(useCase.execute(franchiseId, branchId, product))
                .assertNext(f -> {
                    assertEquals(1, f.getBranches().get(0).getProducts().size());
                    assertEquals("Prod", f.getBranches().get(0).getProducts().get(0).getName());
                })
                .verifyComplete();

        ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
        verify(port).save(captor.capture());
        assertEquals(1, captor.getValue().getBranches().get(0).getProducts().size());
    }

    @Test
    void addProduct_branchNotFound_throwsDomainException() {
        Franchise f = Franchise.builder().id("f1").name("Fran").branches(List.of()).build();
        when(port.findById("f1")).thenReturn(Mono.just(f));

        StepVerifier.create(useCase.execute("f1", "b1", Product.builder().id("p1").name("X").build()))
                .expectError(DomainException.class)
                .verify();

        verify(port, never()).save(any());
    }

    @Test
    void addProduct_blankParams_throwImmediately() {
        assertThrows(DomainException.class, () -> useCase.execute(" ", "b1", Product.builder().build()).block());
        assertThrows(DomainException.class, () -> useCase.execute("f1", " ", Product.builder().build()).block());
        verifyNoInteractions(port);
    }
/*
    @Test
    void addProduct_franchiseNotFound_throwsDomainException() {
        when(port.findById("missing")).thenReturn(Mono.empty());
        StepVerifier.create(useCase.execute("missing", "b1", Product.builder().id("p1").build()))
                .expectError(DomainException.class)
                .verify();
    }

 */
}
