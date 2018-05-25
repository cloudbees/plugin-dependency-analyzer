package com.cloudbees.ce.plugins.injector.service;

import com.cloudbees.ce.plugins.injector.model.Plugin;
import com.cloudbees.ce.plugins.injector.model.PluginNameAndTier;
import com.cloudbees.ce.plugins.injector.model.Tier;
import com.cloudbees.ce.plugins.injector.repository.DependencyRepository;
import com.cloudbees.ce.plugins.injector.repository.PluginRepository;
import hudson.util.VersionNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author Adrien Lecharpentier
 */
@Service
public class PluginService {
    private final PluginRepository pluginRepository;
    private final DependencyRepository dependencyRepository;

    public PluginService(PluginRepository pluginRepository, DependencyRepository dependencyRepository) {
        this.pluginRepository = pluginRepository;
        this.dependencyRepository = dependencyRepository;
    }

    @Transactional
    public Plugin save(Plugin plugin) {
        plugin.getDependencies().forEach(
              dep -> {
                  dep.setTarget(
                        this.getPlugin(dep.getTarget().getName(), dep.getTarget().getVersion())
                              .orElse(dep.getTarget())
                  );
                  dep.setId(
                        dependencyRepository.getDependencyIdBySourceAndOptionalAndTarget(
                              dep.getSource().getName(), dep.getSource().getVersion().toString(), dep.isOptional(), dep.getTarget().getName(), dep.getTarget().getVersion().toString()
                        ).orElse(null)
                  );
              }
        );
        plugin.setId(
              this.getPlugin(plugin.getName(), plugin.getVersion())
                    .map(Plugin::getId).orElse(null)
        );
        return pluginRepository.save(plugin);
    }

    public Mono<Plugin> fromBinary(File pluginBinary) {
        try(JarFile jar = new JarFile(pluginBinary)) {
            Attributes mainAttributes = jar.getManifest().getMainAttributes();
            String name = mainAttributes.getValue("Short-Name");
            if (name == null) {
                name = mainAttributes.getValue("Extension-Name");
            }
            String version = mainAttributes.getValue("Plugin-Version");
            if (version == null) {
                version = mainAttributes.getValue("Implementation-Version");
            }
            String coreVersion = mainAttributes.getValue("Jenkins-Version");
            if (version == null) {
                version = mainAttributes.getValue("Hudson-Version");
            }
            Plugin plugin = new Plugin(name, version, coreVersion);

            String dependencies = mainAttributes.getValue("Plugin-Dependencies");
            if(dependencies != null && !dependencies.isEmpty()) {
                Flux.fromArray(dependencies.split(","))
                      .map(s -> s.split(";"))
                      .subscribe(strings -> {
                          String[] split = strings[0].split(":");
                          plugin.dependsOn(new Plugin(split[0], split[1]), strings.length > 1);
                      });
            }
            return Mono.just(plugin);
        } catch(IOException ex) {
            return Mono.error(ex);
        }
    }

    @Transactional
    public void updatePluginTier(String pluginName, Tier tier) {
        pluginRepository.setTierForPlugin(pluginName, tier.name());
    }

    @Transactional(readOnly = true)
    public Page<PluginNameAndTier> getPluginsWithLimit(Pageable page) {
        return pluginRepository.getPluginsWithTier(page);
    }

    @Transactional(readOnly = true)
    public List<VersionNumber> getPluginVersions(String name) {
        return pluginRepository.getVersionsOfPlugin(name).stream().sorted().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Plugin> getPlugin(String name, VersionNumber version) {
        return Optional.ofNullable(pluginRepository.findByNameAndVersion(name, version));
    }

    @Transactional(readOnly = true)
    public long countUniquePlugins() {
        return pluginRepository.countUniquePlugins();
    }
}
