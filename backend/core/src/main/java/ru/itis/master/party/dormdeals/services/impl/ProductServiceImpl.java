package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.product.*;
import ru.itis.master.party.dormdeals.dto.converters.ProductConverter;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
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
import ru.itis.master.party.dormdeals.services.ResourceService;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import static ru.itis.master.party.dormdeals.models.Product.State.ACTIVE;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ResourceService resourceService;
    private final ShopRepository shopRepository;
    private final CartProductConverter cartProductConverter;
    private final ProductConverter productConverter;

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

    @Override
    @Transactional
    public void addProductImage(long userId, Long productId, MultipartFile productImage) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));

        if (userId != product.getShop().getId())
            throw new NotAcceptableException("Have not permission");

        if (product.getResources().size() >= 6)
            throw new NotAcceptableException("Too much images");

        String resourceId = UUID.randomUUID().toString();
        resourceService.saveFile(FileType.IMAGE, EntityType.PRODUCT, resourceId, productImage);
        product.getResources().add(resourceId);
    }

    @Override
    @Transactional
    public void deleteProductImage(long userId, Long productId, String imageId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));

        if (userId != product.getShop().getId())
            throw new NotAcceptableException("Have not permission");

        resourceService.deleteFile(FileType.IMAGE, EntityType.PRODUCT, imageId);
        product.getResources().remove(imageId);
    }
}
