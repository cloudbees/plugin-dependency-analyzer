package com.cloudbees.ce.plugins.injector.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PluginTest {
    @Test
    public void shouldBeAbleToRecogniseSamePlugins() {
        Plugin p1 = new Plugin("foo", "0.1", "1.0");
        Plugin p2 = new Plugin("foo", "0.1", "1.0");

        assertThat(p1).isEqualTo(p1);
        assertThat(p1).isEqualTo(p2);
        assertThat(p2).isEqualTo(p1);
        assertThat(p2).isEqualTo(p2);
    }

    @Test
    public void shouldBeAbleToSeePluginsAsDifferentWithSameName() {
        Plugin p1 = new Plugin("foo", "0.1", "1.0");
        Plugin p2 = new Plugin("foo", "0.2", "1.0");

        assertThat(p1).isEqualTo(p1);
        assertThat(p1).isNotEqualTo(p2);
        assertThat(p2).isNotEqualTo(p1);
        assertThat(p2).isEqualTo(p2);
    }

    @Test
    public void shouldBeAbleToSeePluginsAsDifferentWithSameVersion() {
        Plugin p1 = new Plugin("foo", "0.1", "1.0");
        Plugin p2 = new Plugin("bar", "0.1", "1.0");

        assertThat(p1).isEqualTo(p1);
        assertThat(p1).isNotEqualTo(p2);
        assertThat(p2).isNotEqualTo(p1);
        assertThat(p2).isEqualTo(p2);
    }

    @Test
    public void shouldBeAbleToRecognisePluginsWithDifferentDependencies() {
        Plugin p1 = new Plugin("foo", "0.1", "1.0");
        p1.dependsOn(new Plugin("aaa", "0.1", "1.0"), false);
        Plugin p2 = new Plugin("foo", "0.1", "1.0");

        assertThat(p1).isEqualTo(p1);
        assertThat(p1).isEqualTo(p2);
        assertThat(p2).isEqualTo(p1);
        assertThat(p2).isEqualTo(p2);
    }
}
