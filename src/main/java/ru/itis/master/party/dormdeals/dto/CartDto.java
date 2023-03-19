package ru.itis.master.party.dormdeals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDtoCart;
import ru.itis.master.party.dormdeals.models.Cart;

import static ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto.from;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDto {
    private List<ProductDtoCart> productDtoCart;
    private Double sumOfProducts;
}
