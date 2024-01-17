package ru.itis.master.party.dormdeals.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.shop.ShopWithProductsDto;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.models.jpa.Product;
import ru.itis.master.party.dormdeals.models.jpa.Shop;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        uses = {UserMapper.class, DormitoryMapper.class, ProductMapper.class},
        builder = @Builder(disableBuilder = true))
public abstract class ShopMapper {

    @Autowired
    private ResourceUrlResolver resolver;

    @Named("toShopDto")
    @Mapping(target = "resourceUrl", source = "id", qualifiedByName = "shopIdToImageUrl")
    public abstract ShopDto toShopDto(Shop shop);

    public ShopDto toShopDtoWithPureInfo(Shop shop) {
        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .build();
    }

    @Mapping(target = "resourceUrl", source = "id", qualifiedByName = "shopIdToImageUrl")
    public abstract ProductDto.Shop toProductDtoShop(Shop shop);

    @Named("shopIdToImageUrl")
    protected String toResourceUrl(Long shopId) {
        return resolver.resolveUrl(FileType.IMAGE, EntityType.SHOP, shopId.toString());
    }

    @Mapping(target = "productsPage", source = "products")
    public abstract ShopWithProductsDto toShopWithProductsDto(Shop shop, Page<Product> products);
}
