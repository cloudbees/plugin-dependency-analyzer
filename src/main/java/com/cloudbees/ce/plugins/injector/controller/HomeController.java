package com.cloudbees.ce.plugins.injector.controller;

import com.cloudbees.ce.plugins.injector.service.PluginService;
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
        return "redirect:plugins";
    }

    @GetMapping(path = {"/plugins", "/plugins/{page}"})
    public String home(final Model model, @PathVariable(name = "page", required = false) Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.map(p -> p - 1).orElse(0), 20, Sort.Direction.ASC, "name");
        model.addAttribute("plugins", pluginService.getPluginsWithLimit(pageable));
        return "home";
    }
}
