package ru.itis.master.party.dormdeals.repositories;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.models.ItemElastic;

import java.util.List;

import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.And;
import static org.springframework.data.elasticsearch.client.elc.Queries.matchQuery;

@Component
@RequiredArgsConstructor
public class CustomItemElasticRepositoryImpl implements CustomItemElasticRepository {
    private final SearchOperations template;

    @Override
    public List<ItemElastic> findByTextAndOptionalFilterByBrandAndType(
            String text, int textFuzziness, String brand, String type, Pageable pageable
    ) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        if (!text.isEmpty()) {
            boolQuery.must(new MultiMatchQuery.Builder()
                    .query(text)
                    .fields("name^4", "description")
                    .fuzziness(String.valueOf(textFuzziness))
                    .analyzer("russian")
                    .operator(And)
                    .build()._toQuery());
        }

        if (!brand.isEmpty())
            boolQuery.filter(matchQuery("brand", brand, And, 1.0F)._toQuery());

        if (!type.isEmpty())
            boolQuery.filter(matchQuery("type", type, And, 1.0F)._toQuery());

        return performQuery(pageable, new Query(boolQuery.build()));
    }

    private List<ItemElastic> performQuery(Pageable pageable, Query boolQuery) {
        NativeQuery query = new NativeQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(pageable)
                .build();

        SearchHits<ItemElastic> result = template.search(query, ItemElastic.class);
        return result.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();
    }
}
