package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.repositories.ProductRepository;
import ru.itis.master.party.dormdeals.repositories.ShopRepository;
import ru.itis.master.party.dormdeals.services.ProductService;

import java.util.List;

import static ru.itis.master.party.dormdeals.models.Product.State.ACTIVE;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductConverter productConverter;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final CartProductConverter cartProductConverter;

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
                .build();

        product = productRepository.save(product);
        return productConverter.convertProductInProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto getProduct(long userId, Long productId) {
        Product product = productRepository.findWithShopById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));

        // hidden or deleted products can get only shop owner
        if (product.getState() != ACTIVE && userId != product.getShop().getOwner().getId())
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
