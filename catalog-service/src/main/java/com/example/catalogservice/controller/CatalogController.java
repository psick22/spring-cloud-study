package com.example.catalogservice.controller;

import static com.example.catalogservice.common.MapUtils.mapList;

import com.example.catalogservice.common.MapUtils;
import com.example.catalogservice.entity.Catalog;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
public class CatalogController {

    private final Environment env;
    private final ModelMapper mapper;
    private final CatalogService catalogService;

    @GetMapping("/health-check")
    public String status() {
        return "Catalog Service is now working on PORT " + env.getProperty("local.server.port");
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> findAll() {

        List<Catalog> catalogs = catalogService.getAllCatalogs();
        List<ResponseCatalog> results = mapList(catalogs, ResponseCatalog.class);

        return ResponseEntity.status(HttpStatus.OK).body(results);

    }


}
