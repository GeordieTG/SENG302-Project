package nz.ac.canterbury.seng302.tab.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to add static resource paths to SpringBoot.
 */
@Configuration
public class AdditionalResourceWebConfig implements WebMvcConfigurer {
    /**
     * Registers the public directory in src/main as a static resource.
     * This directory becomes available even when packaged in jar.
     *
     * @param registry resource handler
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/**")
            .addResourceLocations("file:" + System.getProperty("user.dir")
                + "/public/");
    }

}
