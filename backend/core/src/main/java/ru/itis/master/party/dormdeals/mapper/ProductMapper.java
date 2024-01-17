package ru.itis.master.party.dormdeals.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.dto.product.CartProductDto;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.shop.ShopWithProductsDto;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.models.elasticsearch.ItemElastic;
import ru.itis.master.party.dormdeals.models.jpa.CartProduct;
import ru.itis.master.party.dormdeals.models.jpa.Product;
import ru.itis.master.party.dormdeals.models.jpa.order.OrderProduct;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValueMappingStrategy.RETURN_NULL;

@Mapper(componentModel = SPRING,
        uses = {ShopMapper.class},
        builder = @Builder(disableBuilder = true))
public abstract class ProductMapper {

    @Autowired
    protected ResourceUrlResolver resolver;

    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "resources", source = "resources", qualifiedByName = "createProductImageUrls")
    public abstract ProductDto toProductDto(Product product);

    @Mapping(target = "name", source = "product.name")
    public abstract OrderDto.Product toOrderDtoProduct(OrderProduct product);

    @Mapping(target = "resources", source = "resources", qualifiedByName = "createProductImageUrls")
    public abstract ShopWithProductsDto.Product toShopWithProductsDtoProduct(Product product);

    public abstract List<ProductDto> toProductDtoList(List<Product> products);

    @Mapping(target = "coverImage", source = "product.resources", qualifiedByName = "createFirstImageUrl")
    public abstract CartProductDto toCartProductDto(CartProduct cartProduct);

    @BeanMapping(nullValueMappingStrategy = RETURN_NULL)
    @Mapping(target = "coverImage", source = "resources", qualifiedByName = "createFirstImageUrl")
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "product", source = "product")
    public abstract CartProductDto toCartProductDto(Product product);

    public Page<ShopWithProductsDto.Product> toProductDtoForShopPage(Page<Product> products) {
        return products.map(this::toShopWithProductsDtoProduct);
    }

    public ItemElastic toItemElastic(Product p) {
        String desc = buildDescription(p.getDescription());
        return ItemElastic.builder()

                .itemId(p.getId())
                .name(p.getName())
                .catalogueId(p.getCatalogue().getId())
                .catalogue(p.getCatalogue().getName())
                .brand(p.getBrand())
                .type(p.getType())
                .description(desc)
                .fulltext(p.getCatalogue().getName() + " " + p.getType() + " " + p.getName() + " " + desc)
                .build();
    }


    public abstract List<CartProductDto> toCartProductDtoListFroCartProductList(List<CartProduct> cartProducts);

    public abstract List<CartProductDto> toCartProductDtoListFromProductList(List<Product> products);

    //TODO: определять тип файла
    @Named("createProductImageUrls")
    protected List<String> createProductImageUrls(List<String> resources) {
        return resources.stream()
                .map(id -> resolver.resolveUrl(FileType.IMAGE, EntityType.PRODUCT, id))
                .toList();
    }

    @Named("createFirstImageUrl")
    protected String createFirstImageUrl(List<String> images) {
        return (images.isEmpty()) ? "" :
                resolver.resolveUrl(FileType.IMAGE, EntityType.PRODUCT, images.getFirst());
    }

    protected String buildDescription(String description) {
        return Arrays.stream(description.split(";"))
                .filter(s -> !s.contains(": нет")
                        && !s.contains(": -")
                        && !s.contains(": 0"))
                .map(s -> s.toLowerCase(Locale.ROOT)
                        .replace(": есть", "")
                        .replace(":", ""))
                .collect(Collectors.joining());
    }
}
