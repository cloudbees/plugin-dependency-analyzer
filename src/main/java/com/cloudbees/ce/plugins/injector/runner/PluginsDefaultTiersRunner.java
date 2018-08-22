package com.cloudbees.ce.plugins.injector.runner;

import com.cloudbees.ce.plugins.injector.model.Tier;
import com.cloudbees.ce.plugins.injector.service.PluginService;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.*;
import java.util.Collections;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@Order(value = 1000)
public class PluginsDefaultTiersRunner implements CommandLineRunner {
    private static final Logger LOG = getLogger(PluginsDefaultTiersRunner.class);
    private final PluginService pluginService;

    public PluginsDefaultTiersRunner(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void run(String... args) throws Exception {
        String tierLocation = "/plugins-category";
        URI uri = new ClassPathResource(tierLocation).getURI();
        Path path;

        if ("jar".equals(uri.getScheme())) {
            FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
            path = fs.getPath("/BOOT-INF/classes" + tierLocation + "/tiers.csv");
        } else {
            path = Paths.get("/plugins-category/tiers.csv");
        }

        Files.readAllLines(path)
              .stream()
              .map(s -> s.split(","))
              .forEach(pair -> {
                  LOG.debug("Update plugin {} tier to {}", pair[0], pair[1]);
                  try {
                      pluginService.updatePluginTier(pair[0], Tier.valueOf(pair[1]));
                  } catch (IllegalArgumentException ex) {
                      LOG.error("Error understanding tier for ", pair[0], ex);
                  }
              });
    }
}
