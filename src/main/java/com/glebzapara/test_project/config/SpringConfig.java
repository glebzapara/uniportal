package com.glebzapara.test_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.glebzapara.test_project")
@EnableWebMvc
public class SpringConfig {

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
    }
}
