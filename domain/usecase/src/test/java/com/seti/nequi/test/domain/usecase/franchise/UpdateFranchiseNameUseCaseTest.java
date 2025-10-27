package com.seti.nequi.test.domain.usecase.franchise;

import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.exception.DomainException;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateFranchiseNameUseCaseTest {

    private FranchiseRepositoryPort port;
    private UpdateFranchiseNameUseCase useCase;

    @BeforeEach
    void setup() {
        port = Mockito.mock(FranchiseRepositoryPort.class);
        useCase = new UpdateFranchiseNameUseCase(port);
    }

    @Test
    void rename_success() {
        String id = "f1";
        String newName = "Renamed";

        Franchise f = Franchise.builder().id(id).name("Old").build();

        when(port.findById(id)).thenReturn(Mono.just(f));
        when(port.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute(id, newName))
                .assertNext(fr -> assertEquals("Renamed", fr.getName()))
                .verifyComplete();

        ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
        verify(port).save(captor.capture());
        assertEquals("Renamed", captor.getValue().getName());
        verify(port).findById(id);
        verifyNoMoreInteractions(port);
    }

    @Test
    void blankParams_throwImmediately() {
        assertThrows(DomainException.class, () -> useCase.execute(" ", "X").block());
        assertThrows(DomainException.class, () -> useCase.execute("f1", " ").block());
        verifyNoInteractions(port);
    }

    @Test
    void franchise_not_found_currentBehavior_isEmptyMono() {
        when(port.findById("missing")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("missing", "X"))
                .verifyComplete(); // Mono vacío (comportamiento actual)

        verify(port).findById("missing");
        verify(port, never()).save(any());
        verifyNoMoreInteractions(port);
    }

/*
    // Si agregas en el use case:
    // return repository.findById(franchiseId)
    //   .switchIfEmpty(Mono.error(new DomainException("Franchise not found: " + franchiseId)))
    //   .map(...)
    //   .flatMap(repository::save);

    @Test
    void franchise_not_found_shouldThrowDomainException_ifSwitchIfEmptyAdded() {
        when(port.findById("missing")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("missing", "X"))
                .expectError(DomainException.class)
                .verify();

        verify(port).findById("missing");
        verify(port, never()).save(any());
    }
*/
}
