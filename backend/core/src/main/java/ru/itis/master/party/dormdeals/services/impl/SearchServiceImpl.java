package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.master.party.dormdeals.mapper.ProductMapper;
import ru.itis.master.party.dormdeals.search.SearchChain;
import ru.itis.master.party.dormdeals.dto.CatalogueElastic;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.repositories.impl.SearchRepository;
import ru.itis.master.party.dormdeals.services.SearchService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private static final Pageable PAGE_10 = PageRequest.of(0, 10);
    private final SearchRepository searchRepository;
    private final ProductMapper productMapper;
    private final SearchChain searchChain;

    @Override
    @Transactional
    public List<ProductDto> getProductSearch(List<String> namesQuery,
                                             List<String> categories,
                                             List<Long> shopIdn,
                                             Pageable pageable) {
        return productMapper
                .toProductDtoList(searchRepository.productSearch(namesQuery,
                        categories, shopIdn, pageable));
    }

    @Override
    public List<CatalogueElastic> searchByText(String text) {
        text = text.trim().replaceAll("\\s+", " ");
        return searchChain.searchByText(text, PAGE_10);
    }
}
