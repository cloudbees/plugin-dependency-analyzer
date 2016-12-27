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

package io.jenkins.support.plugins.injector.service;

import io.jenkins.support.plugins.injector.model.Plugin;
import io.jenkins.support.plugins.injector.repository.PluginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author Adrien Lecharpentier
 */
@Service
public class PluginService {
    private static final Logger LOG = LoggerFactory.getLogger(PluginService.class);
    private final PluginRepository repository;

    @Autowired
    public PluginService(PluginRepository repository) {
        this.repository = repository;
    }

    public long countUniquePlugins() {
        return this.repository.count();
    }

    public long countAllPlugins() {
        return this.repository.countAllPlugins();
    }

    public long countAllDependencies() {
        return this.repository.countAllDependencies();
    }

    public void from(MultipartFile file) throws IOException {
        Path pluginFile = Files.createTempFile(UUID.randomUUID().toString(), "-" + file.getOriginalFilename());
        LOG.debug("Copying {} to {}", file.getOriginalFilename(), pluginFile.toAbsolutePath());
        try {
            file.transferTo(pluginFile.toFile());
            try (JarFile pluginBinary = new JarFile(pluginFile.toFile())) {
                Manifest pluginManifest = pluginBinary.getManifest();
                Plugin plugin = PluginConverter.fromManifest(pluginManifest);
                Plugin known = repository.findByNameAndVersion(plugin.getName(), plugin.getVersion());
                if (known != null) {
                    if (known.getRequiredCoreVersion() != null && !known.getRequiredCoreVersion().isEmpty()) {
                        LOG.info("Nothing to do with {}:{}", plugin.getName(), plugin.getVersion());
                        return;
                    } else {
                        plugin.setId(known.getId());
                    }
                }

                plugin.getDependencies()
                    .forEach(link -> {
                        Plugin dep = link.getDependency();
                        Plugin byNameAndVersion = repository.findByNameAndVersion(dep.getName(), dep.getVersion());
                        if (byNameAndVersion != null) {
                            link.setDependency(byNameAndVersion);
                        } else {
                            this.merge(dep);
                        }
                    });
                this.merge(plugin);
            }
        } finally {
            Files.delete(pluginFile);
        }
    }

    private Plugin merge(Plugin plugin) {
        LOG.info("Saving {}:{}", plugin.getName(), plugin.getVersion());
        return repository.save(plugin);
    }
}
