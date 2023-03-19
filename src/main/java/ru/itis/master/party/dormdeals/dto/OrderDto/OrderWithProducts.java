package ru.itis.master.party.dormdeals.dto.OrderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.Order;
import ru.itis.master.party.dormdeals.models.OrderProduct;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderWithProducts {
    private Order order;
    private List<OrderProduct> orderProducts;
}
