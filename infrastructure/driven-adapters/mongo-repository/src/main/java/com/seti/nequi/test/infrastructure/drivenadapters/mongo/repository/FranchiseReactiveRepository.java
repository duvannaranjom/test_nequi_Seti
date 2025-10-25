package com.seti.nequi.test.infrastructure.drivenadapters.mongo.repository;

import com.seti.nequi.test.infrastructure.drivenadapters.mongo.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FranchiseReactiveRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
}
