package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.ReviewApi;
import ru.itis.master.party.dormdeals.dto.review.NewReviewForm;
import ru.itis.master.party.dormdeals.dto.review.ReviewDto;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.ReviewService;

@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewApi {
    private final ReviewService reviewService;

    @Override
    public ResponseEntity<ReviewDto> addReviewOnProduct(NewReviewForm newReviewForm, Long productId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(newReviewForm, productId, userId));
    }

    @Override
    public ResponseEntity<ReviewDto> updateReviewOnProduct(NewReviewForm newReviewForm, Long productId, UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.accepted().body(reviewService.updateReview(newReviewForm, productId, userId));
    }

    @Override
    public ResponseEntity<?> deleteReviewOnProduct(Long productId,
                                                   @AuthenticationPrincipal
                                                   UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        reviewService.deleteReview(productId, userId);

        return ResponseEntity.accepted().build();
    }
}
