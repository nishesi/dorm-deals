package ru.itis.master.party.dormdeals.search.links;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.search.SearchLink;
import ru.itis.master.party.dormdeals.dto.CatalogueElastic;
import ru.itis.master.party.dormdeals.models.elasticsearch.ItemElastic;
import ru.itis.master.party.dormdeals.repositories.ItemElasticRepository;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static ru.itis.master.party.dormdeals.utils.SearchUtils.groupByCatalogue;

@Order(5)
@Component
@RequiredArgsConstructor
public class NumericNameMatchingSearch implements SearchLink<CatalogueElastic> {
    private final ItemElasticRepository itemElasticRepository;

    @Override
    public List<CatalogueElastic> findAll(String text, Pageable pageable) {
        if (!isNumeric(text))
            return List.of();
        List<ItemElastic> list = itemElasticRepository.findAllByNameContaining(text, pageable);
        return groupByCatalogue(list, "");
    }
}
