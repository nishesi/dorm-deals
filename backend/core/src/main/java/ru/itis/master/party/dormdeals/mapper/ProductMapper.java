package ru.itis.master.party.dormdeals.mapper;

import lombok.Setter;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.dto.product.CartProductDto;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.product.ProductPage;
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
import static ru.itis.master.party.dormdeals.enums.EntityType.PRODUCT;
import static ru.itis.master.party.dormdeals.enums.FileType.IMAGE;

@Mapper(componentModel = SPRING,
        uses = {ShopMapper.class},
        builder = @Builder(disableBuilder = true))
public abstract class ProductMapper {

    @Setter(onMethod_={@Autowired})
    protected ResourceUrlResolver resolver;

    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "resources", source = "resources", qualifiedByName = "createProductImageUrls")
    public abstract ProductDto toProductDto(Product product);

    @Mapping(target = "name", source = "product.name")
    public abstract OrderDto.Product toOrderDtoProduct(OrderProduct product);

    public abstract List<ProductDto> toProductDtoList(List<Product> products);

    @Mapping(target = "coverImage", source = "product.resources", qualifiedByName = "createFirstImageUrl")
    public abstract CartProductDto toCartProductDto(CartProduct cartProduct);

    @BeanMapping(nullValueMappingStrategy = RETURN_NULL)
    @Mapping(target = "coverImage", source = "resources", qualifiedByName = "createFirstImageUrl")
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "count", ignore = true)
    @Mapping(target = "product", source = "product")
    public abstract CartProductDto toCartProductDto(Product product);

    public abstract List<CartProductDto> toCartProductDtoListFroCartProductList(List<CartProduct> cartProducts);

    public abstract List<CartProductDto> toCartProductDtoListFromProductList(List<Product> products);

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

    //TODO: определять тип файла
    @Named("createProductImageUrls")
    protected List<String> createProductImageUrls(List<String> resources) {
        return resources.stream()
                .map(id -> resolver.resolveUrl(IMAGE, PRODUCT, id))
                .toList();
    }

    @Named("createFirstImageUrl")
    protected String createFirstImageUrl(List<String> images) {
        return (images.isEmpty()) ? "" :
                resolver.resolveUrl(IMAGE, PRODUCT, images.getFirst());
    }

    @Named("buildDescription")
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

    public abstract ProductPage toProductPage(Page<Product> products);
}
