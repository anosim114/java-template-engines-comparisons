package net.snemeis.filtersjinjava;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class JinjavaRenderer {
    Jinjava jj;

    private JinjavaRenderer() {
        jj = new Jinjava();

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

    String render(String templateName, Map<String, Object> map) {
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
