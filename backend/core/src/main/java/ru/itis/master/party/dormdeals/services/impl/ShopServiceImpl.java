package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.shop.NewShopForm;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.shop.UpdateShopForm;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.mapper.ShopMapper;
import ru.itis.master.party.dormdeals.models.jpa.Dormitory;
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

    private final ShopMapper shopMapper;

    @Override
    public ShopDto getShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(Shop.class, "id", shopId));
        return shopMapper.toShopDto(shop);
    }

    @Override
    public ShopDto createShop(long userId, NewShopForm form) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(User.class, "email", userId));

        if (shopRepository.existsByOwnerId(user.getId()))
            throw new NotAcceptableException("Вы не можете иметь больше одного магазина");

        List<Dormitory> dormitories = dormitoryRepository.findByIdIn(form.dormitoryIdList());

        Shop shop = Shop.builder()
                .name(form.name())
                .description(form.description())
                .dormitories(dormitories)
                .owner(user)
                .build();

        shopRepository.save(shop);

        return shopMapper.toShopDto(shop);
    }

    @Override
    @Transactional
    public ShopDto updateShop(long userId, Long shopId, UpdateShopForm form) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(Shop.class, "id", shopId));

        if (shop.getOwner().getId() != userId)
            throw new NotAcceptableException("have not permission");

        String newShopName = form.name();

        if (!shop.getName().equals(newShopName) &&
                shopRepository.existsByName(newShopName))
            throw new IllegalArgumentException("shop with name <" + newShopName + "> already exists");

        List<Dormitory> dormitories = dormitoryRepository.findByIdIn(form.dormitoryIdList());

        shop.setName(newShopName);
        shop.setDescription(form.description());
        shop.setDormitories(dormitories);

        shopRepository.save(shop);

        return shopMapper.toShopDto(shop);
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
    public void updateShopImage(long userId, MultipartFile shopImage) {
        Shop shop = shopRepository.findByOwnerId(userId)
                .orElseThrow(() -> new NotFoundException(Shop.class, "ownerId", userId));

        resourceService.saveFile(FileType.IMAGE, EntityType.SHOP, String.valueOf(shop.getId()), shopImage);
    }
}
