package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.product.ProductDtoForReview;
import ru.itis.master.party.dormdeals.dto.review.ReviewDto;
import ru.itis.master.party.dormdeals.dto.user.UserDtoForShopAndReview;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Review;
import ru.itis.master.party.dormdeals.models.User;

@Component
@RequiredArgsConstructor
public class ReviewConverter {
    public ReviewDto from(Review review, User user, Product product) {
        return ReviewDto.builder()
                .user(UserDtoForShopAndReview.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .product(ProductDtoForReview.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .build())
                .message(review.getMessage())
                .score(review.getScore())
                .build();
    }

}
