package com.cloudbees.ce.plugins.injector.runner;

import com.cloudbees.ce.plugins.injector.service.PluginService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@Order(value = 100)
public class PluginsImporterRunner implements CommandLineRunner {
    private static final Logger LOG = getLogger(PluginsImporterRunner.class);

    private final Path localRepository;
    private final PluginService pluginService;

    public PluginsImporterRunner(PluginService pluginService) {
        this.pluginService = pluginService;
        this.localRepository = Paths.get(System.getProperty("user.home"),".cache/cloudbees-support/plugins");
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.pluginService.countUniquePlugins() == 0 && Files.exists(localRepository)) {
            LOG.debug("Start crawling local repository: {}", localRepository);
            Files.walkFileTree(localRepository, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if ("hpi".equals(FilenameUtils.getExtension(file.toString()))) {
                        pluginService.fromBinary(file.toFile())
                              .doOnNext(pluginService::save)
                              .doOnError(ex -> LOG.error("Couldn't import a file", ex))
                              .subscribe(plugin -> LOG.info("Imported {}:{}", plugin.getName(), plugin.getVersion()));

                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
            LOG.debug("Finished importing local repository");
        }
    }
}
