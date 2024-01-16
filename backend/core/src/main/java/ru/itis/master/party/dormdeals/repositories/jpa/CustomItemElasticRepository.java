package ru.itis.master.party.dormdeals.repositories.jpa;

import org.springframework.data.domain.Pageable;
import ru.itis.master.party.dormdeals.models.elasticsearch.ItemElastic;

import java.util.List;

public interface CustomItemElasticRepository {
    List<ItemElastic> findByTextAndOptionalFilterByBrandAndType(
            String text, int textFuzziness, String brand, String type, Pageable pageable);
}
