package com.example.catalogservice.service;

import com.example.catalogservice.entity.Catalog;
import com.example.catalogservice.respository.CatalogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
