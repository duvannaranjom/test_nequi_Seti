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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProductNameUseCaseTest {

    private FranchiseRepositoryPort port;
    private UpdateProductNameUseCase useCase;

    @BeforeEach
    void setup() {
        port = Mockito.mock(FranchiseRepositoryPort.class);
        useCase = new UpdateProductNameUseCase(port);
    }

    @Test
    void updateName_success() {
        Product p = Product.builder().id("p1").name("Old").count(3).build();
        Branch b = Branch.builder().id("b1").name("B1").products(List.of(p)).build();
        Franchise f = Franchise.builder().id("f1").branches(List.of(b)).build();

        when(port.findById("f1")).thenReturn(Mono.just(f));
        when(port.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("f1", "b1", "p1", "NewName"))
                .assertNext(fr -> assertEquals("NewName", fr.getBranches().get(0).getProducts().get(0).getName()))
                .verifyComplete();

        ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
        verify(port).save(captor.capture());
        assertEquals("NewName", captor.getValue().getBranches().get(0).getProducts().get(0).getName());
    }

    @Test
    void updateName_blankParams_throwImmediately() {
        assertThrows(DomainException.class, () -> useCase.execute(" ", "b1", "p1", "X").block());
        assertThrows(DomainException.class, () -> useCase.execute("f1", " ", "p1", "X").block());
        assertThrows(DomainException.class, () -> useCase.execute("f1", "b1", " ", "X").block());
        assertThrows(DomainException.class, () -> useCase.execute("f1", "b1", "p1", " ").block());
        verifyNoInteractions(port);
    }

    @Test
    void updateName_productNotFound_throwsDomainException() {
        Branch b = Branch.builder().id("b1").products(List.of()).build();
        Franchise f = Franchise.builder().id("f1").branches(List.of(b)).build();

        when(port.findById("f1")).thenReturn(Mono.just(f));

        StepVerifier.create(useCase.execute("f1", "b1", "p1", "New"))
                .expectError(DomainException.class)
                .verify();

        verify(port, never()).save(any());
    }
/*
    @Test
    void updateName_franchiseNotFound_throwsDomainException() {
        when(port.findById("missing")).thenReturn(Mono.empty());
        StepVerifier.create(useCase.execute("missing", "b1", "p1", "New"))
                .expectError(DomainException.class)
                .verify();
    }

 */
}
