package com.cloudbees.ce.plugins.injector.repository;

import com.cloudbees.ce.plugins.injector.model.Plugin;
import com.cloudbees.ce.plugins.injector.model.PluginNameAndTier;
import hudson.util.VersionNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Adrien Lecharpentier
 */
@Repository
public interface PluginRepository extends Neo4jRepository<Plugin, String> {
    @Depth(value = 2)
    Plugin findByNameAndVersion(String name, VersionNumber version);

    @Query("MATCH (n:Plugin {name:{0}}) RETURN n.version AS version")
    List<VersionNumber> getVersionsOfPlugin(String name);

    @Query("MATCH (n:Plugin {name:{0}}) SET n.tier = {1}")
    void setTierForPlugin(String name, String tier);

    @Query(
          value = "MATCH (n:Plugin) RETURN DISTINCT n.name AS name, n.tier AS tier",
          countQuery = "MATCH (n:Plugin) RETURN count(DISTINCT n.name)"
    )
    Page<PluginNameAndTier> getPluginsWithTier(Pageable page);

    @Query("MATCH (n:Plugin) RETURN count(DISTINCT n.name)")
    long countUniquePlugins();

    @Query(
            value = "MATCH (n:Plugin) WITH DISTINCT n.name AS name, n.tier AS tier WHERE name =~ {0} RETURN name, tier",
            countQuery = "MATCH (n:Plugin) WITH DISTINCT n.name AS name WHERE name =~ {0} RETURN count(name)"
    )
    Page<PluginNameAndTier> findAllByName(String name, Pageable page);
}
