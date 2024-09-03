package net.snemeis.filtersjinjava.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SampleComponentConfiguration {

    @Bean
    public ComponentResourceLocator componentResourceLocator() {
        // language=html
        final String alertComponent = """
          {% macro alert(severity) %}
              <alert class="{{ severity }}"><strong>{{ severity }}</strong> {{ caller() }}</alert>
          {% endmacro %}
        """;

        var locator = new ComponentResourceLocator();
        locator.addComponent("cr-alert", alertComponent);
        locator.addComponent("cr-somefile", "<p>this could also have been the content of some file</p>");

        log.info("finished initializing the component loader with {} components", locator.getComponentsCount());

        return locator;
    }
}
