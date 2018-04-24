package com.cloudbees.ce.plugins.injector.repository;

import com.cloudbees.ce.plugins.injector.model.Plugin;
import com.cloudbees.ce.plugins.injector.model.PluginNameAndTier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Adrien Lecharpentier
 */
@Repository
public interface PluginRepository extends Neo4jRepository<Plugin, String> {
    @Query("MATCH (n:Plugin {name:{0},version:{1}}) RETURN n")
    Optional<Plugin> findByNameAndVersion(String name, String version);

    @Query("MATCH (n:Plugin {name:{0}}) SET n.tier = {1}")
    void setTierForPlugin(String name, String tier);

    @Query(
          value = "MATCH (n:Plugin) RETURN DISTINCT n.name AS name, n.tier AS tier",
          countQuery = "MATCH (n:Plugin) RETURN count(distinct n.name)"
    )
    Page<PluginNameAndTier> getPluginsWithTier(Pageable page);

    @Query("MATCH (n:Plugin) RETURN count(DISTINCT n.name)")
    long countUniquePlugins();
}
