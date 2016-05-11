package com.solidbrain.russads.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by PLADMUC on 2016-05-11.
 */
@Configuration
public class BeanConfig {

	@Bean
	public ConfigurationHelper configurationHelper(){
		return new ConfigurationHelper();
	}
}
