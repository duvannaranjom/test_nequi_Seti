package com.seti.nequi.test.Nequi.Test.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence.repository")
public class MongoConfig {
}
