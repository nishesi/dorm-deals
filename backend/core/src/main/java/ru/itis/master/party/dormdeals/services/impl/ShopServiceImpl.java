package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.product.ProductsPage;
import ru.itis.master.party.dormdeals.dto.shop.NewShop;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.shop.UpdateShop;
import ru.itis.master.party.dormdeals.dto.ShopWithProducts;
import ru.itis.master.party.dormdeals.dto.converters.ProductConverter;
import ru.itis.master.party.dormdeals.dto.converters.ShopConverter;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.jpa.Dormitory;
import ru.itis.master.party.dormdeals.models.jpa.Product;
import ru.itis.master.party.dormdeals.models.jpa.Shop;
import ru.itis.master.party.dormdeals.models.jpa.User;
import ru.itis.master.party.dormdeals.repositories.jpa.DormitoryRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.ProductRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.ShopRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.UserRepository;
import ru.itis.master.party.dormdeals.services.ResourceService;
import ru.itis.master.party.dormdeals.services.ShopService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ShopServiceImpl implements ShopService {

    private final DormitoryRepository dormitoryRepository;

    private final ProductRepository productRepository;

    private final ResourceService resourceService;

    private final ShopRepository shopRepository;

    private final UserRepository userRepository;

    private final ProductConverter productConverter;

    private final ShopConverter shopConverter;

    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public ShopDto createShop(long userId, NewShop newShop) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(User.class, "email", userId));

        if (shopRepository.existsByOwnerId(user.getId()))
            throw new NotAcceptableException("Вы не можете иметь больше одного магазина");

        List<Dormitory> dormitories = dormitoryRepository.findByIdIn(newShop.getDormitoryIdList());

        Shop shop = Shop.builder()
                .name(newShop.getName())
                .description(newShop.getDescription())
                .dormitories(dormitories)
                .owner(user)
                .build();

        shopRepository.save(shop);

        return shopConverter.from(shop);
    }

    @Override
    @Transactional
    public ShopDto updateShop(long userId, Long shopId, UpdateShop newShopData) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(Shop.class, "id", shopId));

        if (shop.getOwner().getId() != userId)
            throw new NotAcceptableException("have not permission");

        String newShopName = newShopData.getName();

        if (!shop.getName().equals(newShopName) &&
                shopRepository.existsByName(newShopName))
            throw new IllegalArgumentException("shop with name <" + newShopName + "> already exists");

        List<Dormitory> dormitories = dormitoryRepository.findByIdIn(newShopData.getDormitoryIdList());

        shop.setName(newShopName);
        shop.setDescription(newShopData.getDescription());
        shop.setDormitories(dormitories);

        shopRepository.save(shop);

        return shopConverter.from(shop);
    }

    @Override
    @Transactional
    public void deleteShop(long userId) {
        Optional<Shop> shopOptional = shopRepository.findByOwnerId(userId);
        shopOptional.ifPresent(shop -> {
            productRepository.deleteAllByShopId(shop.getId());
            shopRepository.deleteById(shop.getId());
        });
    }

    @Override
    @Transactional
    public ShopWithProducts getAllProductsThisShop(Long shopId, int page) {
        Shop thisShop = shopRepository.findById(shopId).orElseThrow(() -> new NotFoundException(Shop.class, "id", shopId));
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Product> productsPageTemp = productRepository
                .findAllByShopIdAndStateOrderById(shopId, Product.State.ACTIVE, pageRequest);

        ProductsPage productsPage = ProductsPage.builder()
                .products(productConverter
                        .convertListProductInListProductDtoForShop(productsPageTemp
                                .getContent()))
                .totalPageCount(productsPageTemp.getTotalPages())
                .build();

        return ShopWithProducts.builder()
                .shop(shopConverter.from(thisShop))
                .productsPage(productsPage)
                .build();
    }

    @Override
    public void updateShopImage(long userId, MultipartFile shopImage) {
        Shop shop = shopRepository.findByOwnerId(userId)
                .orElseThrow(() -> new NotFoundException(Shop.class, "ownerId", userId));

        resourceService.saveFile(FileType.IMAGE, EntityType.SHOP, String.valueOf(shop.getId()), shopImage);
    }
}
