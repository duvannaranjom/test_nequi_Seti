package com.seti.nequi.test.infrastructure.drivenadapters.mongo.adapter;

import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.infrastructure.drivenadapters.mongo.mapper.FranchiseMongoMapper;
import com.seti.nequi.test.infrastructure.drivenadapters.mongo.repository.FranchiseReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FranchiseMongoAdapter implements FranchiseRepositoryPort {

    private final FranchiseReactiveRepository repository;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(FranchiseMongoMapper.toDocument(franchise))
                .map(FranchiseMongoMapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id).map(FranchiseMongoMapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return repository.findAll().map(FranchiseMongoMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}
