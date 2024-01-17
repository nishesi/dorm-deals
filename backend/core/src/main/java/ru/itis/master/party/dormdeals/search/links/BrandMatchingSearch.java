package ru.itis.master.party.dormdeals.search.links;

import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.CatalogueElastic;
import ru.itis.master.party.dormdeals.models.elasticsearch.ItemElastic;
import ru.itis.master.party.dormdeals.repositories.ItemElasticRepository;
import ru.itis.master.party.dormdeals.search.SearchLink;
import ru.itis.master.party.dormdeals.search.TypeMatchingAbstractSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.itis.master.party.dormdeals.utils.SearchUtils.*;
import static ru.itis.master.party.dormdeals.utils.StringUtils.createQuery;
import static ru.itis.master.party.dormdeals.utils.StringUtils.parseAndAssertNeedConvert;

@Order(10)
@Component
public class BrandMatchingSearch extends TypeMatchingAbstractSearch implements SearchLink<CatalogueElastic> {
    public BrandMatchingSearch(ItemElasticRepository repository) {
        super(repository);
    }

    @Override
    public List<CatalogueElastic> findAll(String text, Pageable pageable) {
        List<String> words = new ArrayList<>();
        boolean needConvert = parseAndAssertNeedConvert(text, words);

        String brand = tryFindBrand(words, needConvert);
        if (brand.isEmpty())
            return List.of();

        if (words.isEmpty()) {
            var list = itemElasticRepository.findAllByBrandFuzzy(brand, pageable);
            return groupByCatalogue(list, brand);
        }

        String type = tryFindType(words, needConvert);

        // '_' - prevent fuzzy search for last word
        text = String.join(" ", words) + "_";
        int fuzziness = type.isEmpty() ? 1 : 2;
        List<ItemElastic> list = findWithConvert(text, needConvert, i ->
                itemElasticRepository.findByTextAndOptionalFilterByBrandAndType(i, fuzziness, brand, type, pageable));

        Optional<List<CatalogueElastic>> result = findExactMatching(list, words, brand);
        return result.orElseGet(() -> groupByCatalogue(list, brand));
    }

    private String tryFindBrand(List<String> words, boolean needConvert) {
        for (var iterator = words.iterator(); iterator.hasNext(); ) {
            String word = iterator.next();
            var list = itemElasticRepository.findAllByBrandFuzzy(createQuery(word, needConvert), ONE_ELEMENT);
            if (!list.isEmpty()) {
                iterator.remove();
                return list.get(0).getBrand();
            }
        }
        return "";
    }
}
