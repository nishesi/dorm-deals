package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.ProductDto.*;
import ru.itis.master.party.dormdeals.dto.converters.ProductConverter;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.repositories.ProductRepository;
import ru.itis.master.party.dormdeals.repositories.ShopRepository;
import ru.itis.master.party.dormdeals.services.ProductService;
import ru.itis.master.party.dormdeals.dto.converters.CartProductConverter;


import java.util.List;


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
}
