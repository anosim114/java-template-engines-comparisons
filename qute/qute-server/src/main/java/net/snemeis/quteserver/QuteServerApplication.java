package net.snemeis.quteserver;

import io.quarkus.qute.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class QuteServerApplication {

    @Autowired
    Engine quteEngine;

    public static void main(String[] args) {
        SpringApplication.run(QuteServerApplication.class, args);
    }

    @GetMapping("/")
    @ResponseBody
    String index(Model model) {
        model.addAttribute("name", "World");

        // TODO: for dev mode, do better
        quteEngine.clearTemplates();

        return quteEngine.getTemplate("index").render(model.asMap());
    }
}
