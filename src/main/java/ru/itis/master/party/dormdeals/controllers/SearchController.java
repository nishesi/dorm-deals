package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.SearchApi;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.services.SearchService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class SearchController implements SearchApi {
    //TODO: сделать 404 нормальную ошибку отлавливать если на странице нет товара вообще, переделать List<> на Page<>
    private final SearchService searchService;
    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public ResponseEntity<List<ProductDto>> searchProducts(MultiValueMap<String, String> criteria, Integer pageIndex) {
        List<String> nameQueries = extractCriteriaValues(criteria, "name-query");
        List<String> categories = extractCriteriaValues(criteria, "category");
        List<Long> shopIds = extractCriteriaValues(criteria, "shop-id")
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return ResponseEntity.ok(searchService.getProductSearch(nameQueries, categories, shopIds,
                PageRequest.of(pageIndex, defaultPageSize)));

    }

    private List<String> extractCriteriaValues(MultiValueMap<String, String> criteria, String key) {
        return criteria.getOrDefault(key, List.of())
                .stream()
                .flatMap(value -> Stream.of(value.split(",")))
                .collect(Collectors.toList());
    }
}
