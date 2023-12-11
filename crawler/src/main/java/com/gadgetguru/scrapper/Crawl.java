package com.gadgetguru.scrapper;

import com.gadgetguru.scrapper.controllers.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class Crawl {

    public Product crawlForProduct(String searchTerm) {
        String processedSearchTerm = searchTerm.replaceAll("\\s", "+").replaceAll("[^a-zA-Z0-9+]", "");
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

                    // Pesquisar no Google Imagens usando o nome do produto
                    String googleSearchUrl = "https://www.google.com.br/search?q=" + productName + "&tbm=isch";
                    String googleImageLink = getGoogleImageLink(productName, googleSearchUrl);

                    // Criando um objeto ProductData e retornando os dados do produto
                    Product productData = new Product();
                    productData.setName(productName);
                    productData.setLink(url);
                    productData.setPrice(price);
                    productData.setImageUrl(googleImageLink);

                    System.out.println("Nome do Produto: " + productName);
                    System.out.println("Link do Produto: " + url);
                    System.out.println("Preço do Produto: " + price);
                    System.out.println("URL da Imagem: " + googleImageLink);

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

    private static String getGoogleImageLink(String searchTerm, String googleSearchUrl) {
        try {
            Document doc = Jsoup.connect(googleSearchUrl).get();
            Element firstImageElement = doc.selectFirst("img[data-src]");
    
            if (firstImageElement != null) {
                return firstImageElement.attr("data-src");
            } else {
                System.out.println("Nenhuma imagem encontrada para a pesquisa: " + searchTerm);
            }
        } catch (IOException e) {
            System.out.println("Erro ao realizar a pesquisa no Google Imagens para: " + searchTerm);
        }
        return "Link da imagem no Google não encontrado";
    }
}
