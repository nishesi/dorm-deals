package ru.itis.master.party.dormdeals.utils;


import ru.itis.master.party.dormdeals.dto.CatalogueElastic;
import ru.itis.master.party.dormdeals.models.ItemElastic;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.itis.master.party.dormdeals.utils.StringUtils.convert;


public class SearchUtils {
    public static List<CatalogueElastic> groupByCatalogue(List<ItemElastic> list, String brand) {
        return list.stream()
                .collect(Collectors.groupingBy(ItemElastic::getCatalogueId))
                .values().stream()
                .map(items -> {
                    var item = items.get(0);
                    return new CatalogueElastic(
                            item.getCatalogue(),
                            item.getCatalogueId(),
                            items,
                            brand);
                })
                .toList();
    }

    public static Optional<List<CatalogueElastic>> findExactMatching(List<ItemElastic> list, List<String> words, String brand) {
        String text = String.join(" ", words);
        return list.stream()
                .filter(item -> Objects.equals(text, item.getName()) ||
                        text.startsWith(item.getType()) && text.endsWith(item.getName()))
                .findFirst()
                .map(item -> List.of(
                        new CatalogueElastic(
                                item.getCatalogue(),
                                item.getCatalogueId(),
                                List.of(item),
                                brand)));
    }

    public static <T> List<T> findWithConvert(String text, boolean needConvert, Function<String, List<T>> function) {
        List<T> list = function.apply(text);
        if (list.isEmpty() && needConvert)
            return function.apply(convert(text));
        return list;
    }
}
