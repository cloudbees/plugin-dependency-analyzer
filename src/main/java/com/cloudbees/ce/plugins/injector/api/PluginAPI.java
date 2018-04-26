package com.cloudbees.ce.plugins.injector.api;

import com.cloudbees.ce.plugins.injector.model.Plugin;
import com.cloudbees.ce.plugins.injector.service.PluginService;
import org.slf4j.Logger;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Adrien Lecharpentier
 */
@RestController
@RequestMapping(value = "/api/plugins")
public class PluginAPI {
    private static final Logger LOG = getLogger(PluginAPI.class);
    private final PluginService pluginService;

    public PluginAPI(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @PostMapping(path = "/import")
    public Mono<Plugin> importAPlugin(@RequestPart(name = "plugin") FilePart pluginPart) throws IOException {
        Path pluginBinary = Files.createTempFile("plugin-", pluginPart.filename());
        LOG.debug("Temp file location: {}", pluginBinary.toAbsolutePath());
        return pluginPart
              .transferTo(pluginBinary.toFile())
              .then(Mono.just(pluginBinary.toFile()))
              .doOnNext(File::deleteOnExit)
              .flatMap(pluginService::fromBinary)
              .doOnNext(pluginService::save)
              .doOnError(throwable -> LOG.error("Couldn't process {}", pluginPart.filename(), throwable));
    }
}
