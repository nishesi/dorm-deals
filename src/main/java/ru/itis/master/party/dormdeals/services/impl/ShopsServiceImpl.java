package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.ShopDto.NewShop;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopsPage;
import ru.itis.master.party.dormdeals.dto.ShopDto.UpdateShop;
import ru.itis.master.party.dormdeals.exceptions.NotCreateSecondShop;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.dto.ShopWithProducts;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.ShopsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.ShopsService;
import ru.itis.master.party.dormdeals.utils.OwnerChecker;
import ru.itis.master.party.dormdeals.utils.GetOrThrow;

import static ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto.from;

@Service
@RequiredArgsConstructor

public class ShopsServiceImpl implements ShopsService {
    private final ShopsRepository shopsRepository;
    private final ProductsRepository productsRepository;
    private final UserRepository userRepository;
    private final OwnerChecker ownerChecker;
    private final GetOrThrow getOrThrow;



    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public ShopDto getShop(Long shopId) {
        return from(getOrThrow.getShopOrThrow(shopId, shopsRepository));
    }

    @Override
    public ShopsPage getAllShops(int page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Shop> shopsPage = shopsRepository.findAllByOrderByIdAsc(pageRequest);

        return ShopsPage.builder()
                .shops(from(shopsPage.getContent()))
                .totalPagesCount(shopsPage.getTotalPages())
                .build();
    }

    @Override
    public ShopDto createShop(NewShop newShop) {
        User thisUser = ownerChecker.initThisUser(userRepository);

        if (shopsRepository.countShopsByOwnerId(thisUser.getId()) >= 1) {
            throw new NotCreateSecondShop("Вы не можете иметь больше одного магазина");
        }

        Shop shop = Shop.builder()
                .name(newShop.getName())
                .description(newShop.getDescription())
                .rating(newShop.getRating())
                .placeSells(newShop.getPlaceSells())
                .owner(User.builder()
                        .id(thisUser.getId())
                        .firstName(thisUser.getFirstName())
                        .lastName(thisUser.getLastName())
                        .dormitory(thisUser.getDormitory())
                        .build())
                .build();

        shopsRepository.save(shop);
        return from(shop);
    }

    @Override
    public ShopDto updateShop(Long shopId, UpdateShop updateShop) {
        Shop shopForUpdate = getOrThrow.getShopOrThrow(shopId, shopsRepository);

        ownerChecker.checkOwnerShop(shopForUpdate.getOwner().getId(), ownerChecker.initThisUser(userRepository));

        shopForUpdate.setName(updateShop.getName());
        shopForUpdate.setDescription(updateShop.getDescription());
        shopForUpdate.setRating(updateShop.getRating());
        shopForUpdate.setPlaceSells(updateShop.getPlaceSells());

        shopsRepository.save(shopForUpdate);

        return from(shopForUpdate);
    }

    @Override
    @Transactional
    public void deleteShop(Long shopId) {
        ownerChecker.checkOwnerShop(getOrThrow.getShopOrThrow(shopId, shopsRepository).getOwner().getId(), ownerChecker.initThisUser(userRepository));
        productsRepository.deleteAllByShopId(shopId);
        shopsRepository.deleteById(shopId);
    }

    @Override
    public ShopWithProducts getAllProductsThisShop(Long shopId, int page) {
        Shop thisShop = getOrThrow.getShopOrThrow(shopId, shopsRepository);
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Product> productsPageTemp = productsRepository
                .findAllByShopIdAndStateOrderById(shopId, Product.State.ACTIVE, pageRequest);
        ProductsPage productsPage= ProductsPage.builder()
                .products(ProductDto.from(productsPageTemp.getContent()))
                .totalPageCount(productsPageTemp.getTotalPages())
                .build();

        return ShopWithProducts.builder()
                .shop(thisShop)
                .productsPage(productsPage)
                .build();
    }
}
