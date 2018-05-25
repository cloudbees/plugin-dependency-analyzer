package com.cloudbees.ce.plugins.injector.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.id.UuidStrategy;

import java.util.Objects;

/**
 * @author Adrien Lecharpentier
 */
@RelationshipEntity(type = "DEPENDS_ON")
public class DependencyRelation {
    @Id @GeneratedValue(strategy = UuidStrategy.class)
    private String id;
    @Property
    private boolean optional;

    @JsonIgnore @StartNode
    private Plugin source;
    @EndNode
    private Plugin target;

    private DependencyRelation() {
    }

    DependencyRelation(Plugin source, Plugin target, boolean optional) {
        this.source = source;
        this.target = target;
        this.optional = optional;
    }

    public String getId() {
        return id;
    }

    public DependencyRelation setId(String id) {
        this.id = id;
        return this;
    }

    public boolean isOptional() {
        return optional;
    }

    public DependencyRelation setOptional(boolean optional) {
        this.optional = optional;
        return this;
    }

    public Plugin getSource() {
        return source;
    }

    public DependencyRelation setSource(Plugin source) {
        this.source = source;
        return this;
    }

    public Plugin getTarget() {
        return target;
    }

    public void setTarget(Plugin dep) {
        this.target = dep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DependencyRelation that = (DependencyRelation) o;
        return optional == that.optional &&
              Objects.equals(source, that.source) &&
              Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
