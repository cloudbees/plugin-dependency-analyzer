package io.jenkins.support.plugins.injector.service;

import io.jenkins.support.plugins.injector.model.Plugin;
import org.junit.Test;

import java.io.IOException;
import java.util.jar.Manifest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * @author Adrien Lecharpentier
 */
public class PluginConverterTest {
    @Test
    public void shouldBeAbleToParseManifestCorrectly() throws IOException {
        Manifest manifest = new Manifest(this.getClass().getResourceAsStream("/manifest/correct-manifest.txt"));
        Plugin plugin = PluginConverter.fromManifest(manifest);

        assertAll(
            () -> assertThat(plugin.getName()).isEqualTo("foobar"),
            () -> assertThat(plugin.getVersion()).isEqualTo("1.0")
        );
    }
}