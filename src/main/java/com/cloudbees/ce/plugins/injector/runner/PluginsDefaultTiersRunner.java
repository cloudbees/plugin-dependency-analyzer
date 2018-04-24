package com.cloudbees.ce.plugins.injector.runner;

import com.cloudbees.ce.plugins.injector.model.Tier;
import com.cloudbees.ce.plugins.injector.service.PluginService;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@Profile("dev")
public class PluginsDefaultTiersRunner implements CommandLineRunner {
    private static final Logger LOG = getLogger(PluginsDefaultTiersRunner.class);
    private final PluginService pluginService;

    public PluginsDefaultTiersRunner(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void run(String... args) throws Exception {
        Files.readAllLines(Paths.get("src/main/resources/plugins-category/tiers.csv"))
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
