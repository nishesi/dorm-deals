package ru.itis.master.party.dormdeals.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private List<CartProductDto> cartProductDto;

    private Double sumOfProducts;
}
