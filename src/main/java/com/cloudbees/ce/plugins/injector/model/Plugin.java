package com.cloudbees.ce.plugins.injector.model;

import com.cloudbees.ce.plugins.injector.config.VersionNumberMapping;
import hudson.util.VersionNumber;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.id.UuidStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Adrien Lecharpentier
 */
@NodeEntity(label = "Plugin")
public class Plugin {
    @Id @GeneratedValue(strategy = UuidStrategy.class)
    private String id;

    private String name;
    @Convert(VersionNumberMapping.VersionNumberConverter.class)
    private VersionNumber coreVersion;
    @Convert(VersionNumberMapping.VersionNumberConverter.class)
    private VersionNumber version;
    private Tier tier;

    @Relationship(type = "DEPENDS_ON")
    private Set<DependencyRelation> dependencies = new HashSet<>();

    private Plugin() {
    }

    public Plugin(String name, String version) {
        this(name, version, null);
    }

    public Plugin(String name, String version, String coreVersion) {
        this(name, version, coreVersion, Tier.TIER_3);
    }

    public Plugin(String name, String version, String coreVersion, Tier tier) {
        this.name = name;
        this.version = new VersionNumber(version);
        this.coreVersion = coreVersion == null ? null : new VersionNumber(coreVersion);
        this.tier = tier;
    }

    public String getId() {
        return id;
    }

    public Plugin setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public VersionNumber getVersion() {
        return version;
    }

    public VersionNumber getCoreVersion() {
        return coreVersion;
    }

    public Set<DependencyRelation> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    public Plugin dependsOn(Plugin pl, boolean optional) {
        this.dependencies.add(new DependencyRelation(this, pl, optional));
        return this;
    }

    public Tier getTier() {
        return tier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plugin plugin = (Plugin) o;
        return Objects.equals(name, plugin.name) &&
            Objects.equals(version, plugin.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }
}
