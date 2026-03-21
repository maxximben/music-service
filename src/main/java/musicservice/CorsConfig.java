package musicservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                  // все пути
                .allowedOriginPatterns("*")         // абсолютно все источники
                .allowedMethods("*")                // GET, POST, PUT, DELETE, PATCH, OPTIONS...
                .allowedHeaders("*")                // все заголовки
                .allowCredentials(true)             // куки, Authorization и т.д.
                .maxAge(3600);                      // кэшировать preflight на 1 час
    }
}