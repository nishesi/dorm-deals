package ru.itis.master.party.dormdeals.services;

import org.springframework.data.domain.Pageable;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;

import java.util.List;

public interface SearchService {
    List<ProductDto> getProductSearch(List<String> namesQuery, List<String> categories,
                                      List<Long> shopIdn, Pageable pageable);
}
