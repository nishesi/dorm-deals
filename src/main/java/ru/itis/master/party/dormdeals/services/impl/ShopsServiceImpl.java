package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.InitBinder;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.ShopDto.NewShop;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopsPage;
import ru.itis.master.party.dormdeals.dto.ShopDto.UpdateShop;
import ru.itis.master.party.dormdeals.exceptions.NotAllowedException;
import ru.itis.master.party.dormdeals.exceptions.NotCreateSecondShop;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Role;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.dto.ShopWithProducts;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.ShopsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.ShopsService;

import java.util.Objects;

import static ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto.from;

@Service
@RequiredArgsConstructor

public class ShopsServiceImpl implements ShopsService {
    private final ShopsRepository shopsRepository;
    private final ProductsRepository productsRepository;
    private final UserRepository userRepository;



    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public ShopDto getShop(long id) {
        return from(getShopOrThrow(id));
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
        User thisUser = initThisUser();

        if (shopsRepository.countShopsByOwnerId(thisUser.getId()) >= 1) {
            throw new NotCreateSecondShop("Вы не можете иметь больше одного магазина");
        }

//TODO: чтобы создание магазина отдавало дтошку, а не основного юзера
//        UserDto ownerDto = from(user);
        Shop shop = Shop.builder()
                .name(newShop.getName())
                .description(newShop.getDescription())
                .rating(newShop.getRating())
                .placeSells(newShop.getPlaceSells())
                .owner(thisUser)
                .build();

        shopsRepository.save(shop);
        return from(shop);
    }

    @Override
    public ShopDto updateShop(Long id, UpdateShop updateShop) {
        checkOwnerShop(id, initThisUser());

        Shop shopForUpdate = getShopOrThrow(id);

        shopForUpdate.setName(updateShop.getName());
        shopForUpdate.setDescription(updateShop.getDescription());
        shopForUpdate.setRating(updateShop.getRating());
        shopForUpdate.setPlaceSells(updateShop.getPlaceSells());

        shopsRepository.save(shopForUpdate);

        return from(shopForUpdate);
    }

    @Override
    public void deleteShop(Long id) {
        checkOwnerShop(id, initThisUser());

        shopsRepository.deleteById(id);
    }

    @Override
    public ShopWithProducts getAllProductsThisShop(Long shopId, int page) {
        Shop thisShop = getShopOrThrow(shopId);
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

    private Shop getShopOrThrow(long id) {
        return shopsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Магазин с идентификатором <" + id + "> не найден"));
    }
    private void checkOwnerShop(Long shopId, User thisUser) {
        if (!Objects.equals(thisUser.getId(), shopId)) {
            throw new NotAllowedException("Вы не являетесь владельцем данного магазина.");
        }
    }
    private User initThisUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.getByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

}
