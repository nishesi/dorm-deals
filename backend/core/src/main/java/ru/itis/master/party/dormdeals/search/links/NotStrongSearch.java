package ru.itis.master.party.dormdeals.search.links;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.CatalogueElastic;
import ru.itis.master.party.dormdeals.repositories.ItemElasticRepository;
import ru.itis.master.party.dormdeals.search.SearchLink;

import java.util.List;

import static ru.itis.master.party.dormdeals.utils.SearchUtils.findWithConvert;
import static ru.itis.master.party.dormdeals.utils.SearchUtils.groupByCatalogue;

@Component
@RequiredArgsConstructor
public class NotStrongSearch implements SearchLink<CatalogueElastic> {
    private final ItemElasticRepository itemElasticRepository;
    @Override
    public List<CatalogueElastic> findAll(String text, Pageable pageable) {
        text += "_";
        var list = findWithConvert(text, true,
                t -> itemElasticRepository.findByFulltext(t, pageable));
        return groupByCatalogue(list, "");
    }
}
