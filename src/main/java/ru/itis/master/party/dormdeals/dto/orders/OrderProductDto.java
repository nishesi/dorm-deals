package ru.itis.master.party.dormdeals.dto.orders;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    private ProductDto productDto;
    private Integer count;

}
