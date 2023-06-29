package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.review.ReviewDto;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.user.UserDtoForShopAndReview;
import ru.itis.master.party.dormdeals.models.File;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Review;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final ResourceUrlResolver resolver;
    public ProductDto from(Product product) {

        //TODO: определять тип файла
        List<String> imageUrls = IntStream.range(0, product.getResources().size())
                .mapToObj(index -> resolver.resolveUrl(product.getId(), File.FileDtoType.PRODUCT, File.FileType.IMAGE, index + 1))
                .toList();

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .countInStorage(product.getCountInStorage())
                .rating(product.getRating())
                .state(product.getState())
                .resources(imageUrls)
                .shop(from(product.getShop()))
                .reviews(fromReview(product.getReviews()))
                .build();
    }

    private ShopDto from(Shop shop) {
        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .rating(shop.getRating())
                .resourceUrl(shop.getResource())
                .build();
    }
    private ReviewDto from(Review review) {
        return ReviewDto.builder()
                .user(UserDtoForShopAndReview.builder()
                        .id(review.getUser().getId())
                        .firstName(review.getUser().getFirstName())
                        .lastName(review.getUser().getLastName())
                        .build())
                .message(review.getMessage())
                .score(review.getScore())
                .build();
    }

    private List<ReviewDto> fromReview(List<Review> reviews) {
        return reviews.stream().map(this::from).collect(Collectors.toList());
    }

    public List<ProductDto> from(List<Product> products) {
        return products
                .stream()
                .map(this::from)
                .toList();
    }
}
