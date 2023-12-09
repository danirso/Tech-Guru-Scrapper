package com.gadgetguru.scrapper;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class Crawl {

    private String searchTerm;

    public void crawlForProduct(String searchTerm) {
        this.searchTerm = searchTerm.replaceAll("\\s", "-").replaceAll("[^a-zA-Z0-9-]", "");
        String url = "https://www.kabum.com.br/busca/" + this.searchTerm;
        crawl(1, url, new ArrayList<>());
    }


    private static void crawl(int level, String url, ArrayList<String> visited) {
        if (level <= 5) {
            Document doc = request(url, visited);
            if (doc != null) {
                // Obtém links da classe 'productCard'
                Elements productLinks = doc.select(".productCard > a[href]");
                for (Element productLink : productLinks) {
                    String productUrl = productLink.absUrl("href");
                    if (!visited.contains(productUrl)) {
                        crawl(level + 1, productUrl, visited);
                        return;  // Interrompe a busca após encontrar um link
                    }
                }
    
                Elements priceElements = doc.select(".finalPrice");
                if (!priceElements.isEmpty()) {
                    String price = priceElements.text();
    
                    // Obtém o nome do produto
                    Element productNameElement = doc.selectFirst(".sc-89bddf0f-6.dVrDvy");
                    String productName = productNameElement != null ? productNameElement.text() : "Nome do Produto Não Encontrado";
    
                    // Obtém a URL da imagem do produto
                    Element productImageElement = doc.selectFirst(".iiz__img[src]");
                    String productImageUrl = productImageElement != null ? productImageElement.absUrl("src") : "URL da Imagem Não Encontrada";
    
                    System.out.println("Nome do Produto: " + productName);
                    System.out.println("Link do Produto: " + url);
                    System.out.println("Preço do Produto: " + price);
                    System.out.println("URL da Imagem: " + productImageUrl);
                    return;  // Interrompe a busca após encontrar o preço
                } else {
                    System.out.println("Elemento com classe 'finalPrice' não encontrado no link: " + url);
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