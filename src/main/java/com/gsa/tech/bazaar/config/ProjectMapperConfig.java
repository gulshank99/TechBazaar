package com.gsa.tech.bazaar.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectMapperConfig {

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }

}
