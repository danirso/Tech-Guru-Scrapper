package com.gadgetguru.scrapper;

import com.gadgetguru.scrapper.controllers.Product;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class Crawl {

    public Product crawlForProduct(String searchTerm) {
        String processedSearchTerm = searchTerm.replaceAll("\\s", "-").replaceAll("[^a-zA-Z0-9-]", "");
        String url = "https://www.kabum.com.br/busca/" + processedSearchTerm;
        return crawl(1, url, new ArrayList<>());
    }

    private static Product crawl(int level, String url, ArrayList<String> visited) {
        if (level <= 5) {
            Document doc = request(url, visited);
            if (doc != null) {
                Elements productLinks = doc.select(".productCard > a[href]");
                for (Element productLink : productLinks) {
                    String productUrl = productLink.absUrl("href");
                    if (!visited.contains(productUrl)) {
                        return crawl(level + 1, productUrl, visited);
                    }
                }
    
                Elements priceElements = doc.select(".finalPrice");
                if (!priceElements.isEmpty()) {
                    String price = priceElements.text();

                    Element productNameElement = doc.selectFirst(".sc-89bddf0f-6.dVrDvy");
                    String productName = productNameElement != null ? productNameElement.text() : "Nome do Produto Não Encontrado";

                    Element productImageElement = doc.selectFirst(".sc-50113dda-3 img.iiz__img[src]");
                    String productImageUrl = productImageElement != null ? productImageElement.absUrl("src") : "URL da Imagem Não Encontrada";

                    // Criando um objeto ProductData e retornando os dados do produto
                    Product productData = new Product();
                    productData.setName(productName);
                    productData.setLink(url);
                    productData.setPrice(price);
                    productData.setImageUrl(productImageUrl);
                    System.out.println("Nome do Produto: " + productName);
                    System.out.println("Link do Produto: " + url);
                    System.out.println("Preço do Produto: " + price);
                    System.out.println("URL da Imagem: " + productImageUrl);
                    return productData;
                } else {
                    System.out.println("Elemento com classe 'finalPrice' não encontrado no link: " + url);
                }
            }
        }
        return null;
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
