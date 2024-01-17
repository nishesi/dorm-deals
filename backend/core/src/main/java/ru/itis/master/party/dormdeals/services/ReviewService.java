package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.review.NewReviewForm;
import ru.itis.master.party.dormdeals.dto.review.ReviewDto;

public interface ReviewService {
    ReviewDto addReview(NewReviewForm newReviewForm, Long productId, Long userId);
    ReviewDto updateReview(NewReviewForm newReviewForm, Long productId, Long userId);
    void deleteReview(Long productId, long userId);
    float getRatingProduct(Long productId);
    float getRatingShop(Long shopId);
}
