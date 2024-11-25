package com.example.n2.demo;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API N2", version = "v1", description = "Documentação da API N2"))
public class SwaggerConfig {

}
