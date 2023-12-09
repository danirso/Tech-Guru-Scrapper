package com.gadgetguru.scrapper;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class Crawl2 {

    private String searchTerm;

    public void crawlForProduct(String searchTerm) {
        this.searchTerm = searchTerm.replaceAll("-", "+").replaceAll("[^a-zA-Z0-9-]", "");
        String url = "https://www.magazineluiza.com.br/busca/" + this.searchTerm;
        crawl2(1, url, new ArrayList<>());
    }

    private static void crawl2(int level, String url, ArrayList<String> visited) {
        if (level <= 5) {
            Document doc = request(url, visited);
            if (doc != null) {
                Elements productLinks = doc.select(".sc-APcvf.ejDyHN > a[href]");
                for (Element productLink : productLinks) {
                    String productUrl = productLink.absUrl("href");
                    if (!visited.contains(productUrl)) {
                        crawl2(level + 1, productUrl, visited);
                        return;  
                    }
                }
    
                Elements priceElements = doc.select("[data-testid=price-value]");
                if (!priceElements.isEmpty()) {
                    String price = priceElements.text().trim();
    
                    Element productNameElement = doc.selectFirst(".sc-kpDqfm.gXZPqL");
                    String productName = productNameElement != null ? productNameElement.text() : "Nome do Produto Não Encontrado";
    
                    Element productImageElement = doc.selectFirst(".sc-cWSHoV.jnuWYf");
                    String productImageUrl = productImageElement != null ? productImageElement.absUrl("src") : "URL da Imagem Não Encontrada";
    
                    System.out.println("Nome do Produto: " + productName);
                    System.out.println("Link do Produto: " + url);
                    System.out.println("Preço do Produto: " + price);
                    System.out.println("URL da Imagem: " + productImageUrl);
                    return;  
                } else {
                    System.out.println("Elemento com classe 'price-value' não encontrado no link: " + url);
                }
            }
        }
    }

    private static Document request(String url, ArrayList<String> v) {
        try {
            Document doc = Jsoup.connect(url).get();
            v.add(url);
            return doc;
        } catch (IOException e) {
            System.out.println("Erro de IO ao acessar o link: " + url);
            return null;
        }
    }
}