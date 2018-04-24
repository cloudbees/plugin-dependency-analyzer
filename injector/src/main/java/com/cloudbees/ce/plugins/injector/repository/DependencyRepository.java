package com.cloudbees.ce.plugins.injector.repository;

import com.cloudbees.ce.plugins.injector.model.DependencyRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DependencyRepository extends Neo4jRepository<DependencyRelation, String> {
    @Query("MATCH (:Plugin {name:{0},version:{1}})-[dep:DEPENDS_ON {optional:{2}}]->(:Plugin {name:{3},version:{4}}) RETURN dep.id")
    Optional<String> getDependencyIdBySourceAndOptionalAndTarget(String sourceName, String sourceVersion, boolean isOptional, String targetName, String targetVersion);
}
