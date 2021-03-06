package com.informationservice.repositories;

import com.informationservice.model.OpenApi;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface OpenApiRepository extends CrudRepository<OpenApi, String> {
    Optional<OpenApi> findById(String id);

    Optional<List<OpenApi>> findByName(String name);


}
