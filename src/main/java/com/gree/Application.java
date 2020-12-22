package com.gree;


import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

import javax.inject.Inject;

@OpenAPIDefinition(
        info = @Info(
                title = "接口文档",
                version = "1.0",
                description = "接口文档",
                contact = @Contact(url = "http://cooker.github.com")
        )
)
/**
 * @author cici
 */
public class Application {

    public static void main(String[] args) throws Exception {

        Micronaut.run(Application.class, args);


    }
}
