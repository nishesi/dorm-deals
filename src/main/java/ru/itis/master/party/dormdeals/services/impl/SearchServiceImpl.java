package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.master.party.dormdeals.dto.converters.ProductConverter;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.repositories.SearchRepository;
import ru.itis.master.party.dormdeals.services.SearchService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SearchRepository searchRepository;
    private final ProductConverter productConverter;
    @Override
    @Transactional
    public List<ProductDto> getProductSearch(List<String> namesQuery,
                                             List<String> categories,
                                             List<Long> shopIdn,
                                             Pageable pageable) {
        return productConverter
                .from(searchRepository.productSearch(namesQuery,
                        categories, shopIdn, pageable));
    }
}
