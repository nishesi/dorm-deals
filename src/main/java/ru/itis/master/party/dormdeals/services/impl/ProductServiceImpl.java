package ru.itis.master.party.dormdeals.services.impl;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.ProductDto.NewProduct;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.ProductDto.UpdateProduct;
import ru.itis.master.party.dormdeals.exceptions.NotAllowedException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.ShopsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.ProductService;

import java.util.Objects;

import static ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto.from;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productsRepository;
    private final ShopsRepository shopsRepository;
    private final UserRepository userRepository;
    private User thisUser;

    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public ProductsPage getAllProducts(int page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Product> productsPage = productsRepository.findAllByStateOrderById(Product.State.ACTIVE, pageRequest);

        return ProductsPage.builder()
                .products(from(productsPage.getContent()))
                .totalPageCount(productsPage.getTotalPages())
                .build();
    }

    @Override
    public ProductDto addProduct(NewProduct newProduct) {
        initThisUser();
        Shop shop = shopsRepository.findShopByOwnerId(thisUser.getId()).orElseThrow(() -> new NotFoundException("Магазин не найден"));

        Product product = Product.builder()
                .name(newProduct.getName())
                .description(newProduct.getDescription())
                .category(newProduct.getCategory())
                .price(newProduct.getPrice())
                .countInStorage(newProduct.getCountInStorage())
                .uuidOfPhotos(newProduct.getUuidOfPhotos())
                .shop(shop)
                .state(Product.State.ACTIVE)
                .build();

        productsRepository.save(product);

        return from(product);
    }

    @Override
    public ProductDto getProduct(Long productId) {
        Product product = getProductOrThrow(productId);
        return from(product);
    }

    @Override
    public ProductDto updateProduct(Long productId, UpdateProduct updatedProduct) {
        Product productForUpdate = getProductOrThrow(productId);
        checkOwnerShop(productForUpdate.getShop().getOwner().getId());

        productForUpdate.setName(updatedProduct.getName());
        productForUpdate.setDescription(updatedProduct.getDescription());
        productForUpdate.setPrice(updatedProduct.getPrice());
        productForUpdate.setCountInStorage(updatedProduct.getCountInStorage());

        productsRepository.save(productForUpdate);

        return from(productForUpdate);
    }

    @Override
    public void deleteProduct(Long productId) {
        initThisUser();
        Product productForDelete = getProductOrThrow(productId);

        checkOwnerShop(productForDelete.getShop().getOwner().getId());

        productForDelete.setState(Product.State.DELETED);
        productsRepository.save(productForDelete);
    }

//    @Override
//    public ProductsPage getAllProductsByShop(int page, Long shopId) {
//        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
//        Page<Product> productsPage = productsRepository.findAllByShopIdAndStateOrderById(shopId, Product.State.ACTIVE, pageRequest);
//
//        return ProductsPage.builder()
//                .products(from(productsPage.getContent()))
//                .totalPageCount(productsPage.getTotalPages())
//                .build();
//    }

    @Override
    public void returnInSell(Long productId) {
        Product productForReturn = getProductOrThrow(productId);
        checkOwnerShop(productForReturn.getShop().getOwner().getId());

        productForReturn.setState(Product.State.ACTIVE);
        productsRepository.save(productForReturn);
    }


    private Product getProductOrThrow(Long productId) {
        return productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Товар с идентификатором <" + productId + "> не найден"));
    }


    //TODO: вынести эти два метода в отдельный класс
    private void checkOwnerShop(Long ownerShopId) {
        initThisUser();

        if (!Objects.equals(thisUser.getId(), ownerShopId)) {
            throw new NotAllowedException("Вы не являетесь владельцем данного магазина.");
        }
    }
    private void initThisUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        thisUser = userRepository.getByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

}
