package net.snemeis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JinjaConfiguration {

    @Bean
    public JinjavaRenderer jinjavaRenderer() {
        return new JinjavaRenderer();
    }
}
