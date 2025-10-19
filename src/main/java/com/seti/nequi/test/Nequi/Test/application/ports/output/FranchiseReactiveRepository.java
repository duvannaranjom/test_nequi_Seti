package com.seti.nequi.test.Nequi.Test.application.ports.output;

import com.seti.nequi.test.Nequi.Test.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseReactiveRepository {
    Mono<Franchise> save(Franchise franchise);
    Flux<Franchise> findAll();
    Mono<Franchise> findById(String id);
    Mono<Void> deleteById(String id);
    Mono<Boolean> existsById(String id);
}
