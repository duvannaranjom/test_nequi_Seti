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

class UpdateBranchNameUseCaseTest {

    private FranchiseRepositoryPort port;
    private UpdateBranchNameUseCase useCase;

    @BeforeEach
    void setup() {
        port = Mockito.mock(FranchiseRepositoryPort.class);
        useCase = new UpdateBranchNameUseCase(port);
    }

    @Test
    void updateBranchName_success() {
        // given
        String franchiseId = "f1";
        String branchId = "b1";
        String newName = "New Name";

        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .name("Fran")
                .branches(new ArrayList<>(List.of(
                        Branch.builder().id("b1").name("Old").products(List.of()).build()
                )))
                .build();

        when(port.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(port.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // when
        Mono<Franchise> result = useCase.execute(franchiseId, branchId, newName);

        // then
        StepVerifier.create(result)
                .assertNext(f -> {
                    assertEquals("New Name", f.getBranches().get(0).getName());
                })
                .verifyComplete();

        ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
        verify(port).save(captor.capture());
        assertEquals("New Name", captor.getValue().getBranches().get(0).getName());
        verify(port).findById(franchiseId);
        verifyNoMoreInteractions(port);
    }

    @Test
    void updateBranchName_branchNotFound_throwsDomainException() {
        String franchiseId = "f1";
        String branchId = "nope";
        String newName = "X";

        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .name("Fran")
                .branches(new ArrayList<>(List.of(
                        Branch.builder().id("b1").name("B1").products(List.of()).build()
                )))
                .build();

        when(port.findById(franchiseId)).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.execute(franchiseId, branchId, newName))
                .expectErrorMatches(ex -> ex instanceof DomainException &&
                        ex.getMessage().contains("Branch not found"))
                .verify();

        verify(port).findById(franchiseId);
        verify(port, never()).save(any());
        verifyNoMoreInteractions(port);
    }

    @Test
    void updateBranchName_franchiseNotFound_currentBehavior_isEmptyMono() {
        // Con la implementación actual (sin switchIfEmpty(error)), el Mono queda vacío
        String franchiseId = "missing";
        String branchId = "b1";

        when(port.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(franchiseId, branchId, "Name"))
                .verifyComplete(); // no emite valores, no error

        verify(port).findById(franchiseId);
        verify(port, never()).save(any());
        verifyNoMoreInteractions(port);
    }

/*
    // Si prefieres lanzar DomainException cuando no exista la franquicia,
    // agrega en el use case: .switchIfEmpty(Mono.error(new DomainException("Franchise not found: " + franchiseId)))
    // y entonces usa este test en su lugar:

    @Test
    void updateBranchName_franchiseNotFound_shouldThrowDomainException_ifSwitchIfEmptyAdded() {
        String franchiseId = "missing";
        when(port.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(franchiseId, "b1", "Name"))
                .expectErrorMatches(ex -> ex instanceof DomainException &&
                        ex.getMessage().contains("Franchise not found"))
                .verify();
    }
*/

    @Test
    void updateBranchName_blankParams_throwsImmediately() {
        assertThrows(DomainException.class, () -> useCase.execute(" ", "b1", "X").block());
        assertThrows(DomainException.class, () -> useCase.execute("f1", " ", "X").block());
        assertThrows(DomainException.class, () -> useCase.execute("f1", "b1", " ").block());
        verifyNoInteractions(port);
    }
}
