package com.seti.nequi.test.domain.usecase.branch;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

class AddBranchToFranchiseUseCaseTest {

    @Test
    void addBranch_ok() {
        FranchiseRepositoryPort port = Mockito.mock(FranchiseRepositoryPort.class);
        AddBranchUseCase useCase = new AddBranchUseCase(port);

        Franchise f = Franchise.builder().id("f1").name("Fran").branches(new ArrayList<>()).build();
        Branch newBranch = Branch.builder().id("b1").name("Branch").build();

        Mockito.when(port.findById("f1")).thenReturn(Mono.just(f));
        Mockito.when(port.save(Mockito.any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("f1", newBranch))
                .expectNextMatches(fr -> fr.getBranches().size() == 1)
                .verifyComplete();
    }
}
