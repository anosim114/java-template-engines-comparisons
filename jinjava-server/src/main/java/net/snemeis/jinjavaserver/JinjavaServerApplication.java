package net.snemeis.jinjavaserver;

import lombok.extern.slf4j.Slf4j;
import net.snemeis.JinjavaRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@SpringBootApplication
@Controller
public class JinjavaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JinjavaServerApplication.class, args);
    }

    @Autowired
    public JinjavaRenderer renderer;

    @GetMapping("/")
    @ResponseBody
    String index(Model model) {
        model.addAttribute("name", "world");

        return renderer.render("index", model.asMap());
    }
}
