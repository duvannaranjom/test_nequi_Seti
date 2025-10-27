package com.seti.nequi.test.domain.usecase.branch;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
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

class DeleteBranchUseCaseTest {

    private FranchiseRepositoryPort port;
    private DeleteBranchUseCase useCase;

    @BeforeEach
    void setup() {
        port = Mockito.mock(FranchiseRepositoryPort.class);
        useCase = new DeleteBranchUseCase(port);
    }

    @Test
    void deleteBranch_success() {
        // given
        String franchiseId = "f1";
        String branchId = "b1";

        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .name("Fran")
                .branches(new ArrayList<>(List.of(
                        Branch.builder().id("b1").name("B1").products(List.of()).build(),
                        Branch.builder().id("b2").name("B2").products(List.of()).build()
                )))
                .build();

        when(port.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(port.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // when
        Mono<Franchise> result = useCase.execute(franchiseId, branchId);

        // then
        StepVerifier.create(result)
                .assertNext(f -> {
                    assertEquals(1, f.getBranches().size());
                    assertEquals("b2", f.getBranches().get(0).getId());
                })
                .verifyComplete();

        ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
        verify(port).save(captor.capture());
        assertEquals(1, captor.getValue().getBranches().size());
        verify(port).findById(franchiseId);
        verifyNoMoreInteractions(port);
    }

    @Test
    void deleteBranch_branchNotFound_throwsDomainException() {
        String franchiseId = "f1";
        String branchId = "nope";

        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .name("Fran")
                .branches(new ArrayList<>(List.of(
                        Branch.builder().id("b1").name("B1").products(List.of()).build()
                )))
                .build();

        when(port.findById(franchiseId)).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.execute(franchiseId, branchId))
                .expectErrorMatches(ex -> ex instanceof DomainException &&
                        ex.getMessage().contains("Branch not found"))
                .verify();

        verify(port, times(1)).findById(franchiseId);
        verify(port, never()).save(any());
        verifyNoMoreInteractions(port);
    }

    @Test
    void deleteBranch_franchiseNotFound_throwsDomainException() {
        String franchiseId = "missing";
        String branchId = "b1";

        when(port.findById(franchiseId)).thenReturn(Mono.empty()); // tu implementación usa switchIfEmpty(error)

        StepVerifier.create(useCase.execute(franchiseId, branchId))
                .expectErrorMatches(ex -> ex instanceof DomainException &&
                        ex.getMessage().contains("Franchise not found"))
                .verify();

        verify(port, times(1)).findById(franchiseId);
        verify(port, never()).save(any());
        verifyNoMoreInteractions(port);
    }

    @Test
    void deleteBranch_blankParams_throwsImmediately() {
        // franchiseId en blanco
        assertThrows(DomainException.class, () -> useCase.execute(" ", "b1").block());
        // branchId en blanco
        assertThrows(DomainException.class, () -> useCase.execute("f1", " ").block());

        verifyNoInteractions(port);
    }
}
