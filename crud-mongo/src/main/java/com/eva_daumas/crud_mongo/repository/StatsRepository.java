package com.eva_daumas.crud_mongo.repository;

import com.eva_daumas.crud_mongo.model.Stat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatsRepository extends MongoRepository<Stat,String> {
}
