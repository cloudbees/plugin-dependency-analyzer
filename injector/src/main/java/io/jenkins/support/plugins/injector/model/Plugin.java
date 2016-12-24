/*
 * The MIT License
 *
 * Copyright (c) 2016 Adrien Lecharpentier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.jenkins.support.plugins.injector.model;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Adrien Lecharpentier
 */
@NodeEntity(label = "Plugin")
public class Plugin {
    @GraphId
    private Long id;

    private String name;
    private String version;
    private String requiredCoreVersion;

    @Relationship(type = "DEPENDS_ON")
    private Set<DependencyRelation> dependencies = new HashSet<>();

    private Plugin() {
    }

    public Plugin(String name, String version) {
        this(name, version, null);
    }

    public Plugin(String name, String version, String requiredCoreVersion) {
        this.name = name;
        this.version = version;
        this.requiredCoreVersion = requiredCoreVersion;
    }

    public Long getId() {
        return id;
    }

    public Plugin setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getRequiredCoreVersion() {
        return requiredCoreVersion;
    }

    public Set<DependencyRelation> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    public Plugin dependsOn(Plugin pl, boolean optional) {
        this.dependencies.add(new DependencyRelation(this, pl, optional));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plugin plugin = (Plugin) o;
        return Objects.equals(name, plugin.name) &&
            Objects.equals(version, plugin.version) &&
            Objects.equals(dependencies, plugin.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }
}
