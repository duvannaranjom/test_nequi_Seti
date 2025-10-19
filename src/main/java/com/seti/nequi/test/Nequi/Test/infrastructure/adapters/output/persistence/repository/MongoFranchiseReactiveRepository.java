package com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence.repository;

import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence.entity.FranchiseEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoFranchiseReactiveRepository extends ReactiveMongoRepository<FranchiseEntity, String> {}
