package com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence;

import com.seti.nequi.test.Nequi.Test.application.ports.output.FranchiseReactiveRepository;
import com.seti.nequi.test.Nequi.Test.domain.model.Franchise;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence.entity.FranchiseEntity;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence.mapper.FranchiseMapper;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence.repository.MongoFranchiseReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FranchiseReactiveRepositoryImpl implements FranchiseReactiveRepository {

    private final MongoFranchiseReactiveRepository mongoRepository;
    private final FranchiseMapper mapper;
    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity franchiseEntity = mapper.toEntity(franchise);
        return mongoRepository.save(franchiseEntity)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return mongoRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return mongoRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return mongoRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return mongoRepository.existsById(id);
    }
}
