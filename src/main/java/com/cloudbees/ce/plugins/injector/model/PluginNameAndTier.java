package com.cloudbees.ce.plugins.injector.model;

import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class PluginNameAndTier {
    private String name;
    private String tier;

    public String getName() {
        return name;
    }

    public String getTier() {
        return tier;
    }
}
