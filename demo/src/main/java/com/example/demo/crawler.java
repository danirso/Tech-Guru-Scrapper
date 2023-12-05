package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class crawler {

    public static void main(String[] args) {
        // Iniciar a aplicação Spring Boot
        SpringApplication.run(crawler.class, args);

        // Criar uma instância da classe Crawl
        Crawl.main(args);

        // Se você deseja chamar outro método da classe Crawl, você pode fazer algo como:
        // crawlInstance.outroMetodo();
        
        // Se você deseja acessar variáveis ou métodos estáticos da classe Crawl, use a classe diretamente:
        // Crawl.outroMetodoEstatico();
    }
}
