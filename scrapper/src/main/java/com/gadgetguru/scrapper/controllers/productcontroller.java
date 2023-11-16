package com.gadgetguru.scrapper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.gadgetguru.scrapper.amazonscrapper;

@RestController
public class productcontroller {

    @Autowired
    private amazonscrapper scrapingService;

    @GetMapping("/products")
    public List<Product> getProductsByName(@RequestParam String keyword) {
        return scrapingService.scrapeProductsByName(keyword);
    }
}
