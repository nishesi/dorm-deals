package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.converters.CartProductConverter;
import ru.itis.master.party.dormdeals.dto.converters.ProductConverter;
import ru.itis.master.party.dormdeals.dto.product.CartProductDto;
import ru.itis.master.party.dormdeals.dto.product.NewProduct;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.product.UpdateProduct;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Review;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.repositories.ProductRepository;
import ru.itis.master.party.dormdeals.repositories.ShopRepository;
import ru.itis.master.party.dormdeals.services.ProductService;

import java.util.List;
import java.util.Objects;

import static ru.itis.master.party.dormdeals.models.Product.State.ACTIVE;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductConverter productConverter;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final CartProductConverter cartProductConverter;
    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    @Transactional
    public ProductDto addProduct(long userId, NewProduct newProduct) {
        Shop shop = shopRepository.findByOwnerId(userId)
                .orElseThrow(() -> new NotFoundException(Shop.class, "ownerId", userId));

        Product product = Product.builder()
                .name(newProduct.getName())
                .description(newProduct.getDescription())
                .category(newProduct.getCategory())
                .price(newProduct.getPrice())
                .countInStorage(newProduct.getCountInStorage())
                .shop(shop)
                .state(ACTIVE)
                .rating(0)
                .build();

        product = productRepository.save(product);
        return productConverter.convertProductInProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto getProduct(Long productId, Long userId, int pageIndex) {
        Product product = productRepository.findWithShopById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));
        List<Review> reviews = product.getReviews();

        int totalReviews = reviews.size();
        int startIndex = pageIndex * defaultPageSize;
        int endIndex = Math.min(startIndex + defaultPageSize, totalReviews);

        List<Review> pagedReviews = reviews.subList(startIndex, endIndex);

        product.setReviews(pagedReviews);
        // hidden or deleted products can get only shop owner
        if (product.getState() != ACTIVE && !Objects.equals(userId, product.getShop().getOwner().getId()))
            throw new NotAcceptableException("have not permission");

        return productConverter.convertProductInProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(long userId, Long productId, UpdateProduct updatedProduct) {
        Product product = productRepository.findWithShopById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));

        if (product.getShop().getOwner().getId() != userId)
            throw new NotAcceptableException("have not permission");

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setCountInStorage(updatedProduct.getCountInStorage());

        return productConverter.convertProductInProductDto(product);
    }

    @Override
    @Transactional
    public void updateProductState(long userId, long productId, Product.State state) {
        Product product = productRepository.findWithShopById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));

        if (product.getShop().getOwner().getId() != userId)
            throw new NotAcceptableException("have not permission");

        product.setState(state);
    }

    @Override
    @Transactional
    public List<CartProductDto> getCartProducts(List<Long> productsId) {
        List<Product> products = productRepository.findAllWithResourcesByIdIn(productsId);
        return cartProductConverter.listFromProduct(products);
    }
}
