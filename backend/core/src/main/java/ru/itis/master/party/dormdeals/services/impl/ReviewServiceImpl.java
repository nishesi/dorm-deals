package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.review.NewReviewForm;
import ru.itis.master.party.dormdeals.dto.review.ReviewDto;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.mapper.ReviewMapper;
import ru.itis.master.party.dormdeals.models.jpa.Product;
import ru.itis.master.party.dormdeals.models.jpa.Review;
import ru.itis.master.party.dormdeals.models.jpa.Shop;
import ru.itis.master.party.dormdeals.models.jpa.User;
import ru.itis.master.party.dormdeals.repositories.jpa.ProductRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.ReviewRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.ShopRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.UserRepository;
import ru.itis.master.party.dormdeals.services.ReviewService;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewDto addReview(NewReviewForm newReviewForm, Long productId, Long userId) {

        if (reviewRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new NotAcceptableException("У вас уже есть отзыв на этот товар");
        }

        User user = userRepository.getReferenceById(userId);
        Product product = productRepository.getReferenceById(productId);
        Shop shop = product.getShop();

        Review review = reviewRepository.save(Review.builder()
                .message(newReviewForm.message())
                .score(newReviewForm.score())
                .author(user)
                .product(product)
                .build());

        product.setRating(getRatingProduct(productId));
        shop.setRating(getRatingShop(shop.getId()));

        product = productRepository.save(product);
        shopRepository.save(shop);

        return reviewMapper.toReviewDto(review);
    }


    @Override
    @Transactional
    public ReviewDto updateReview(NewReviewForm newReviewForm, Long productId, Long userId) {
        Review review = reviewRepository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new NotFoundException(Review.class, "id", productId));
        review.setScore(newReviewForm.score());
        review.setMessage(newReviewForm.message());
        reviewRepository.save(review);

        updateRatingProductAndShop(review);

        return reviewMapper.toReviewDto(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long productId, long userId) {
        Optional<Review> reviewOptional = reviewRepository.findByProductIdAndUserId(productId, userId);
        reviewOptional.ifPresent(review -> {

            if (review.getAuthor().getId() != userId) {
                throw new NotAcceptableException("have not permission");
            }

            reviewRepository.delete(review);

            updateRatingProductAndShop(review);

        });
    }

    @Override
    public float getRatingProduct(Long productId) {
        return reviewRepository.getAverageScoreProduct(productId);
    }

    @Override
    public float getRatingShop(Long shopId) {
        return reviewRepository.getAverageScoreShop(shopId);
    }

    private void updateRatingProductAndShop(Review review) {
        Product product = review.getProduct();
        Shop shop = product.getShop();

        product.setRating(getRatingProduct(product.getId()));
        shop.setRating(getRatingShop(shop.getId()));

        productRepository.save(product);
        shopRepository.save(shop);
    }
}
