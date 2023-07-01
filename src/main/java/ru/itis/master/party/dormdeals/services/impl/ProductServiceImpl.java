package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.product.*;
import ru.itis.master.party.dormdeals.dto.converters.ProductConverter;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.repositories.ProductRepository;
import ru.itis.master.party.dormdeals.repositories.ShopRepository;
import ru.itis.master.party.dormdeals.services.ProductService;
import ru.itis.master.party.dormdeals.dto.converters.CartProductConverter;
import ru.itis.master.party.dormdeals.services.ResourceService;


import java.util.List;
import java.util.UUID;


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
    public ProductsPage getAllProducts(int page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Product> productsPage = productRepository.findAllByStateOrderById(Product.State.ACTIVE, pageRequest);

        return ProductsPage.builder()
                .products(productConverter.from(productsPage.getContent()))
                .totalPageCount(productsPage.getTotalPages())
                .build();
    }

    @Override
    public ProductDto addProduct(long userId, NewProduct newProduct) {
        Shop shop = shopRepository.findShopByOwnerId(userId)
                .orElseThrow(() -> new NotFoundException(Shop.class, "ownerId", userId));

        Product product = Product.builder()
                .name(newProduct.getName())
                .description(newProduct.getDescription())
                .category(newProduct.getCategory())
                .price(newProduct.getPrice())
                .countInStorage(newProduct.getCountInStorage())
                .shop(shop)
                .state(Product.State.ACTIVE)
                .build();

        productRepository.save(product);

        return productConverter.from(product);
    }

    @Override
    public ProductDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));
        return productConverter.from(product);
    }

    @Override
    public ProductDto updateProduct(long userId, Long productId, UpdateProduct updatedProduct) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));

        if (product.getShop().getOwner().getId() != userId)
            throw new NotAcceptableException("have not permission");

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setCountInStorage(updatedProduct.getCountInStorage());

        productRepository.save(product);

        return productConverter.from(product);
    }

    @Override
    public void deleteProduct(long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));

        if (product.getShop().getOwner().getId() != userId)
            throw new NotAcceptableException("have not permission");


        product.setState(Product.State.DELETED);
        productRepository.save(product);
    }

//    @Override
//    public ProductsPage getAllProductsByShop(int page, Long shopId) {
//        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
//        Page<Product> productsPage = productRepository.findAllByShopIdAndStateOrderById(shopId, Product.State.ACTIVE, pageRequest);
//
//        return ProductsPage.builder()
//                .products(from(productsPage.getContent()))
//                .totalPageCount(productsPage.getTotalPages())
//                .build();
//    }

    @Override
    public void returnInSell(long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));

        if (product.getShop().getOwner().getId() != userId)
            throw new NotAcceptableException("have not permission");

        product.setState(Product.State.ACTIVE);
        productRepository.save(product);
    }

    @Override
    public List<CartProductDto> getCartProducts(List<Long> productsId) {
        return cartProductConverter.listFromProduct(productRepository.findAllById(productsId));
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
