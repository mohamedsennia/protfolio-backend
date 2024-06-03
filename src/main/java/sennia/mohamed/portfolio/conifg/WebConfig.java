package sennia.mohamed.portfolio.conifg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.dir}")
    private String uploadDir;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200","https://senniamohamed.netlify.app/")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);

    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String link= new File(uploadDir).getAbsolutePath();
        link=link.replace('\\','/')+'/';

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:"+link);
    }
}
