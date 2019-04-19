package eu.lukks.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");

		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");

		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");

		registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");

		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:" + System.getProperty("user.dir") + "/files/").setCachePeriod(0);
	}

}
