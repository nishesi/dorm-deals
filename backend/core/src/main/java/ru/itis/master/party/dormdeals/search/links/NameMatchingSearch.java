package ru.itis.master.party.dormdeals.search.links;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.search.SearchLink;
import ru.itis.master.party.dormdeals.dto.CatalogueElastic;
import ru.itis.master.party.dormdeals.models.elasticsearch.ItemElastic;
import ru.itis.master.party.dormdeals.repositories.ItemElasticRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.itis.master.party.dormdeals.utils.SearchUtils.*;
import static ru.itis.master.party.dormdeals.utils.StringUtils.parseAndAssertNeedConvert;

@Order(25)
@Component
@RequiredArgsConstructor
public class NameMatchingSearch implements SearchLink<CatalogueElastic> {
    private final ItemElasticRepository itemElasticRepository;

    @Override
    public List<CatalogueElastic> findAll(String text, Pageable pageable) {
        List<String> words = new ArrayList<>();
        boolean needConvert = parseAndAssertNeedConvert(text, words);

        // '_' - prevent fuzzy search for last word
        text =  String.join(" ", words) + "_";
        List<ItemElastic> list = findWithConvert(text, needConvert,
                t -> itemElasticRepository.findByNameOrDescription(t, pageable));
        var result = findExactMatching(list, words, "");
        return result.orElseGet(() -> groupByCatalogue(list, ""));
    }
}
