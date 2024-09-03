package net.snemeis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JinjaConfiguration {

    @Bean
    public ComponentResourceLocator componentResourceLocator() {
        return new ComponentResourceLocator();
    }

    @Bean
    public JinjavaRenderer jinjavaRenderer(ComponentResourceLocator cmpResolver) {
        return new JinjavaRenderer(cmpResolver);
    }
}
