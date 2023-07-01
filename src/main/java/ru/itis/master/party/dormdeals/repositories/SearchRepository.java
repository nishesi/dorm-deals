package ru.itis.master.party.dormdeals.repositories;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.itis.master.party.dormdeals.models.Product;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public List<Product> productSearch(List<String> namesQuery, List<String> categories, List<Long> shopIdn, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = cb.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (!namesQuery.isEmpty()) {
            List<Predicate> namePredicates = namesQuery.stream()
                    .map(query -> cb.or(
                            cb.like(root.get("name"), "%" + query + "%"),
                            cb.like(root.get("description"), "%" + query + "%")
                    ))
                    .toList();
            predicates.add(cb.or(namePredicates.toArray(new Predicate[0])));
        }

        if (!categories.isEmpty()) {
            predicates.add(root.get("category").in(categories));
        }

        if (!shopIdn.isEmpty()) {
            predicates.add(root.get("shop").get("id").in(shopIdn));
        }

        root.fetch("resources", JoinType.LEFT);

        criteriaQuery.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Product> query = entityManager.createQuery(criteriaQuery);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }
}

