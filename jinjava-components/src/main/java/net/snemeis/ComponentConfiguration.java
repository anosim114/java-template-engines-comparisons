package net.snemeis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComponentConfiguration {

    @Bean(name="configure components, don't know what to do here with the @bean stuff")
    public int addComponents(ComponentResourceLocator componentResourceLocator) {
        System.out.println("adding new components now");
        // language=html
        final String alertComponent = """
          {% macro alert(severity) %}
              <alert class="{{ severity }}"><strong>{{ severity }}</strong> {{ caller() }}</alert>
          {% endmacro %}
        """;

        componentResourceLocator.addComponent("cr-icon", "🙂");
        componentResourceLocator.addComponent("cr-alert", alertComponent);

        return 2;
    }
}
