import io.quarkus.qute.Engine;
import net.snemeis.I18nNamespaceResolver;
import net.snemeis.QuteConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes={
        QuteConfiguration.class,
        BeanInjectorTest.MessagesConfiguration.class,
        I18nNamespaceResolver.class
})
public class BeanInjectorTest {

    @Autowired
    Engine qe;

    @Autowired
    MessageSource messageSource;

    @Test
    void basic_output() {
        var out = qe.parse("hello from qute").render();

        assertEquals("hello from qute", out);
    }

    @Test
    void variabes_render() {
        var out = qe.parse("hello {name}!").data("name", "world").render();

        assertEquals("hello world!", out);
    }

    @Test
    void messageSource_retrieves_messages() {
        var out = messageSource.getMessage("test.one", null, Locale.ENGLISH);

        assertEquals("this is the message one", out);
    }

    @Test
    void qute_translates_well_default() {
        var out = qe.parse("{i18n:t('test.one')}").render();

        assertEquals("this is the message one", out);
    }

    @Test
    void qute_translates_well_inlineVariable() {
        var out = qe.parse("{i18n:t('test.vars', 'power rangers reject')}").render();

        assertEquals("this is with variables...power rangers reject", out);
    }

    @Test
    void qute_translates_well_dataVariable() {
        var out = qe.parse("{i18n:t('test.vars', powerRangerOne)}").data("powerRangerOne", "Red Ranger").render();

        assertEquals("this is with variables...Red Ranger", out);
    }

    @Test
    void qute_translates_well_dataVariableAndInlineVariable() {
        var out = qe.parse("{i18n:t('test.vars2', ranger, 'is the best')}").data("ranger", "Red Ranger").render();

        assertEquals("Red Ranger is the best", out);
    }

    @Test
    void qute_translates_well_nestedVariable() {
        Optional<String> var = Optional.of("Blue Ranger");
        var out = qe.parse("{i18n:t('test.vars', ranger.get())}").data("ranger", var).render();

        assertEquals("this is with variables...Blue Ranger", out);
    }

    @TestConfiguration
    static public class MessagesConfiguration {

        @Bean
        public ResourceBundleMessageSource messageSource() {

            var source = new ResourceBundleMessageSource();
            source.setBasenames("messages/messages");
            source.setUseCodeAsDefaultMessage(true);

            return source;
        }
    }
}
