package ru.itis.master.party.dormdeals.search.links;

import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.search.SearchLink;
import ru.itis.master.party.dormdeals.search.TypeMatchingAbstractSearch;
import ru.itis.master.party.dormdeals.dto.CatalogueElastic;
import ru.itis.master.party.dormdeals.models.ItemElastic;
import ru.itis.master.party.dormdeals.repositories.ItemElasticRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.itis.master.party.dormdeals.utils.SearchUtils.*;
import static ru.itis.master.party.dormdeals.utils.StringUtils.parseAndAssertNeedConvert;

@Order(15)
@Component
public class CatalogueMatchingSearch extends TypeMatchingAbstractSearch implements SearchLink<CatalogueElastic> {
    public CatalogueMatchingSearch(ItemElasticRepository repository) {
        super(repository);
    }

    @Override
    public List<CatalogueElastic> findAll(String text, Pageable pageable) {
        List<String> words = new ArrayList<>();
        boolean needConvert = parseAndAssertNeedConvert(text, words);
        Long catalogueId = tryFindCatalogueId(words, needConvert);
        if (catalogueId == null)
            return List.of();

        String type = tryFindType(words, needConvert);

        List<ItemElastic> list = findByCriteria(type, catalogueId, pageable);

        var result = findExactMatching(list, List.of(text.split(" ")), "");
        return result.orElseGet(() -> groupByCatalogue(list, ""));
    }

    private Long tryFindCatalogueId(List<String> words, boolean needConvert) {
        for (var iterator = words.iterator(); iterator.hasNext(); ) {
            String word = iterator.next();
            var list = findWithConvert(word, needConvert,
                    t -> itemElasticRepository.findAllByCatalogueFuzzy(t, ONE_ELEMENT));
            if (!list.isEmpty()) {
                iterator.remove();
                return list.get(0).getCatalogueId();
            }
        }
        return null;
    }

    private List<ItemElastic> findByCriteria(String type, Long catalogueId, Pageable pageable) {
        if (type.isEmpty()) {
            return itemElasticRepository.findByCatalogueId(catalogueId, pageable);
        }
        return itemElasticRepository.findByCatalogueIdAndType(catalogueId, type, pageable);
    }
}
