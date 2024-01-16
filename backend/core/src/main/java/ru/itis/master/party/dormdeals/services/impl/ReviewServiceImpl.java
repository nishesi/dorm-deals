package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.converters.ReviewConverter;
import ru.itis.master.party.dormdeals.dto.review.NewReviewDto;
import ru.itis.master.party.dormdeals.dto.review.ReviewDto;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
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
    private final ReviewConverter reviewConverter;

    @Override
    @Transactional
    public ReviewDto addReview(NewReviewDto newReviewDto, Long productId, Long userId) {

        if (reviewRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new NotAcceptableException("У вас уже есть отзыв на этот товар");
        }

        User user = userRepository.getReferenceById(userId);
        Product product = productRepository.getReferenceById(productId);
        Shop shop = product.getShop();

        Review review = reviewRepository.save(Review.builder()
                .message(newReviewDto.getMessage())
                .score(newReviewDto.getScore())
                .user(user)
                .product(product)
                .build());

        product.setRating(getRatingProduct(productId));
        shop.setRating(getRatingShop(shop.getId()));

        productRepository.save(product);
        shopRepository.save(shop);


        return reviewConverter.from(review, user, product);
    }


    @Override
    @Transactional
    public ReviewDto updateReview(NewReviewDto newReviewDto, Long productId, Long userId) {
        Review reviewForUpdate = reviewRepository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new NotFoundException(Review.class, "id", productId));
        reviewForUpdate.setScore(newReviewDto.getScore());
        reviewForUpdate.setMessage(newReviewDto.getMessage());
        reviewRepository.save(reviewForUpdate);

        updateRatingProductAndShop(reviewForUpdate);

        User user = reviewForUpdate.getUser();
        Product product = reviewForUpdate.getProduct();

        return reviewConverter.from(reviewForUpdate, user, product);
    }

    @Override
    @Transactional
    public void deleteReview(Long productId, long userId) {
        Optional<Review> reviewOptional = reviewRepository.findByProductIdAndUserId(productId, userId);
        reviewOptional.ifPresent(review -> {

            if (review.getUser().getId() != userId) {
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
