package ru.itis.master.party.dormdeals.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.models.jpa.order.Order;
import ru.itis.master.party.dormdeals.models.jpa.order.OrderMessage;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        uses = {UserMapper.class, ProductMapper.class},
        builder = @Builder(disableBuilder = true))
public abstract class OrderMapper {

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected ShopMapper shopMapper;

    @Mapping(target = "products")
    @Mapping(target = "messages", source = "orderMessages")
    public abstract OrderDto toOrderDtoWithMessages(Order order, Page<OrderMessage> orderMessages);

    public Page<OrderDto> toOrderDtoPageForShop(Page<Order> orders) {
        return orders.map(this::toOrderDtoInPage);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "shop", source = "shop")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "addedDate", source = "addedDate")
    @Mapping(target = "price", source = "price")
    protected abstract OrderDto toOrderDtoInPage(Order order);

    protected Page<OrderDto.Message> toOrderDtoMessagePage(Page<OrderMessage> orderMessages) {
        return orderMessages.map(this::toOrderDtoMessage);
    }

    protected abstract OrderDto.Message toOrderDtoMessage(OrderMessage orderMessage);
}
