package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.itis.master.party.dormdeals.models.elasticsearch.ItemElastic;
import ru.itis.master.party.dormdeals.repositories.jpa.CustomItemElasticRepository;

import java.util.List;

public interface ItemElasticRepository extends
        ElasticsearchRepository<ItemElastic, Integer>,
        CustomItemElasticRepository {

    List<ItemElastic> findByCatalogueId(Long catalogueId, Pageable pageable);

    List<ItemElastic> findByCatalogueIdAndType(Long catalogueId, String type, Pageable pageable);

    List<ItemElastic> findAllByNameContaining(String text, Pageable pageable);

    @Query("""
            {
              "match": {
                "brand": {
                  "query": "?0",
                  "fuzziness": "1"
                }
              }
            }""")
    List<ItemElastic> findAllByBrandFuzzy(String text, Pageable pageable);

    @Query("""
            {
              "match": {
                "catalogue": {
                  "query": "?0",
                  "fuzziness": "1"
                }
              }
            }""")
    List<ItemElastic> findAllByCatalogueFuzzy(String text, Pageable pageable);

    @Query("""
            {
              "match": {
                "type": {
                  "query": "?0",
                  "fuzziness": "2"
                }
              }
            }""")
    List<ItemElastic> findAllByTypeFuzzy(String text, Pageable pageable);

    @Query("""
            {
              "multi_match": {
                "fields": [
                  "name^2",
                  "description"
                ],
                "operator": "AND",
                "query": "?0",
                "fuzziness": 1,
                "analyzer": "russian"
              }
            }""")
    List<ItemElastic> findByNameOrDescription(String text, Pageable pageable);

    @Query("""
            {
              "match": {
                "fulltext": {
                  "query": "?0",
                  "fuzziness": "2"
                }
              }
            }""")
    List<ItemElastic> findByFulltext(String text, Pageable pageable);
}