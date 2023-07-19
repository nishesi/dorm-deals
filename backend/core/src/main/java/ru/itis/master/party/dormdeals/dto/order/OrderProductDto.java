package ru.itis.master.party.dormdeals.dto.order;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    private ProductDto productDto;
    private Integer count;
    private Float price;
}
