package net.snemeis.filtersjinjava;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@AllArgsConstructor
public class FilterController {

    private final FilterService filterService;
    JinjavaRenderer jj;
    FilterService fs;

    @GetMapping("/")
    @ResponseBody
    String index(Model model) {
        log.info("getting the index page");
        model.addAttribute("name", "World!");
        model.addAttribute("availableFilters", filterService.getSampleFilters());

        return jj.render("index", model.asMap());
    }

    @ModelAttribute("appliedFilters")
    public FilterService.Filters getAppliedFilters(FilterService.Filters filters) {
        return filters;
    }

    @GetMapping("/filter")
    @ResponseBody
    String filter(
            Model model,
            @ModelAttribute("appliedFilters") FilterService.Filters appliedFilters,
            HttpServletResponse res
    ) {
        log.info("doing specific filtering");

        var filters = filterService.consolidateFilterTypes(
                filterService.getSampleFilters(),
                appliedFilters
        );

        model.addAttribute("availableFilters", filters);

        res.setContentType("text/html");
        return jj.render("filters/filterbar", model.asMap());
    }
}
