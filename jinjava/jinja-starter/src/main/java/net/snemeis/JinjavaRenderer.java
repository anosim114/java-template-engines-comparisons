package net.snemeis;

import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import com.hubspot.jinjava.loader.ClasspathResourceLocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class JinjavaRenderer {

    Jinjava jj;

    public JinjavaRenderer() {
        jj = new Jinjava();

        // set resource locator to use class as well as look for components
        jj.setResourceLocator(
            new ClasspathResourceLocator()
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
    public String render(String templateName, Map<String, Object> map) {
        log.info("going to pull template: {}", templateName);

        String template;

        try {
            template = jj.newInterpreter().getResource("templates/" + templateName + ".jinja");
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
