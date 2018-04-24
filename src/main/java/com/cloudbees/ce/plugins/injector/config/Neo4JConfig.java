package com.cloudbees.ce.plugins.injector.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Adrien Lecharpentier
 */
@Configuration
@EnableNeo4jRepositories(basePackages = {"com.cloudbees.ce.plugins.injector.repository"})
@EnableTransactionManagement
public class Neo4JConfig {
}
