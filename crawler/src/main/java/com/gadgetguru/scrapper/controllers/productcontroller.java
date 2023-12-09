package com.gadgetguru.scrapper.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class productcontroller {
    @GetMapping("/{searchTerm}")
    public Product getProductInfo(@PathVariable String searchTerm) {
        // Seu código existente de raspagem
        String url = "https://www.kabum.com.br/busca/" + searchTerm;
        Product product = scrapeProductInfo(url);

        return product;
    }

    private Product scrapeProductInfo(String url) {
        // Lógica de raspagem para obter nome, link, preço e URL da imagem
        // ...

        // Exemplo:
        Product product = new Product();
        product.setName("Nome do Produto");
        product.setLink(url);
        product.setPrice("R$ 100,00");
        product.setImageUrl("https://example.com/image.jpg");

        return product;
    }
}
