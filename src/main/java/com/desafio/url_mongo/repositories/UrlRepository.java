package com.desafio.url_mongo.repositories;

import com.desafio.url_mongo.entities.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {

    UrlEntity getOriginalUrlById(String id);

}
