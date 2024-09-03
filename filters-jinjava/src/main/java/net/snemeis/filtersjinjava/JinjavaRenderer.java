package net.snemeis.filtersjinjava;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import com.hubspot.jinjava.loader.CascadingResourceLocator;
import com.hubspot.jinjava.loader.ClasspathResourceLocator;
import lombok.extern.slf4j.Slf4j;
import net.snemeis.filtersjinjava.components.ComponentResourceLocator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import java.util.*;

@Slf4j
@Component
@AutoConfigureAfter({ ComponentResourceLocator.class })
public class JinjavaRenderer {

    Jinjava jj = new Jinjava();

    private JinjavaRenderer(ComponentResourceLocator componentLocator) {

        // set resource locator to use class as well as look for components
        jj.setResourceLocator(
                new CascadingResourceLocator(
                        componentLocator,
                        new ClasspathResourceLocator()
                )
        );

        // register i18n function
        jj.getGlobalContext().registerFunction(
            new ELFunctionDefinition(
                    "",
                    "i18n",
                    I18nResolver.class,
                    "translate",
                    Object.class,
                    String[].class
            )
        );

        log.info("finished setting up jinjava");
    }

    // render a template with the filename `templateName` with the given `map`
    String render(String templateName, Map<String, Object> map) {
        log.info("going to pull template: {}", templateName);

        String template;

        try {
            template = Resources.toString(Resources.getResource("templates/" + templateName + ".jinja"), Charsets.UTF_8);
        } catch(Exception e) {
            return "ERROR";
        }

        return jj.render(template, map);
    }

    @Component
    public static class I18nResolver {
        private static MessageSource messages;

        I18nResolver(MessageSource source) {
            messages = source;
        }

        public static String translate(Object message, String... fillers) {
            return messages.getMessage("filters.greetings", null, "no", Locale.CANADA);
        }
    }
}
