package com.cloudbees.ce.plugins.injector.controller;

import com.cloudbees.ce.plugins.injector.model.Plugin;
import com.cloudbees.ce.plugins.injector.service.PluginService;
import hudson.util.VersionNumber;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class HomeController {
    private final PluginService pluginService;

    public HomeController(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @GetMapping(path = "/")
    public String home() {
        return "redirect:/plugins";
    }

    @GetMapping(path = {"/plugins", "/plugins/{page}"})
    public String home(final Model model, @PathVariable(name = "page", required = false) Integer page) {
        Pageable pageable = PageRequest.of((page == null ? 1 : page) - 1, 15, Sort.Direction.ASC, "name");
        model.addAttribute("plugins", pluginService.getPluginsWithLimit(pageable));
        return "plugin-listing";
    }

    @GetMapping(path = "/plugin/{name}")
    public String pluginDetails(final Model model, @PathVariable(name = "name") String name) {
        model.addAttribute("name", name);
        model.addAttribute("versions", pluginService.getPluginVersions(name));
        return "plugin-versions-listing";
    }

    @GetMapping(path = "/plugin/{name}/{version}")
    public String pluginVersionDetails(final Model model,
                                       @PathVariable(name = "name") String name,
                                       @PathVariable(name = "version") VersionNumber version) {
        Optional<Plugin> optionalPlugin = pluginService.getPlugin(name, version);
        return optionalPlugin
              .map(pl -> {
                  model.addAttribute("plugin", pl);
                  return "plugin-details";
              })
              .orElseGet(() -> "redirect:/plugin/" + name);
    }
}
