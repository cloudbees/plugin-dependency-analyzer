package com.cloudbees.ce.plugins.injector.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DependencyRelationTest {
    @Test
    public void shouldBeTheSameIfPluginAndOptionalTheSame() {
        DependencyRelation dep1 = new DependencyRelation(new Plugin("bar", "0.1"), new Plugin("foo", "0.1"), false);
        DependencyRelation dep2 = new DependencyRelation(new Plugin("bar", "0.1"), new Plugin("foo", "0.1"), false);

        assertThat(dep1).isEqualTo(dep1);
        assertThat(dep1).isEqualTo(dep2);
        assertThat(dep2).isEqualTo(dep2);
    }

    @Test
    public void shouldBeTheDifferentIfOptionalIsDifferent() {
        DependencyRelation dep1 = new DependencyRelation(new Plugin("bar", "0.1"), new Plugin("foo", "0.1"), false);
        DependencyRelation dep2 = new DependencyRelation(new Plugin("bar", "0.1"), new Plugin("foo", "0.1"), true);

        assertThat(dep1).isEqualTo(dep1);
        assertThat(dep1).isNotEqualTo(dep2);
        assertThat(dep2).isEqualTo(dep2);
    }
}
