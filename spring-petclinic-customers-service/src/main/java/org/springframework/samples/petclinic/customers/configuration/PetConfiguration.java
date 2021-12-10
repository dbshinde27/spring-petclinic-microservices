package org.springframework.samples.petclinic.customers.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PetConfiguration implements WebMvcConfigurer {

	@Autowired
	PetInterceptor petInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(petInterceptor).addPathPatterns("/**");
	}

}
