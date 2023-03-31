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
import ru.itis.master.party.dormdeals.exceptions.NotCreateSecondShop;
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
import ru.itis.master.party.dormdeals.utils.GetOrThrow;
import ru.itis.master.party.dormdeals.utils.OwnerChecker;

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

    private final OwnerChecker ownerChecker;

    private final GetOrThrow getOrThrow;

    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public ShopDto getShop(Long shopId) {
        return shopConverter.from(getOrThrow.getShopOrThrow(shopId, shopsRepository));
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
                new NotFoundException("user with email <" + ownerEmail + "> not found"));

        if (shopsRepository.existsByOwnerId(thisUser.getId()))
            throw new NotCreateSecondShop("Вы не можете иметь больше одного магазина");

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
        Shop shop = getOrThrow.getShopOrThrow(shopId, shopsRepository);

        ownerChecker.checkOwnerShop(shop.getOwner().getId(), ownerChecker.initThisUser(userRepository));

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
        ownerChecker.checkOwnerShop(
                getOrThrow.getShopOrThrow(shopId, shopsRepository).getOwner().getId(),
                ownerChecker.initThisUser(userRepository));

        productsRepository.deleteAllByShopId(shopId);
        shopsRepository.deleteById(shopId);
    }

    @Override
    public ShopWithProducts getAllProductsThisShop(Long shopId, int page) {
        Shop thisShop = getOrThrow.getShopOrThrow(shopId, shopsRepository);
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
