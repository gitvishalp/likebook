package com.likebook.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfiguration implements OperationCustomizer {

	@Value("${swagger.local-url}")
	private String Url;
	@Value("${server.servlet.context-path}")
	private String contextPath;
	@Bean
	  public OpenAPI myOpenAPI() {
		
	    Server devServer = new Server();
	    devServer.url(Url.concat(contextPath));
	    devServer.setDescription("Server URL in Development environment");
        
	    Contact contact = new Contact();
	    contact.setEmail("vpblaze643@gmail.com");
	    contact.setName("LikeBook");	    
	    contact.setUrl("http://localhost:8081");
  
	    List<Server> servers = new ArrayList<>(1);
	    servers.add(devServer);

	    Info info = new Info()
	        .title("LikeBook APIs")
	        .version("1.0")
	        .contact(contact)
	        .description("This is LikeBook APIs").termsOfService("http://localhost:8081");

	    return new OpenAPI().info(info).servers(servers).components(new Components().addSecuritySchemes("bearer-key",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).name("bearerAuth").scheme("bearer").bearerFormat("JWT").in(SecurityScheme.In.HEADER).name("Authorization")))
            .addSecurityItem(
                new SecurityRequirement().addList("bearer-key", Arrays.asList("read", "write")));
	  }

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		// TODO Auto-generated method stub
		return operation;
	}
}
