package com.glebzapara.uniportal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.glebzapara.uniportal")
public class SpringConfig {

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
    }
}
