package com.swetonyancelmo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Caminho relativo: o Swagger UI envia as requisições para a mesma
                // origem em que foi carregado (o api-gateway), que então roteia
                // para este serviço. Evita apontar para a porta interna (8081) e CORS.
                .servers(List.of(new Server().url("/")))
                .info(new Info()
                    .title("Catalog Service API")
                    .version("1.0.0")
                    .description("Documentação para API de Catálogo de Produtos"));
    }

}
