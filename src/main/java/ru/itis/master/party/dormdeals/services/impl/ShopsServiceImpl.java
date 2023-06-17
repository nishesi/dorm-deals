package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.ShopDto.NewShop;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopsPage;
import ru.itis.master.party.dormdeals.dto.ShopDto.UpdateShop;
import ru.itis.master.party.dormdeals.dto.ShopWithProducts;
import ru.itis.master.party.dormdeals.dto.converters.ProductConverter;
import ru.itis.master.party.dormdeals.dto.converters.ShopConverter;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Dormitory;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.DormitoryRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.ShopsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.ShopsService;
import ru.itis.master.party.dormdeals.utils.UserUtil;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ShopsServiceImpl implements ShopsService {

    private final ShopConverter shopConverter;

    private final ProductConverter productConverter;

    private final DormitoryRepository dormitoryRepository;

    private final ProductsRepository productsRepository;

    private final ShopsRepository shopsRepository;

    private final UserRepository userRepository;

    private final UserUtil userUtil;

    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public ShopDto getShop(Long shopId) {
        return shopConverter.from(shopsRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(Shop.class, "id", shopId)));
    }

    @Override
    public ShopsPage getAllShops(int page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Shop> shopsPage = shopsRepository.findAllByOrderByIdAsc(pageRequest);

        return ShopsPage.builder()
                .shops(shopConverter.from(shopsPage.getContent()))
                .totalPagesCount(shopsPage.getTotalPages())
                .build();
    }

    @Override
    public ShopDto createShop(String ownerEmail, NewShop newShop) {
        User thisUser = userRepository.getByEmail(ownerEmail).orElseThrow(() ->
                new NotFoundException(User.class, "email", ownerEmail));

        if (shopsRepository.existsByOwnerId(thisUser.getId()))
            throw new NotAcceptableException("Вы не можете иметь больше одного магазина");

        List<Dormitory> dormitories = dormitoryRepository.findByIdIn(newShop.getDormitoryIdList());

        Shop shop = Shop.builder()
                .name(newShop.getName())
                .description(newShop.getDescription())
                .dormitories(dormitories)
                .owner(thisUser)
                .build();

        shopsRepository.save(shop);
        return shopConverter.from(shop);
    }

    @Override
    public ShopDto updateShop(Long shopId, UpdateShop newShopData) {
        Shop shop = shopsRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(Shop.class, "id", shopId));

        userUtil.checkShopOwner(shop.getOwner().getId(), userUtil.initThisUser(userRepository));

        if (!shop.getName().equals(newShopData.getName()) &&
                shopsRepository.existsByName(newShopData.getName()))
            throw new IllegalArgumentException("shop with name <" + newShopData.getName() + "> already exists");

        List<Dormitory> dormitories = dormitoryRepository.findByIdIn(newShopData.getDormitoryIdList());

        shop.setName(newShopData.getName());
        shop.setDescription(newShopData.getDescription());
        shop.setDormitories(dormitories);

        shopsRepository.save(shop);

        return shopConverter.from(shop);
    }

    @Override
    @Transactional
    public void deleteShop(Long shopId) {
        userUtil.checkShopOwner(
                shopsRepository.findById(shopId).orElseThrow(() -> new NotFoundException(Shop.class, "id", shopId))
                        .getOwner().getId(),
                userUtil.initThisUser(userRepository));

        productsRepository.deleteAllByShopId(shopId);
        shopsRepository.deleteById(shopId);
    }

    @Override
    public ShopWithProducts getAllProductsThisShop(Long shopId, int page) {
        Shop thisShop = shopsRepository.findById(shopId).orElseThrow(() -> new NotFoundException(Shop.class, "id", shopId));
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Product> productsPageTemp = productsRepository
                .findAllByShopIdAndStateOrderById(shopId, Product.State.ACTIVE, pageRequest);
        ProductsPage productsPage = ProductsPage.builder()
                .products(productConverter.from(productsPageTemp.getContent()))
                .totalPageCount(productsPageTemp.getTotalPages())
                .build();

        return ShopWithProducts.builder()
                .shop(shopConverter.from(thisShop))
                .productsPage(productsPage)
                .build();
    }
}
