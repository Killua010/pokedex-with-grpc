package br.com.grpcpokemon.server.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
    
	@Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        final Duration readTimeout = Duration.ofMillis(3000);
        final Duration connectTimeout = Duration.ofMillis(30000);
        return builder.setConnectTimeout(connectTimeout).setReadTimeout(readTimeout).build();
        
    }
}
