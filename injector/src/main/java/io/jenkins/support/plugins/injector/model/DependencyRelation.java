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

import org.neo4j.ogm.annotation.*;

/**
 * @author Adrien Lecharpentier
 */
@RelationshipEntity(type = "DEPENDS_ON")
public class DependencyRelation {
    @GraphId
    private Long id;
    @Property
    private boolean optional;

    @StartNode
    private Plugin source;
    @EndNode
    private Plugin dependency;

    private DependencyRelation() {
    }

    DependencyRelation(Plugin source, Plugin dependency, boolean optional) {
        this.source = source;
        this.dependency = dependency;
        this.optional = optional;
    }

    public boolean isOptional() {
        return optional;
    }

    public Plugin getSource() {
        return source;
    }

    public Plugin getDependency() {
        return dependency;
    }

    public void setDependency(Plugin dep) {
        this.dependency = dep;
    }
}
