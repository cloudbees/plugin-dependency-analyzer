package com.cloudbees.ce.plugins.injector.runner;

import com.cloudbees.ce.plugins.injector.config.TiersMapping;
import com.cloudbees.ce.plugins.injector.model.Tier;
import com.cloudbees.ce.plugins.injector.service.PluginService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hudson.util.VersionNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class PluginTierUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginTierUpdater.class);
    private final PluginService pluginService;
    private final String pluginsAPIURI = "https://plugin-site-api.cloudbees.com";

    public PluginTierUpdater(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Scheduled(
          fixedDelay = 1000 * 60 * 60 * 12, // 12hrs between each update
          initialDelay = 1000
    )
    public void update() {
        WebClient.create(pluginsAPIURI)
              .get()
              .uri("/plugins?limit={limit}",
                    WebClient.create(pluginsAPIURI).get().uri("/plugins?limit=1")
                          .retrieve()
                          .bodyToMono(PluginResponse.class)
                          .map(pluginResponse -> pluginResponse.total)
                          .blockOptional()
                          .orElse(50)
              )
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .flatMap(resp -> resp.bodyToMono(PluginResponse.class))
              .doOnError(ex -> LOGGER.error("Error getting plugins details", ex))
              .flatMapIterable(pluginResponse -> pluginResponse.plugins)
              .filter(p -> p.tier != null)
              .subscribe(plugin -> {
                  LOGGER.debug("Updating tier of plugin {} to `{}`", plugin.name, plugin.tier);
                  pluginService.updatePluginTier(plugin.name, plugin.tier);
              });
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class PluginResponse {
        @JsonProperty private List<Plugin> plugins;
        @JsonProperty private int page;
        @JsonProperty private int pages;
        @JsonProperty private int total;
        @JsonProperty private int limit;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Plugin {
        @JsonProperty private String name;
        @JsonProperty private VersionNumber version;
        @JsonDeserialize(using = TiersMapping.TiersDeserializer.class)
        @JsonProperty private Tier tier = Tier.none;
    }
}
