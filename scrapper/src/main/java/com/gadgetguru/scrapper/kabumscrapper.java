package com.gadgetguru.scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.gadgetguru.scrapper.controllers.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class kabumscrapper {

    public List<Product> scrapeProductsByName(String searchKeyword) {
        List<Product> products = new ArrayList<>();

        try {
            Document document = Jsoup.connect("https://www.kabum.com.br/").get();

            Elements productElements = document.select("seletor-dos-elementos-de-produtos");

            for (Element productElement : productElements) {
                String name = productElement.select("seletor-do-nome-do-produto").text();
                String price = productElement.select("seletor-do-preco-do-produto").text();

                if (name.toLowerCase().contains(searchKeyword.toLowerCase())) {
                    Product product = new Product(name, price);
                    products.add(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }
    
}