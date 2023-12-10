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
public class Crawl3 {

    public Product crawlForProduct(String searchTerm) {
        String processedSearchTerm = searchTerm.replaceAll(" ", "+").replaceAll("[^a-zA-Z0-9-]", "");
        String url = "https://www.gkinfostore.com.br/buscar?q=" + processedSearchTerm;
        return crawl3(1, url, new ArrayList<>());
    }

    private static Product crawl3(int level, String url, ArrayList<String> visited) {
        if (level <= 5) {
            Document doc = request(url, visited);
            if (doc != null) {
                Elements productLinks = doc.select(".listagem-item[data-id]");
                for (Element productLink : productLinks) {
                    String productUrl = productLink.select("a").attr("href");
                    if (!visited.contains(productUrl)) {
                        return crawl3(level + 1, productUrl, visited);
                    }
                }

                Elements priceElements = doc.select(".desconto-a-vista");
                if (!priceElements.isEmpty()) {
                    Element firstPriceElement = priceElements.first();
                    String priceText = firstPriceElement.text();
                    String price = priceText.replaceAll("[^0-9.,]+", "");
    
                    Element productNameElement = doc.selectFirst(".nome-produto.titulo.cor-secundaria");
                    String productName = productNameElement != null ? productNameElement.text() : "Nome do Produto Não Encontrado";
    
                    Element productImageElement = doc.selectFirst("#imagemProduto");
                    String productImageUrl = productImageElement != null ? productImageElement.absUrl("src") : "URL da Imagem Não Encontrada";
    
                    // Criando um objeto ProductData e retornando os dados do produto
                    Product productData3 = new Product();
                    productData3.setName(productName);
                    productData3.setLink(url);
                    productData3.setPrice(price);
                    productData3.setImageUrl(productImageUrl);
                    System.out.println("Nome do Produto: " + productName);
                    System.out.println("Link do Produto: " + url);
                    System.out.println("Preço do Produto: " + price);
                    System.out.println("URL da Imagem: " + productImageUrl);
                    return productData3;
                } else {
                    System.out.println("Elemento com classe 'price-value' não encontrado no link: " + url);
                }
            }
        }
        return null;
    }

    private static Document request(String url, ArrayList<String> v) {
        try {
            if (url != null && !url.isEmpty()) {
                Document doc = Jsoup.connect(url).get();
                v.add(url);
                return doc;
            } else {
                System.out.println("URL inválida: " + url);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Erro de IO ao acessar o link: " + url);
            return null;
        }
    }
}
