package ru.itis.master.party.dormdeals.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.itis.master.party.dormdeals.models.ItemElastic;
import ru.itis.master.party.dormdeals.repositories.ItemElasticRepository;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static ru.itis.master.party.dormdeals.utils.SearchUtils.findWithConvert;

@RequiredArgsConstructor
public abstract class TypeMatchingAbstractSearch {
    protected static final Pageable ONE_ELEMENT = PageRequest.of(0, 1);
    protected final ItemElasticRepository itemElasticRepository;

    protected String tryFindType(List<String> words, boolean needConvert) {
        String type = "";

        List<ItemElastic> local;
        for (Iterator<String> iterator = words.iterator(); iterator.hasNext(); ) {
            String word = iterator.next();
            local = findWithConvert(word, needConvert, t -> itemElasticRepository.findAllByTypeFuzzy(t, ONE_ELEMENT));
            if (!local.isEmpty()) {
                type = local.stream()
                        .map(ItemElastic::getType)
                        .min(Comparator.comparingInt(String::length))
                        .get();
                iterator.remove();
            }
        }
        return type;
    }
}
