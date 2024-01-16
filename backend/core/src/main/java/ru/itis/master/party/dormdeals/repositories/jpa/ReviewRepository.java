package ru.itis.master.party.dormdeals.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.master.party.dormdeals.models.jpa.Review;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select coalesce(avg(r.score), 0) from Review r where r.product.id = :idProduct")
    float getAverageScoreProduct(Long idProduct);

    @Query("select coalesce(avg(r.score), 0) from Review r where r.product.shop.id = :idShop")
    float getAverageScoreShop(Long idShop);

    Optional<Review> findByProductIdAndUserId(Long idProduct, Long userId);

    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
