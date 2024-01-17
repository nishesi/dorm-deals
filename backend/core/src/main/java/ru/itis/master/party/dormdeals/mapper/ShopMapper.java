package ru.itis.master.party.dormdeals.mapper;

import lombok.Setter;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.models.jpa.Shop;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        uses = {UserMapper.class,
                DormitoryMapper.class,
                ProductMapper.class},
        builder = @Builder(disableBuilder = true))
public abstract class ShopMapper {

    @Setter(onMethod_ = {@Autowired})
    private ResourceUrlResolver resolver;

    @Named("toShopDto")
    @Mapping(target = "resourceUrl", source = "id", qualifiedByName = "shopIdToImageUrl")
    public abstract ShopDto toShopDto(Shop shop);

    @Mapping(target = "resourceUrl", source = "id", qualifiedByName = "shopIdToImageUrl")
    public abstract ProductDto.Shop toProductDtoShop(Shop shop);

    @Named("shopIdToImageUrl")
    protected String toResourceUrl(Long shopId) {
        return resolver.resolveUrl(FileType.IMAGE, EntityType.SHOP, shopId.toString());
    }
}
